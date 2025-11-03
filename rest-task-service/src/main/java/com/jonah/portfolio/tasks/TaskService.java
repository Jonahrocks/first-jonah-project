package com.jonah.portfolio.tasks;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> listTasks() {
        return repository.findAll().stream()
                .sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
                .toList();
    }

    public Task getTask(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task createTask(TaskRequest request) {
        return repository.create(request.title(), request.description(), request.completed());
    }

    @Transactional
    public Task updateTask(UUID id, TaskRequest request) {
        Task task = getTask(id);
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setCompleted(request.completed());
        task.setUpdatedAt(Instant.now());
        repository.save(task);
        return task;
    }

    public void deleteTask(UUID id) {
        if (repository.findById(id).isEmpty()) {
            throw new TaskNotFoundException(id);
        }
        repository.delete(id);
    }
}
