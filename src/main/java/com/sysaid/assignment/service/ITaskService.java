package com.sysaid.assignment.service;

import com.sysaid.assignment.domain.task.dto.TaskResponse;
import com.sysaid.assignment.domain.task.dto.TaskType;
import java.util.List;
import java.util.Optional;

public interface ITaskService {
    TaskResponse getRandomTask(TaskType type, String username);
    Optional<TaskResponse> getRatedTask(String username);
    List<TaskResponse> getTasksByStateOfComplete(String username, TaskType type, Boolean completed);
    List<TaskResponse> getWishlist(String username);
    boolean userAbleToConsumeMoreTasks(TaskType taskType, String username);
    void completeTask(Long taskId);
    void putToWishlist(Long taskId);
    List<TaskResponse> getAllTasks(String username);
}
