package com.sysaid.assignment.service;

import com.sysaid.assignment.domain.task.dto.TaskResponse;

public interface IBoredApiClient {
    TaskResponse getRandomTaskByType(String type);
}
