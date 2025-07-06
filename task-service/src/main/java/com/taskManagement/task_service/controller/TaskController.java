package com.taskManagement.task_service.controller;

import com.taskManagement.task_service.dto.CreateTaskRequest;
import com.taskManagement.task_service.dto.PageResponse;
import com.taskManagement.task_service.dto.UpdateTaskRequest;
import com.taskManagement.task_service.entity.Task;
import com.taskManagement.task_service.interfaces.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/all")
    public ResponseEntity<List<Task>> getAllTasksWithoutPagination() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        try {
            PageResponse<Task> response = taskService.getAllTasks(page, size, sortBy, sortDir);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create-task")
    public ResponseEntity<?> createTask(@RequestBody CreateTaskRequest createTaskRequest) {
        Task task = taskService.createTask(createTaskRequest);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.fetchTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTaskById(@PathVariable Long id, @RequestBody UpdateTaskRequest updateTaskRequest) {
        Task task = taskService.updateTaskById(id, updateTaskRequest);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTaskById(@PathVariable Long id) {
        Task task = taskService.deleteTaskById(id);
        return ResponseEntity.ok("Deleted this task: " + task);
    }
}
