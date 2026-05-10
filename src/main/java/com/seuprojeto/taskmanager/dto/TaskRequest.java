package com.seuprojeto.taskmanager.dto;

import com.seuprojeto.taskmanager.model.TaskPriority;
import com.seuprojeto.taskmanager.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequest {

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 2, max = 200)
    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private LocalDateTime dueDate;
}
