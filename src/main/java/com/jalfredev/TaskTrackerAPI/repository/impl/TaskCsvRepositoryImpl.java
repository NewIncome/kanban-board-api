package com.jalfredev.TaskTrackerAPI.repository.impl;

import com.jalfredev.TaskTrackerAPI.domain.TaskDto;
import com.jalfredev.TaskTrackerAPI.mapper.TaskMapper;
import com.jalfredev.TaskTrackerAPI.repository.TaskCsvRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class TaskCsvRepositoryImpl implements TaskCsvRepository {

  private final Path filePath;
  private static final String HEADER = "ID,CONTENT,COLUMNS";

  private final TaskMapper taskMapper;

  public TaskCsvRepositoryImpl(
                @Value("${app.data.dir}") Path dataDir,
                TaskMapper taskMapper) throws IOException {
    this.filePath = dataDir.resolve("tasks.csv");
    this.taskMapper = taskMapper;
    initializeFile();
  }
  /* @Value,
   * Typically used for expression-driven or property-driven dependency injection.
   * Also supported for dynamic resolution of handler method arguments — for example,
   * in Spring MVC.
   * A common use case is to inject values using #{systemProperties.myProp} style
   * SpEL (Spring Expression Language) expressions. Alternatively, values may be
   * injected using ${my.app.myProp} style property placeholders.
   */


  @Override
  public List<TaskDto> findAll() throws IOException {
    try (Stream<String> lines = Files.lines(filePath)) {
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
        filePath,
        row.getBytes(),
        StandardOpenOption.APPEND
    );

    return existsById(taskDto.id());
  }

  @Override
  public void delete(UUID taskId) throws IOException {
    List<String> lines = Files.readAllLines(filePath);

    int originalSize = lines.size();

    List<String> updatedLines = lines.stream()
          .filter(line -> !line.startsWith(taskId.toString() + ","))
          .toList();

    if(updatedLines.size() != originalSize) {
      Files.write(
          filePath,
          updatedLines,
          StandardOpenOption.TRUNCATE_EXISTING
      );
    }
  }


  private void initializeFile() throws IOException {
    // 1️⃣ Create parent directories ONLY
    Path parentDir = filePath.getParent();
    if (parentDir != null) {
      Files.createDirectories(parentDir);
    }

    // 2️⃣ Create the file if it does not exist
    if (!Files.exists(filePath)) {
      Files.createFile(filePath);

      // 3️⃣ Write header
      Files.writeString(
          filePath,
          "id,content,column" + System.lineSeparator(),
          StandardOpenOption.WRITE
      );
    }
  }

  //to confirm task(row) existence and prevents duplicates
  private boolean existsById(UUID id) throws IOException {
    try (Stream<String> lines = Files.lines(filePath)) {
    return lines
        .skip(1)
        .anyMatch(line -> line.startsWith(id.toString() + ","));
    }
  }

}
