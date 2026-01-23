package com.jalfredev.TaskTrackerAPI.controller;

import com.jalfredev.TaskTrackerAPI.domain.TaskDto;
import com.jalfredev.TaskTrackerAPI.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


//@CrossOrigin(origins = "http://localhost:5173")
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
  public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) throws IOException {
    TaskDto createdTask = taskService.addTask(taskDto);
    return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
  }

  @DeleteMapping("/{task_id}")
  public ResponseEntity deleteTask(@PathVariable("task_id") UUID taskId) {
    try {
      taskService.deleteTask(taskId);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @PatchMapping("/{task_id}")
  public ResponseEntity<TaskDto> partialUpdateTask(@PathVariable("task_id") UUID taskId,
                                                   @RequestBody TaskDto taskDto) throws IOException {
    TaskDto editedTask = taskService.updateTask(taskId, taskDto);
    return new ResponseEntity(editedTask, HttpStatus.OK);
  }

}
