package com.example.model.projection;

import com.example.model.Task;
import com.example.model.TaskGroup;

import java.time.LocalDateTime;

public class TaskWriteModel {
    private String description;
    private LocalDateTime deadline;

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(final LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public Task toTask(final TaskGroup taskGroup){
        return new Task(description, deadline, taskGroup);
    }
}
