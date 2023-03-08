package com.example.model.projection;

import com.example.model.Project;
import com.example.model.TaskGroup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupWriteModel {
    @NotBlank(message = "Task group must have a description.")
    private String description;
    @Valid
    private List<TaskWriteModel> tasks = new ArrayList<>();

    public GroupWriteModel() {
        tasks.add(new TaskWriteModel());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public List<TaskWriteModel> getTasks() {
        return tasks;
    }

    public void setTasks(final List<TaskWriteModel> tasks) {
        this.tasks = tasks;
    }

    public TaskGroup toGroup(final Project project) {
        var result = new TaskGroup();
        result.setDescription(description);
        result.setTasks(
                tasks.stream()
                        .map(source -> source.toTask(result))
                        .collect(Collectors.toSet())
        );
        result.setProject(project);
        return result;
    }
}
