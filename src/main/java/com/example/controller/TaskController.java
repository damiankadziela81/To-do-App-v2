package com.example.controller;

import com.example.model.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RepositoryRestController
class TaskController {
    public static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }

    //@RequestMapping(method = RequestMethod.GET, path = "/tasks")
    //use this endpoint when there are no params, otherwise use the ones containing HATEOAS in RestRepository
    @GetMapping(value = "/tasks", params = {"!sort","!page","!size"})
    ResponseEntity<?> readAllTasks(){
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
    }
}
