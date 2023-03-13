package com.example.controller;

import com.example.model.Task;
import com.example.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {
    @LocalServerPort //injecting random server port
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    TaskRepository taskRepository;

    @Test
    void httpGet_returnsAllTasks () {
        //given
        int initial = taskRepository.findAll().size();
        taskRepository.save(new Task("test task 1", LocalDateTime.now()));
        taskRepository.save(new Task("test task 2", LocalDateTime.now()));

        //when
        Task[] result = testRestTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);

        //then
        assertThat(result).hasSize(initial + 2);
    }

    @Test
    void httpGet_returnsTaskById() {
        //given
        String description = "test task";
        int id = taskRepository.save(new Task(description, LocalDateTime.now())).getId();

        //when
        Task result = testRestTemplate.getForObject("http://localhost:" + port + "/tasks/" + id, Task.class);

        //then
        assertThat(result).hasFieldOrPropertyWithValue("id", id);
        assertThat(result).hasFieldOrPropertyWithValue("description", description);
    }

    @Test
    void httpPost_createsNewTask() {
        //given
        Task task = new Task("test task", LocalDateTime.now());
        HttpEntity<Task> request = new HttpEntity<>(task);

        //when
        ResponseEntity<Task> result = testRestTemplate.postForEntity("http://localhost:" + port + "/tasks", request, Task.class);

        //then
        assertNotEquals(result.getBody().getId(), 0);
        assertEquals(task.getDescription(), result.getBody().getDescription());
    }

}