package com.seuprojeto.taskmanager.service;

import com.seuprojeto.taskmanager.dto.TaskRequest;
import com.seuprojeto.taskmanager.dto.TaskResponse;
import com.seuprojeto.taskmanager.model.Task;
import com.seuprojeto.taskmanager.model.TaskPriority;
import com.seuprojeto.taskmanager.model.TaskStatus;
import com.seuprojeto.taskmanager.model.User;
import com.seuprojeto.taskmanager.repository.TaskRepository;
import com.seuprojeto.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // Converte Task para TaskResponse
    private TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .userId(task.getUser().getId())
                .userName(task.getUser().getName())
                .build();
    }

    // Busca usuário pelo email
    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
    }

    // CREATE — criar nova tarefa
    public TaskResponse create(TaskRequest request, String userEmail) {
        User user = getUser(userEmail);

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : TaskStatus.PENDING)
                .priority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM)
                .dueDate(request.getDueDate())
                .user(user)
                .build();

        return toResponse(taskRepository.save(task));
    }

    // READ — buscar todas as tarefas do usuário
    public Page<TaskResponse> findAll(String userEmail, Pageable pageable) {
        User user = getUser(userEmail);
        return taskRepository.findByUserId(user.getId(), pageable)
                .map(this::toResponse);
    }

    // READ — buscar tarefa por ID
    public TaskResponse findById(Long id, String userEmail) {
        User user = getUser(userEmail);
        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada!"));
        return toResponse(task);
    }

    // UPDATE — atualizar tarefa
    public TaskResponse update(Long id, TaskRequest request, String userEmail) {
        User user = getUser(userEmail);
        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada!"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        return toResponse(taskRepository.save(task));
    }

    // DELETE — deletar tarefa
    public void delete(Long id, String userEmail) {
        User user = getUser(userEmail);
        Task task = taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada!"));
        taskRepository.delete(task);
    }

    // FILTER — buscar por status
    public Page<TaskResponse> findByStatus(String userEmail, TaskStatus status, Pageable pageable) {
        User user = getUser(userEmail);
        return taskRepository.findByUserIdAndStatus(user.getId(), status, pageable)
                .map(this::toResponse);
    }

    // SEARCH — buscar por título
    public Page<TaskResponse> search(String userEmail, String title, Pageable pageable) {
        User user = getUser(userEmail);
        return taskRepository.findByUserIdAndTitleContainingIgnoreCase(user.getId(), title, pageable)
                .map(this::toResponse);
    }
}
