package com.jalfredev.TaskTrackerAPI.service.impl;

import com.jalfredev.TaskTrackerAPI.domain.TaskDto;
import com.jalfredev.TaskTrackerAPI.repository.TaskCsvRepository;
import com.jalfredev.TaskTrackerAPI.repository.impl.TaskCsvRepositoryImpl;
import com.jalfredev.TaskTrackerAPI.service.TaskService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

  private final TaskCsvRepository taskCsvRepository;

  public TaskServiceImpl(TaskCsvRepository taskCsvRepository) {
    this.taskCsvRepository = taskCsvRepository;

  }


  @Override
  public List<TaskDto> getTasks() throws IOException {
    return taskCsvRepository.findAll();
  }

  @Override
  public void addTask(TaskDto taskDto) throws IOException {
    taskCsvRepository.save(taskDto);
  }

  @Override
  public void deleteTask(UUID taskId) {

  }

}
