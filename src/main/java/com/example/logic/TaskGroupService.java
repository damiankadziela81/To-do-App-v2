package com.example.logic;

import com.example.model.Project;
import com.example.model.TaskGroup;
import com.example.model.TaskGroupRepository;
import com.example.model.TaskRepository;
import com.example.model.projection.GroupReadModel;
import com.example.model.projection.GroupWriteModel;

import java.util.List;
import java.util.stream.Collectors;

public class TaskGroupService {
    private TaskGroupRepository repository;
    private TaskRepository taskRepository;

    TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) {
        return createGroup(source, null);
    }

    GroupReadModel createGroup(final GroupWriteModel source, final Project project) {
        TaskGroup result = repository.save(source.toGroup(project));
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll() {
        return repository.findAll().stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) {
        if(taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)) {
            throw new IllegalStateException("Group still has pending tasks. Finish all the tasks first! ");
        }
        TaskGroup result = repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("TaskGroup with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }
}
