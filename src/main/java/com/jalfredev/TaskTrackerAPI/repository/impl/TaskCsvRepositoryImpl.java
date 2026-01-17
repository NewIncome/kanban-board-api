package com.jalfredev.TaskTrackerAPI.repository.impl;

import com.jalfredev.TaskTrackerAPI.domain.TaskDto;
import com.jalfredev.TaskTrackerAPI.mapper.TaskMapper;
import com.jalfredev.TaskTrackerAPI.repository.TaskCsvRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class TaskCsvRepositoryImpl implements TaskCsvRepository {

  private static final Path FILE_PATH = Paths.get("TodoBoard/data/tasks.csv");
  private static final String HEADER = "ID,CONTENT,COLUMNS";

  private final TaskMapper taskMapper;

  public TaskCsvRepositoryImpl(TaskMapper taskMapper) throws IOException {
    this.taskMapper = taskMapper;
    initializeFile();
  }


  @Override
  public List<TaskDto> findAll() throws IOException {
    try (Stream<String> lines = Files.lines(FILE_PATH)) {
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
        FILE_PATH,
        row.getBytes(),
        StandardOpenOption.APPEND
    );
  }


  private void initializeFile() throws IOException {
    // Ensure parent directory exists
    if (Files.notExists(FILE_PATH.getParent())) Files.createDirectories(FILE_PATH.getParent());

    // Create file if missing
    if (Files.notExists(FILE_PATH)) {
      Files.createFile(FILE_PATH);
      Files.write(FILE_PATH, (HEADER + System.lineSeparator()).getBytes());
    }
  }

}
