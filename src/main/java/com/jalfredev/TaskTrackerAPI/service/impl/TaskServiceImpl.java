package com.jalfredev.TaskTrackerAPI.service.impl;

import com.jalfredev.TaskTrackerAPI.domain.Column;
import com.jalfredev.TaskTrackerAPI.domain.TaskDto;
import com.jalfredev.TaskTrackerAPI.repository.TaskCsvRepository;
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
  public TaskDto addTask(TaskDto taskDto) throws IOException {
    Column column = taskDto.column() == null ? Column.TO_DO : taskDto.column();
    TaskDto completeTaskDto = new TaskDto(
        UUID.randomUUID(),
        taskDto.content(),
         column
    );
    boolean savedTask = taskCsvRepository.save(completeTaskDto);
    if(savedTask) return completeTaskDto;
    else throw new IllegalArgumentException("Task already exists!");
  }

  @Override
  public void deleteTask(UUID taskId) throws IOException {
    taskCsvRepository.delete(taskId);
  }

}
