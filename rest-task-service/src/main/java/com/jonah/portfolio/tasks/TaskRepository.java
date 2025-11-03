package com.jonah.portfolio.tasks;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class TaskRepository {

    private final Map<UUID, Task> tasks = new ConcurrentHashMap<>();

    public Collection<Task> findAll() {
        return tasks.values();
    }

    public Optional<Task> findById(UUID id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public Task save(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    public Task create(String title, String description, boolean completed) {
        Instant now = Instant.now();
        Task task = new Task(UUID.randomUUID(), title, description, completed, now, now);
        tasks.put(task.getId(), task);
        return task;
    }

    public void delete(UUID id) {
        tasks.remove(id);
    }
}
