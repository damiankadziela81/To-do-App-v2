package com.example.model.projection;

import com.example.model.TaskGroup;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupWriteModel {
    private String description;
    private Set<TaskWriteModel> tasks;

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Set<TaskWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(final Set<TaskWriteModel> tasks) {
        this.tasks = tasks;
    }

    public TaskGroup toGroup(){
        return new TaskGroup(description, tasks.stream()
                .map(TaskWriteModel::toTask)
                .collect(Collectors.toSet()));
    }
}