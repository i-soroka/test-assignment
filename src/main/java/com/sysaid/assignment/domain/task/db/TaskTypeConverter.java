package com.sysaid.assignment.domain.task.db;

import com.sysaid.assignment.domain.task.dto.TaskType;

import javax.persistence.AttributeConverter;

public class TaskTypeConverter implements AttributeConverter<TaskType, String> {

    @Override
    public String convertToDatabaseColumn(TaskType type) {
        return type.name();
    }

    @Override
    public TaskType convertToEntityAttribute(String s) {
        return TaskType.of(s).get();
    }
}
