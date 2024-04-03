package com.sysaid.assignment.util;

import com.sysaid.assignment.domain.task.db.Task;
import com.sysaid.assignment.domain.task.dto.TaskResponse;
import com.sysaid.assignment.domain.task.mapper.TaskMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;

public class Utils {

    // Could be generic
    public static TaskResponse getRandomFromGivenList(List<Task> tasks, Random random) {
        return TaskMapper.map(
                tasks.get(random.nextInt(tasks.size()))
        );
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new HashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
