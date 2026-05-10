package com.seuprojeto.taskmanager.controller;

import com.seuprojeto.taskmanager.dto.TaskRequest;
import com.seuprojeto.taskmanager.dto.TaskResponse;
import com.seuprojeto.taskmanager.model.TaskStatus;
import com.seuprojeto.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // POST /api/tasks — criar tarefa
    @PostMapping
    public ResponseEntity<TaskResponse> create(
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        TaskResponse response = taskService.create(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/tasks — listar todas as tarefas (com paginação)
    @GetMapping
    public ResponseEntity<Page<TaskResponse>> findAll(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        return ResponseEntity.ok(taskService.findAll(userDetails.getUsername(), pageable));
    }

    // GET /api/tasks/{id} — buscar tarefa por ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(taskService.findById(id, userDetails.getUsername()));
    }

    // PUT /api/tasks/{id} — atualizar tarefa
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(taskService.update(id, request, userDetails.getUsername()));
    }

    // DELETE /api/tasks/{id} — deletar tarefa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        taskService.delete(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    // GET /api/tasks/status/{status} — filtrar por status
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<TaskResponse>> findByStatus(
            @PathVariable TaskStatus status,
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(taskService.findByStatus(userDetails.getUsername(), status, pageable));
    }

    // GET /api/tasks/search?title=xxx — buscar por título
    @GetMapping("/search")
    public ResponseEntity<Page<TaskResponse>> search(
            @RequestParam String title,
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(taskService.search(userDetails.getUsername(), title, pageable));
    }
}