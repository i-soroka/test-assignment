package com.sysaid.assignment.domain.task.mapper;

import com.sysaid.assignment.domain.task.db.Task;
import com.sysaid.assignment.domain.task.dto.TaskResponse;
import com.sysaid.assignment.domain.user.db.User;

import java.time.LocalDateTime;

public interface TaskMapper {

    static Task map(TaskResponse task, User user) {
        return new Task(
                null,
                task.getActivity(),
                task.getAccessibility(),
                task.getType(),
                task.getParticipants(),
                task.getPrice(),
                task.getLink(),
                task.getKey(),
                LocalDateTime.now(),
                false,
                false,
                0,
                user
        );
    }

    static TaskResponse map(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getActivity(),
                task.getType(),
                task.getAmountOfParticipants(),
                task.getPrice(),
                task.getLink(),
                task.getKey(),
                task.getAccessibility()
        );
    }
}
