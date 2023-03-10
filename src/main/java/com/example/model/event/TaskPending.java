package com.example.model.event;

import com.example.model.Task;

import java.time.Clock;

public class TaskPending extends TaskEvent {
    TaskPending(final Task source) {
        super(source.getId(), Clock.systemDefaultZone());
    }
}
