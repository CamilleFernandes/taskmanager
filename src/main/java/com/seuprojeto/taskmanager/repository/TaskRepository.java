package com.seuprojeto.taskmanager.repository;

import com.seuprojeto.taskmanager.model.Task;
import com.seuprojeto.taskmanager.model.TaskPriority;
import com.seuprojeto.taskmanager.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Busca todas as tarefas de um usuário (com paginação)
    Page<Task> findByUserId(Long userId, Pageable pageable);

    // Busca por status
    Page<Task> findByUserIdAndStatus(Long userId, TaskStatus status, Pageable pageable);

    // Busca por prioridade
    Page<Task> findByUserIdAndPriority(Long userId, TaskPriority priority, Pageable pageable);

    // Busca por título (contém o texto)
    Page<Task> findByUserIdAndTitleContainingIgnoreCase(Long userId, String title, Pageable pageable);

    // Busca uma tarefa específica do usuário
    Optional<Task> findByIdAndUserId(Long id, Long userId);

    // Conta tarefas por status
    long countByUserIdAndStatus(Long userId, TaskStatus status);
}
