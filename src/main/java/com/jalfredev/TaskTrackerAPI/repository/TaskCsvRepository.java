package com.jalfredev.TaskTrackerAPI.repository;

import com.jalfredev.TaskTrackerAPI.domain.TaskDto;

import java.io.IOException;
import java.util.List;

public interface TaskCsvRepository {

  List<TaskDto> findAll() throws IOException;

  void save(TaskDto taskDto) throws IOException;
  
}
