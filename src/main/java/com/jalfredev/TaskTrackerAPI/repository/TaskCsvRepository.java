package com.jalfredev.TaskTrackerAPI.repository;

import com.jalfredev.TaskTrackerAPI.domain.TaskDto;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface TaskCsvRepository {

  List<TaskDto> findAll() throws IOException;

  boolean save(TaskDto taskDto) throws IOException;

  void delete(UUID taskId) throws IOException;

}
