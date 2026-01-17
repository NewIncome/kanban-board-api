package com.jalfredev.TaskTrackerAPI.domain;

import java.util.UUID;

public record TaskDto(
    UUID id,
    String content,
    Column column
) { }
