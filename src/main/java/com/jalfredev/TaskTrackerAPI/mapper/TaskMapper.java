package com.jalfredev.TaskTrackerAPI.mapper;

import com.jalfredev.TaskTrackerAPI.domain.TaskDto;

public interface TaskMapper {

  TaskDto toDto(String taskRow);

  String fromDto(TaskDto taskDto);

}
