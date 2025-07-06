package com.taskManagement.task_service.dto;

import com.taskManagement.task_service.enums.Category;
import com.taskManagement.task_service.enums.Priority;
import lombok.Data;

@Data
public class CreateTaskRequest {

    private String title;
    private String description;
    private String assignedBy;
    private String assignedTo;
    private String createdBy;
    private Priority priority;
    private Category category;
}
