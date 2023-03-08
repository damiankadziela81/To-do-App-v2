package com.example.model.projection;

import com.example.model.Task;
import com.example.model.TaskGroup;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class TaskWriteModel {
    @NotBlank(message = "Task must have a description.")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
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
