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
    assertTrue(Files.exists(tempDir));
    assertTrue(Files.isDirectory(tempDir));
    assertTrue(Files.exists(tempDir.resolve("tasks.csv")));
    assertFalse(Files.isDirectory(tempDir.resolve("tasks.csv")));
    assertTrue(Files.isRegularFile(tempDir.resolve("tasks.csv")));

    assertEquals(
        tempDir + "/tasks.csv",
        tempDir.resolve("tasks.csv").toString()
    );

    assertThat(Files.readAllLines(tempDir.resolve("tasks.csv")))
        .contains ("id,content,column");
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

    List<String> lines = Files.readAllLines(csvFile);
    assertEquals("id,content,column", lines.get(0));
    //assertEquals(newTaskDto.id().toString(), lines.get(1).split(",")[0]);
    assertThat(lines.get(1)).contains(String.format("%s, %s, %s",
        newTaskDto.id().toString(),
        "This is a new Task",
        "TO_DO" )
    );
  }

  @Test
  public void testThatTaskCsvRepoCorrectlyListsAllTasks() throws IOException {
    //Create 2 tasks
    TaskDto taskDto1 = new TaskDto(UUID.randomUUID(), "1st Task", Column.TO_DO);
    TaskDto taskDto2 = new TaskDto(UUID.randomUUID(), "2nd Task", Column.IN_PROGRESS);
    taskCsvRepository.save(taskDto1);
    taskCsvRepository.save(taskDto2);

    List<String> lines = Files.readAllLines(tempDir.resolve("tasks.csv"));
    assertThat(lines).hasSize(3);
    assertThat(lines.get(1)).contains("1st Task");
    assertThat(lines.get(2)).contains("2nd Task");
  }

  @Test
  public void testThatTaskCsvRepoCorrectlyDeletesATaskRow() throws IOException {
    //Create a Task, assert it's there, then delete it
    TaskDto taskDto = new TaskDto(UUID.randomUUID(), "Task numero 1", Column.TO_DO);
    taskCsvRepository.save(taskDto);

    List<String> lines = Files.readAllLines(tempDir.resolve("tasks.csv"));
    assertThat(lines).hasSize(2);
    assertThat(lines.get(1)).isEqualTo(taskDto.id().toString() + ", Task numero 1, TO_DO");

    taskCsvRepository.delete(taskDto.id());
    lines = Files.readAllLines(tempDir.resolve("tasks.csv"));
    assertThat(lines).hasSize(1);
  }

}
