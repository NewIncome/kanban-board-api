package com.jalfredev.TaskTrackerAPI.controller;

import com.jalfredev.TaskTrackerAPI.domain.Column;
import com.jalfredev.TaskTrackerAPI.domain.TaskDto;
import com.jalfredev.TaskTrackerAPI.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.Path;


@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTests {

  @TempDir
  static Path tempDir;

  @DynamicPropertySource
  static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("app.data.dir", ()->tempDir.toString());
  }

  private final MockMvc mockMvc;

  private final TaskService taskService;

  private final ObjectMapper objectMapper;

  @Autowired
  public TaskControllerIntegrationTests(
      MockMvc mockMvc, TaskService taskService, ObjectMapper objectMapper) {
    this.mockMvc = mockMvc;
    this.taskService = taskService;
    this.objectMapper = objectMapper;
  }


  @Test
  public void testThatCreateTaskCorrectlyCreatesATaskWithStatusCode201() throws Exception {
    TaskDto taskDto = new TaskDto(null,"My first Task", Column.TO_DO);
    String jsonTaskDto = objectMapper.writeValueAsString(taskDto);

    mockMvc.perform(
        MockMvcRequestBuilders
            .post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonTaskDto)
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.id").isString()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.id").isNotEmpty()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.content").value("My first Task")
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.column").value("TO_DO")
    );
  }

}
