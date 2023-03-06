package com.example.controller;

import com.example.model.Task;
import com.example.model.TaskRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
class TaskController {
    public static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }

    @GetMapping(params = {"!sort","!page","!size"})
    ResponseEntity<List<Task>> readAllTasks(){
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping
    ResponseEntity<List<Task>> readAllTasks(Pageable page){
        logger.info("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping("/{id}")
    ResponseEntity<Task> getTaskByID(@PathVariable int id) {
        return repository.findById(id)
                .map(task -> ResponseEntity.ok(task))
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    @GetMapping("/search/done")
    ResponseEntity<List<Task>> readDoneTasks(@RequestParam(defaultValue = "true") boolean state) {
        return ResponseEntity.ok(
                repository.findByDone(state)
        );
    }

    @PostMapping
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate){
        Task createdTask = repository.save(toCreate);
        logger.info("New task created with id: " + createdTask.getId());
        return ResponseEntity.created(URI.create("/" + createdTask.getId())).body(createdTask);
    }

    @Transactional
    @PutMapping("/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {
        if(!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom(toUpdate);
//                    repository.save(task);   //when using @Transactional you don't need this line
                });
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id) {
        if(!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/undefined")
    ResponseEntity<List<Task>> readUndefinedTasks() {
        return ResponseEntity.ok(
                repository.findByDeadlineIsNull()
        );
    }

    @GetMapping("/search/overdue")
    ResponseEntity<List<Task>> readOverdueTasks() {
        return ResponseEntity.ok(
                repository.findByDeadlineBefore(LocalDateTime.now())
        );
    }

    @GetMapping("/search/pending")
    ResponseEntity<List<Task>> readPendingTasks() {
        return ResponseEntity.ok(
                repository.findByDeadlineAfter(LocalDateTime.now())
        );
    }


}
