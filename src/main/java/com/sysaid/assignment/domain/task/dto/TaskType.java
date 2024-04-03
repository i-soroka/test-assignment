package com.sysaid.assignment.domain.task.dto;

import lombok.AllArgsConstructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

@AllArgsConstructor
public enum TaskType {

    EDUCATION("education"),
    RECREATIONAL("recreational"),
    SOCIAL("social"),
    DIY("diy"),
    CHARITY("charity"),
    COOKING("cooking"),
    RELAXATION("relaxation"),
    MUSIC("music"),
    BUSYWORK("busywork"),
    ;

    private final String name;
    private final static Map<String, TaskType> STRING_TO_TYPE = new HashMap<>();

    static {
        Stream.of(TaskType.values()).forEach(
                typeEnum -> STRING_TO_TYPE.put(typeEnum.name, typeEnum)
        );
    }

    public static Optional<TaskType> of(String typeName) {
        return STRING_TO_TYPE.containsKey(typeName) ? Optional.of(STRING_TO_TYPE.get(typeName))
                                                    : Optional.empty();
    }

    public static String of(TaskType type) {
        return type.name;
    }

}
