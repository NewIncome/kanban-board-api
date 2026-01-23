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
    ).andExpect(
        MockMvcResultMatchers.status().isCreated()
    );
  }

  @Test
  public void testThatListAllTasksCorrectlyListsAllSavedTasksWithStatusCode200() throws Exception {
    TaskDto taskDto1 = new TaskDto(null,"My first Task", Column.DONE);
    TaskDto taskDto2 = new TaskDto(null,"My second Task", Column.TO_DO);
    taskService.addTask(taskDto1);
    taskService.addTask(taskDto2);

    mockMvc.perform(
        MockMvcRequestBuilders
            .get("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$[0].content").value("My first Task")
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$[0].column").value("DONE")
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$[1].content").value("My second Task")
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$[1].column").value("TO_DO")
    ).andExpect(
        MockMvcResultMatchers.status().isOk()
    );
  }

  @Test
  public void testThatDeleteTaskCorrectlyRemovesATaskRow() throws Exception {
    TaskDto taskDto = new TaskDto(null,"My second Task", Column.TO_DO);
    TaskDto createdTaskDto = taskService.addTask(taskDto);

    mockMvc.perform(
        MockMvcRequestBuilders
            .delete("/api/tasks/" + createdTaskDto.id())
            .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(
        MockMvcResultMatchers.status().isNoContent()
    );
  }

  @Test
  public void testThatPartialUpdateTaskCorrectlyUpdatesATask() throws Exception {
    TaskDto taskDto = new TaskDto(null, "My last task", Column.IN_PROGRESS);
    TaskDto createdTask = taskService.addTask(taskDto);

    TaskDto modifiedTask = new TaskDto(createdTask.id(), "My lasto task", Column.DONE);
    String modifiedTaskJson = objectMapper.writeValueAsString(modifiedTask);

    mockMvc.perform(
        MockMvcRequestBuilders
              .patch("/api/tasks/" + createdTask.id())
              .contentType(MediaType.APPLICATION_JSON)
              .content(modifiedTaskJson)
    ).andExpect(
        MockMvcResultMatchers.status().isOk()
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.id").value(createdTask.id().toString())
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.content").value(modifiedTask.content())
    ).andExpect(
        MockMvcResultMatchers.jsonPath("$.column").value(modifiedTask.column().toString())
    );
  }

}
