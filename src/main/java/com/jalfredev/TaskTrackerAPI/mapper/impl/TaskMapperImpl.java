package com.jalfredev.TaskTrackerAPI.mapper.impl;

import com.jalfredev.TaskTrackerAPI.domain.Column;
import com.jalfredev.TaskTrackerAPI.domain.TaskDto;
import com.jalfredev.TaskTrackerAPI.mapper.TaskMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TaskMapperImpl implements TaskMapper {

  @Override
  public TaskDto toDto(String taskRow) {
    String[] fields = taskRow.split(", ");

    return new TaskDto(
        UUID.fromString(fields[0]),
        fields[1],
        Column.valueOf(fields[2])
    );
  }

  @Override
  public String fromDto(TaskDto taskDto) {
    return String.format(
        "%s, %s, %s",
        taskDto.id(),
        taskDto.content(),
        taskDto.column()
    );
  }

}
