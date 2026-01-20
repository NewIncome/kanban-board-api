package com.jalfredev.TaskTrackerAPI.repository;

import com.jalfredev.TaskTrackerAPI.domain.Column;
import com.jalfredev.TaskTrackerAPI.domain.TaskDto;
import com.jalfredev.TaskTrackerAPI.mapper.TaskMapper;
import com.jalfredev.TaskTrackerAPI.mapper.impl.TaskMapperImpl;
import com.jalfredev.TaskTrackerAPI.repository.impl.TaskCsvRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


public class TaskCsvRepositoryImplIntegrationTests {

  @TempDir
  private Path tempDir;

  private TaskCsvRepository taskCsvRepository;

  @BeforeEach
  void setup() throws IOException {
    //Path csvFile = tempDir.resolve("tasks.csv");
    TaskMapper mapper = new TaskMapperImpl();

    taskCsvRepository = new TaskCsvRepositoryImpl(tempDir, mapper);
  }


  @Test
  public void testThatTaskCsvRepoCorrectlyCreatesABasePath() throws IOException {
    //Path csv = tempDir.resolve("tasks.csv");
    assertTrue(Files.exists(tempDir));
  }

  @Test
  public void testThatTaskCsvRepoCorrectlyCreatesATask() throws IOException {
    Path csvFile = tempDir.resolve("tasks.csv");

    TaskDto newTaskDto = new TaskDto(
        UUID.randomUUID(),
        "This is a new Task",
        Column.TO_DO
    );

    taskCsvRepository.save(newTaskDto);

    /*//Debug lines
    System.out.println("csvFile = " + csvFile);
    System.out.println("exists = " + Files.exists(csvFile));
    System.out.println("isDirectory = " + Files.isDirectory(csvFile));
    System.out.println("isRegularFile = " + Files.isRegularFile(csvFile));*/

    List<String> lines = Files.readAllLines(csvFile);
    assertEquals("id,content,column", lines.get(0));
    //assertEquals(newTaskDto.id().toString(), lines.get(1).split(",")[0]);
    assertThat(lines.get(1)).contains(String.format("%s, %s, %s",
        newTaskDto.id().toString(),
        "This is a new Task",
        Column.TO_DO.toString() )
    );
  }

}
