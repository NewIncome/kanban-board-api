package com.jalfredev.TaskTrackerAPI.service;

import com.jalfredev.TaskTrackerAPI.domain.TaskDto;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface TaskService {

  List<TaskDto> getTasks() throws IOException;

  TaskDto addTask(TaskDto taskDto) throws IOException;

  void deleteTask(UUID taskId) throws IOException;

  TaskDto updateTask(UUID taskId, TaskDto taskDto) throws IOException;

}
