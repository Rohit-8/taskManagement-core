package com.taskManagement.task_service.service;

import com.taskManagement.task_service.dto.CreateTaskRequest;
import com.taskManagement.task_service.dto.PageResponse;
import com.taskManagement.task_service.dto.UpdateTaskRequest;
import com.taskManagement.task_service.entity.Task;
import com.taskManagement.task_service.interfaces.TaskService;
import com.taskManagement.task_service.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private static final List<String> allowedSortFields = Arrays.asList(
            "title", "description", "assignedBy", "assignedTo", "createdAt", "priority"
    );

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public PageResponse<Task> getAllTasks(int page, int size, String sortBy, String sortDir) {
        if (!allowedSortFields.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sortBy field: " + sortBy);
        }
        Pageable pageable;
        Page<Task> taskPage;

        if ("priority".equalsIgnoreCase(sortBy)) {
            pageable = PageRequest.of(page, size);
            if ("desc".equalsIgnoreCase(sortDir)) {
                taskPage = taskRepository.findAllOrderByPriorityDesc(pageable);
            } else {
                taskPage = taskRepository.findAllOrderByPriorityAsc(pageable);
            }
        } else {
            Sort sort = sortDir.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            pageable = PageRequest.of(page, size, sort);
            taskPage = taskRepository.findAll(pageable);
        }

        return new PageResponse<>(
                taskPage.getContent(),
                taskPage.getNumber(),
                taskPage.getSize(),
                taskPage.getTotalElements(),
                taskPage.getTotalPages(),
                taskPage.isLast()
            );
    }


    @Override
    public Optional<Task> fetchTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task createTask(CreateTaskRequest createTaskRequest) {
        Task request = Task.builder()
                .title(createTaskRequest.getTitle())
                .description(createTaskRequest.getDescription())
                .createdBy(createTaskRequest.getCreatedBy())
                .assignedTo(createTaskRequest.getAssignedTo())
                .assignedBy(createTaskRequest.getAssignedBy())
                .createdAt(LocalDateTime.now())
                .priority(createTaskRequest.getPriority())
                .build();
        return taskRepository.save(request);
    }

    @Override
    public Task updateTaskById(Long id, UpdateTaskRequest updateTaskRequest) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        task.setTitle(updateTaskRequest.getTitle());
        task.setDescription(updateTaskRequest.getDescription());
        task.setAssignedBy(updateTaskRequest.getAssignedBy());
        task.setAssignedTo(updateTaskRequest.getAssignedTo());
        task.setPriority(updateTaskRequest.getPriority());
        return taskRepository.save(task);
    }

    @Override
    public Task deleteTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        taskRepository.deleteById(id);
        return task;
    }
}
