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
import java.util.UUID;
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
  public boolean save(TaskDto taskDto) throws IOException {
    String row = taskMapper.fromDto(taskDto);

    Files.write(
        FILE_PATH,
        row.getBytes(),
        StandardOpenOption.APPEND
    );

    return existsById(taskDto.id());
  }

  @Override
  public void delete(UUID taskId) throws IOException {
    List<String> lines = Files.readAllLines(FILE_PATH);

    int originalSize = lines.size();

    List<String> updatedLines = lines.stream()
          .filter(line -> !line.startsWith(taskId.toString() + ","))
          .toList();

    if(updatedLines.size() != originalSize) {
      Files.write(
          FILE_PATH,
          updatedLines,
          StandardOpenOption.TRUNCATE_EXISTING
      );
    }
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

  //to confirm task(row) existence and prevents duplicates
  private boolean existsById(UUID id) throws IOException {
    try (Stream<String> lines = Files.lines(FILE_PATH)) {
    return lines
        .skip(1)
        .anyMatch(line -> line.startsWith(id.toString() + ","));
    }
  }

}
