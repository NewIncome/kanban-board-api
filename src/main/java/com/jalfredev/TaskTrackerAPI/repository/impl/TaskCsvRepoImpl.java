package com.jalfredev.TaskTrackerAPI.repository.impl;

import com.jalfredev.TaskTrackerAPI.domain.TaskDto;
import com.jalfredev.TaskTrackerAPI.mapper.TaskMapper;
import com.jalfredev.TaskTrackerAPI.repository.TaskCsvRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class TaskCsvRepoImpl implements TaskCsvRepository {

  private static final String FILE_PATH = "...";

  private final TaskMapper taskMapper;

  public TaskCsvRepoImpl(TaskMapper taskMapper) {
    this.taskMapper = taskMapper;
  }

  @Override
  public List<TaskDto> findAll() throws IOException {
    try (Stream<String> lines = Files.lines(Paths.get(FILE_PATH))) {
      return lines
              .skip(1)  //skip header
              .filter(line -> !line.isBlank())  //in case there's a space before
              .map(taskMapper::toDto)
              .collect(Collectors.toList());
    }
  }

  @Override
  public void save(TaskDto taskDto) throws IOException {
    String row = taskMapper.fromDto(taskDto);

    Files.write(
        Paths.get(FILE_PATH),
        row.getBytes(),
        StandardOpenOption.APPEND
    );
  }

}
