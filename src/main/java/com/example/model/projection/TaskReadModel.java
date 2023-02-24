package com.example.model.projection;

import com.example.model.Task;

public class TaskReadModel {
    private String description;
    private boolean done;

    public TaskReadModel(Task source) {
        description = source.getDescription();
        done = source.isDone();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(final boolean done) {
        this.done = done;
    }


}
