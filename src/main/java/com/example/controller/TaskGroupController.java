package com.example.controller;

import com.example.logic.TaskGroupService;
import com.example.model.Task;
import com.example.model.TaskRepository;
import com.example.model.projection.GroupReadModel;
import com.example.model.projection.GroupWriteModel;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/groups")
class TaskGroupController {

    public static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskGroupService taskGroupService;
    private final TaskRepository taskRepository;

    TaskGroupController(final TaskGroupService taskGroupService, final TaskRepository taskRepository) {
        this.taskGroupService = taskGroupService;
        this.taskRepository = taskRepository;
    }

    @GetMapping
    ResponseEntity<List<GroupReadModel>> readAllGroups(){
        logger.warn("Exposing all groups");
        return ResponseEntity.ok(taskGroupService.readAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
    }

    @PostMapping
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel toCreate) {
        logger.info("New group created");
        final GroupReadModel createdGroup = taskGroupService.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/" + createdGroup.getId())).body(createdGroup);
    }

    @PatchMapping("/{id}")
    ResponseEntity<?> toggleGroup(@PathVariable int id) {
        taskGroupService.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }
}
