package com.jonah.portfolio.tasks;

import java.util.UUID;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(UUID id) {
        super("Task %s not found".formatted(id));
    }
}
