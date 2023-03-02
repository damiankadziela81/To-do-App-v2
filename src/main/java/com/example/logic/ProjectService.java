package com.example.logic;

import com.example.TaskConfiguration;
import com.example.model.*;
import com.example.model.projection.GroupReadModel;
import com.example.model.projection.GroupWriteModel;
import com.example.model.projection.TaskWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfiguration taskConfiguration;
    private TaskGroupService taskGroupService;

    public ProjectService(
            final ProjectRepository repository,
            final TaskGroupRepository taskGroupRepository,
            final TaskConfiguration taskConfiguration,
            final TaskGroupService taskGroupService
    ) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskConfiguration = taskConfiguration;
        this.taskGroupService = taskGroupService;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project create(Project source) {
        return repository.save(source);
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline) {
        if (!taskConfiguration.getTemplate().isAllowMultipleTasks()
                && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one pending group from project is allowed");
        }
        return repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(step -> {
                                                var task = new TaskWriteModel();
                                                task.setDescription(step.getDescription());
                                                task.setDeadline(deadline.plusDays(step.getDaysToDeadline()));
                                                return task;
                                            }
                                    ).collect(Collectors.toSet()));
                    return taskGroupService.createGroup(targetGroup);
                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
    }
}
