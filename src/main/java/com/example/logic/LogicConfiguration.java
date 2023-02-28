package com.example.logic;

import com.example.TaskConfiguration;
import com.example.model.ProjectRepository;
import com.example.model.TaskGroupRepository;
import com.example.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LogicConfiguration {
    @Bean
    ProjectService projectService(
        final ProjectRepository repository,
        final TaskGroupRepository taskGroupRepository,
        final TaskConfiguration config
    ) {
        return new ProjectService(repository, taskGroupRepository,config);
    }

    @Bean
    TaskGroupService taskGroupService (
        final TaskGroupRepository repository,
        final TaskRepository taskRepository
    ) {
        return new TaskGroupService(repository, taskRepository);
    }
}
