package com.example.controller;

import com.example.logic.ProjectService;
import com.example.model.ProjectStep;
import com.example.model.projection.ProjectWriteModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/projects")
class ProjectController {
    private final ProjectService service;

    ProjectController(final ProjectService service) {
        this.service = service;
    }

    @GetMapping
    String showProjects(Model model) {
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }

    @PostMapping
    String addProject(@ModelAttribute("project") ProjectWriteModel current, Model model) {
        service.create(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("message", "Projekt został dodany");
        return "projects";
    }

    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current) {
        current.getSteps().add(new ProjectStep());
        return "projects";
    }
}
