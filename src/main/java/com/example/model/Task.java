package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private boolean done;

    Task() {
    }

    public int getId() {
        return id;
    }

    void setId(final int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(final String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    void setDone(final boolean done) {
        this.done = done;
    }
}
