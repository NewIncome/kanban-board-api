package com.jalfredev.TaskTrackerAPI.controller;

import com.jalfredev.TaskTrackerAPI.domain.TaskDto;
import com.jalfredev.TaskTrackerAPI.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public List<TaskDto> listTasks() throws IOException {
    return taskService.getTasks();
  }

  @PostMapping
  public void createTask(@RequestBody TaskDto taskDto) throws IOException {
    taskService.addTask(taskDto);
  }

  @DeleteMapping("/{task_id}")
  public void deleteTask(@PathVariable UUID taskId) {
    taskService.deleteTask(taskId);
  }
  
}
