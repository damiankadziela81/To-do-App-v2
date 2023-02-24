package com.example.logic;

import com.example.TaskConfiguration;
import com.example.model.*;
import com.example.model.projection.GroupReadModel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private ProjectRepository repository;
    private TaskGroupRepository taskGroupRepository;
    private TaskConfiguration taskConfiguration;

    public ProjectService(final ProjectRepository repository, final TaskGroupRepository taskGroupRepository, final TaskConfiguration taskConfiguration) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.taskConfiguration = taskConfiguration;
    }

    public List<Project> readAll(){
        return repository.findAll();
    }

    public Project create(Project source){
        return repository.save(source);
    }

    public GroupReadModel createGroup(int projectId, LocalDateTime deadline) {
        if(!taskConfiguration.getTemplate().isAllowMultipleTasks()
                && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one pending group from project is allowed");
        }
        TaskGroup result = repository.findById(projectId)
                .map(project -> {
                    return new TaskGroup(project.getDescription(),
                            project.getSteps().stream()
                                    .map(step -> new Task(step.getDescription(),
                                            deadline.plusDays(step.getDaysToDeadline())))
                                    .collect(Collectors.toSet()));
                }).orElseThrow(()-> new IllegalArgumentException("Project with given id not found"));
        return new GroupReadModel(result);
    }
}
