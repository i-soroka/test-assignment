package com.sysaid.assignment.controller;

import com.sysaid.assignment.domain.task.dto.TaskResponse;
import com.sysaid.assignment.domain.task.dto.TaskType;
import com.sysaid.assignment.service.BoredApiClient;
import com.sysaid.assignment.service.logic.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * the controller is a basic structure and save some time on "dirty" work.
 */

@RestController
@RequestMapping("/tasks")
public class TaskController {

	private static final String TASK_TYPE_IS_NOT_SUPPORTED_PATTERN = "Task type %s is not supported.";
	private final TaskService taskService;
	private final BoredApiClient client;

	/**
	 * constructor for dependency injection
	 * @param taskService
	 */
	public TaskController(TaskService taskService, BoredApiClient client) {
		this.taskService = taskService;
		this.client = client;
	}

	/**
	 * will return uncompleted tasks for given user
	 * @param username the user which the tasks relevant for
	 * @param type type of the task
	 * @return list uncompleted tasks for the user
	 */
	@GetMapping("/uncompleted/{username}")
	public ResponseEntity<List<TaskResponse>> getUncomplitedTasks(
			@PathVariable ("username") String username,
			@RequestParam(name = "type",required = false) String type
	){
		return ResponseEntity.ok(taskService.getTasksByStateOfComplete(
				username,
				TaskType.of(type).orElseThrow(() -> new RuntimeException(String.format(TASK_TYPE_IS_NOT_SUPPORTED_PATTERN, type))),
				false
			)
		);
	}

	/**
	 * example for simple API use
	 * @return random task of the day
	 */
	@GetMapping("/of-the-day")
	public  ResponseEntity<TaskResponse> getTaskOfTheDay(@RequestParam String taskType,
														 @RequestParam String username
	){
		TaskType type = TaskType.of(taskType)
				.orElseThrow(() -> new RuntimeException(String.format(TASK_TYPE_IS_NOT_SUPPORTED_PATTERN, taskType)));
		return ResponseEntity.ok(
				taskService.getRandomTask(type, username)
		);
	}

	@GetMapping("/all")
	public ResponseEntity<List<TaskResponse>> getAll(
			@RequestParam String username
	) {
		return ResponseEntity.ok(taskService.getAllTasks(username));
	}

	@GetMapping("/wishlist")
	public ResponseEntity<List<TaskResponse>> getWishlist(
			@RequestParam String username
	) {
		return ResponseEntity.ok(taskService.getWishlist(username));
	}

	@PutMapping("/wishlist")
	public ResponseEntity<String> toWishlist(
			@RequestParam Long taskId
	) {
		taskService.putToWishlist(taskId);
		return ResponseEntity.ok("Task " + taskId + " was added to wishlist.");
	}

	@PutMapping("/complete")
	public ResponseEntity<String> complete(
			@RequestParam Long taskId
	) {
		taskService.completeTask(taskId);
		return ResponseEntity.ok("Task " + taskId + " has been completed.");
	}

	@GetMapping("/completed")
	public ResponseEntity<List<TaskResponse>> complete(
			@RequestParam String username
	) {
		return ResponseEntity.ok(taskService.getTasksByStateOfComplete(username, null, true));
	}

	@GetMapping("/rated")
	public ResponseEntity<TaskResponse> rated(
			@RequestParam String username
	) {
		return ResponseEntity.of(taskService.getRatedTask(username));
	}
}

