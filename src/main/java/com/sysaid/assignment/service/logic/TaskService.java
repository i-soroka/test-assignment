package com.sysaid.assignment.service.logic;

import com.sysaid.assignment.domain.task.db.Task;
import com.sysaid.assignment.domain.task.db.TaskRepository;
import com.sysaid.assignment.domain.task.dto.TaskResponse;
import com.sysaid.assignment.domain.task.dto.TaskType;
import com.sysaid.assignment.domain.task.mapper.TaskMapper;
import com.sysaid.assignment.domain.user.db.User;
import com.sysaid.assignment.domain.user.db.UserRepository;
import com.sysaid.assignment.service.BoredApiClient;
import com.sysaid.assignment.service.IBoredApiClient;
import com.sysaid.assignment.service.ITaskService;
import com.sysaid.assignment.util.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.sysaid.assignment.util.Utils.distinctByKey;

@Service
@AllArgsConstructor
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final IBoredApiClient api;
    private static final Integer COUNT_OF_RANDOM_TASKS_OF_GIVEN_TYPE_THAT_USER_CAN_FETCH = 10;
    private static final String USER_NOT_FOUND_TEMPLATE = "User %s is not found.";
    private static final String USER_NOT_AVAILABLE_FOR_TASK_TYPE_TEMPLATE =
            "User %s has exceeded limit of consuming tasks of type %s.";
    private static final String TASK_NOT_FOUND_TEMPLATE = "Task with id %d is not found.";

    @Override
    public TaskResponse getRandomTask(TaskType type, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(String.format(USER_NOT_FOUND_TEMPLATE, username)));
        if (userAbleToConsumeMoreTasks(type, username)) {
            TaskResponse randomTask = api.getRandomTaskByType(TaskType.of(type));
            taskRepository.save(
                    TaskMapper.map(randomTask, user)
            );
            return randomTask;
        }
        throw new RuntimeException(String.format(USER_NOT_AVAILABLE_FOR_TASK_TYPE_TEMPLATE, username, TaskType.of(type)));
    }

    @Override
    public Optional<TaskResponse> getRatedTask(String username) {
        TaskResponse ratedTask = null;
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(String.format(USER_NOT_FOUND_TEMPLATE, username)));
        // We can split tasks to following category:
        // - tasks with highest rating
        // - tasks with 2nd highest rating
        // - tasks with 3rd highest rating
        // - tasks with 4th highest rating
        // - tasks with 5th highest rating
        // - other tasks

        // Step 1. Find how many group of rating do we have (select distinct by rating)
        List<Task> tasks = taskRepository.findAllByUser(user).stream()
                .filter(distinctByKey(Task::getRating))
                .collect(Collectors.toList());
        if (tasks.size() == 0) {
            return Optional.empty();
        }
        Random random = new Random();
        int randomNumber = random.nextInt(100);

        if (randomNumber < 20) {
            // 20% chance to return the highest rating task
            ratedTask = Utils.getRandomFromGivenList(taskRepository.getTaskByRatingAndUser(tasks.get(0).getRating(), user), random);
        } else if (randomNumber < 40 && tasks.size() >= 2) {
            // 20% chance to return the 2nd highest rating task
            ratedTask = Utils.getRandomFromGivenList(taskRepository.getTaskByRatingAndUser(tasks.get(1).getRating(), user), random);
        } else if (randomNumber < 50 && tasks.size() >= 3) {
            // 10% chance to return the 3rd highest rating task
            ratedTask = Utils.getRandomFromGivenList(taskRepository.getTaskByRatingAndUser(tasks.get(2).getRating(), user), random);
        } else if (randomNumber < 55 && tasks.size() >= 4) {
            // 5% chance to return the 4th highest rating task
            ratedTask = Utils.getRandomFromGivenList(taskRepository.getTaskByRatingAndUser(tasks.get(3).getRating(), user), random);
        } else if (randomNumber < 60 && tasks.size() >= 5) {
            // 5% chance to return the 5th highest rating task
            ratedTask = Utils.getRandomFromGivenList(taskRepository.getTaskByRatingAndUser(tasks.get(3).getRating(), user), random);
        } else {
            // TODO: Check index out of bound
            List<Task> allTasks = taskRepository.findAllByUser(user);
            // 40% chance to return another random task
            int randomIndex = random.nextInt(allTasks.size() - 1);
            ratedTask = TaskMapper.map(tasks.get(randomIndex));
        }
        return Optional.of(ratedTask);
    }

    @Override
    public List<TaskResponse> getTasksByStateOfComplete(String username, TaskType type, Boolean completed) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(String.format(USER_NOT_FOUND_TEMPLATE, username)));
        List<Task> tasks = type == null ? taskRepository.findByCompletedAndUser(completed, user) :
                                          taskRepository.findByCompletedAndUserAndType(TaskType.of(type), completed, user);
        return tasks.stream()
                .map(TaskMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponse> getWishlist(String username) {
        return taskRepository.findByInWishlistAndUser(
                        true,
                        userRepository.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException(String.format(USER_NOT_FOUND_TEMPLATE, username)))
                ).stream()
                .map(TaskMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public boolean userAbleToConsumeMoreTasks(TaskType type, String username) {
        return taskRepository.countByTypeAndUser(
                TaskType.of(type),
                userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException(String.format(USER_NOT_FOUND_TEMPLATE, username)))
        ) < COUNT_OF_RANDOM_TASKS_OF_GIVEN_TYPE_THAT_USER_CAN_FETCH;
    }

    @Override
    public void completeTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException(String.format(TASK_NOT_FOUND_TEMPLATE, taskId)));
        task.setCompleted(true);
        task.setRating(task.getRating() + 2);
        taskRepository.save(task);
    }

    @Override
    public void putToWishlist(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException(String.format(TASK_NOT_FOUND_TEMPLATE, taskId)));
        task.setInWishlist(true);
        task.setRating(task.getRating() + 1);
        taskRepository.save(task);
    }

    @Override
    public List<TaskResponse> getAllTasks(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(String.format(USER_NOT_FOUND_TEMPLATE, username)));
        return taskRepository.findAllByUser(user).stream()
                .map(TaskMapper::map)
                .collect(Collectors.toList());
    }
}
