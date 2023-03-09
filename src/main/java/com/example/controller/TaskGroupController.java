package com.example.controller;

import com.example.logic.TaskGroupService;
import com.example.model.Task;
import com.example.model.TaskRepository;
import com.example.model.projection.GroupReadModel;
import com.example.model.projection.GroupWriteModel;
import com.example.model.projection.TaskWriteModel;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/groups")
class TaskGroupController {

    public static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);
    private final TaskGroupService taskGroupService;
    private final TaskRepository taskRepository;

    TaskGroupController(final TaskGroupService taskGroupService, final TaskRepository taskRepository) {
        this.taskGroupService = taskGroupService;
        this.taskRepository = taskRepository;
    }

    @ResponseBody
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<GroupReadModel>> readAllGroups(){
        logger.warn("Exposing all groups");
        return ResponseEntity.ok(taskGroupService.readAll());
    }

    @ResponseBody
    @GetMapping(value = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Task>> readAllTasksFromGroup(@PathVariable int id) {
        return ResponseEntity.ok(taskRepository.findAllByGroup_Id(id));
    }

    @ResponseBody
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<GroupReadModel> createGroup(@RequestBody @Valid GroupWriteModel toCreate) {
        logger.info("New group created");
        final GroupReadModel createdGroup = taskGroupService.createGroup(toCreate);
        return ResponseEntity.created(URI.create("/" + createdGroup.getId())).body(createdGroup);
    }

    @ResponseBody
    @PatchMapping("/{id}")
    ResponseEntity<?> toggleGroup(@PathVariable int id) {
        taskGroupService.toggleGroup(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    String showGroups(Model model) {
        model.addAttribute("group", new GroupWriteModel());
        return "groups";
    }

    @PostMapping(produces = MediaType.TEXT_HTML_VALUE, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String addGroup(
            @ModelAttribute("group") @Valid GroupWriteModel current,
            BindingResult bindingResult,
            Model model
    ) {
        if(bindingResult.hasErrors()) {
            return "groups";
        }
        taskGroupService.createGroup(current);
        model.addAttribute("group", new GroupWriteModel());
        model.addAttribute("groups", getGroups());
        model.addAttribute("message", "Grupa została dodana");
        return "groups";
    }

    @PostMapping(params = "addTask", produces = MediaType.TEXT_HTML_VALUE)
    String addGroupTask(@ModelAttribute("group") GroupWriteModel current) {
        current.getTasks().add(new TaskWriteModel());
        return "groups";
    }

    @ModelAttribute("groups")
    List<GroupReadModel> getGroups() {
        return taskGroupService.readAll();
    }

}
