package com.example.controller;

import com.example.model.Task;
import com.example.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class TaskControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void httpGet_returnsTaskById () throws Exception {
        //given
        int id = taskRepository.save(new Task("test task 1", LocalDateTime.now())).getId();

        //when then
        mockMvc.perform(get("/tasks/" + id))
                .andExpect(status().is2xxSuccessful());
    }
}
