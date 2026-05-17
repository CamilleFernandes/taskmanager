package com.seuprojeto.taskmanager.service;

import com.seuprojeto.taskmanager.dto.TaskRequest;
import com.seuprojeto.taskmanager.dto.TaskResponse;
import com.seuprojeto.taskmanager.model.Task;
import com.seuprojeto.taskmanager.model.TaskPriority;
import com.seuprojeto.taskmanager.model.TaskStatus;
import com.seuprojeto.taskmanager.model.User;
import com.seuprojeto.taskmanager.repository.TaskRepository;
import com.seuprojeto.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do TaskService")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Task task;
    private TaskRequest taskRequest;
    private final String USER_EMAIL = "camille@email.com";

    @BeforeEach
void setUp() {
    user = User.builder()
            .id(1L)
            .name("Camille Fernandes")
            .email(USER_EMAIL)
            .build();

    task = Task.builder()
            .id(1L)
            .title("Estudar Spring Boot")
            .description("Aprender testes com JUnit e Mockito")
            .status(TaskStatus.PENDING)
            .priority(TaskPriority.HIGH)
            .dueDate(LocalDateTime.now().plusDays(7))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .user(user)
            .build();

    taskRequest = new TaskRequest();
    taskRequest.setTitle("Estudar Spring Boot");
    taskRequest.setDescription("Aprender testes com JUnit e Mockito");
    taskRequest.setStatus(TaskStatus.PENDING);
    taskRequest.setPriority(TaskPriority.HIGH);
    taskRequest.setDueDate(LocalDateTime.now().plusDays(7));
}

    @Test
    @DisplayName("Deve criar uma tarefa com sucesso")
    void deveCriarTarefaComSucesso() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.create(taskRequest, USER_EMAIL);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Estudar Spring Boot");
        assertThat(response.getStatus()).isEqualTo(TaskStatus.PENDING);
        assertThat(response.getPriority()).isEqualTo(TaskPriority.HIGH);

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Deve lancar excecao ao criar tarefa com usuario inexistente")
    void deveLancarExcecaoAoCriarTarefaComUsuarioInexistente() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.create(taskRequest, USER_EMAIL))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuário não encontrado!");

        verify(taskRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar lista de tarefas do usuario")
    void deveRetornarListaDeTarefas() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> page = new PageImpl<>(List.of(task));

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(taskRepository.findByUserId(user.getId(), pageable)).thenReturn(page);

        Page<TaskResponse> resultado = taskService.findAll(USER_EMAIL, pageable);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getTitle()).isEqualTo("Estudar Spring Boot");
    }

    @Test
    @DisplayName("Deve buscar tarefa por ID com sucesso")
    void deveBuscarTarefaPorId() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(taskRepository.findByIdAndUserId(1L, user.getId())).thenReturn(Optional.of(task));

        TaskResponse response = taskService.findById(1L, USER_EMAIL);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Estudar Spring Boot");
    }

    @Test
    @DisplayName("Deve lancar excecao quando tarefa nao for encontrada")
    void deveLancarExcecaoQuandoTarefaNaoEncontrada() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(taskRepository.findByIdAndUserId(99L, user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.findById(99L, USER_EMAIL))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Tarefa não encontrada!");
    }

    @Test
    @DisplayName("Deve deletar tarefa com sucesso")
    void deveDeletarTarefaComSucesso() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(taskRepository.findByIdAndUserId(1L, user.getId())).thenReturn(Optional.of(task));

        taskService.delete(1L, USER_EMAIL);

        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    @DisplayName("Deve lancar excecao ao deletar tarefa inexistente")
    void deveLancarExcecaoAoDeletarTarefaInexistente() {
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(taskRepository.findByIdAndUserId(99L, user.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.delete(99L, USER_EMAIL))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Tarefa não encontrada!");

        verify(taskRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Deve filtrar tarefas por status")
    void deveFiltrarTarefasPorStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> page = new PageImpl<>(List.of(task));

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(taskRepository.findByUserIdAndStatus(user.getId(), TaskStatus.PENDING, pageable)).thenReturn(page);

        Page<TaskResponse> resultado = taskService.findByStatus(USER_EMAIL, TaskStatus.PENDING, pageable);

        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getStatus()).isEqualTo(TaskStatus.PENDING);
    }

    @Test
    @DisplayName("Deve buscar tarefas por titulo")
    void deveBuscarTarefasPorTitulo() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> page = new PageImpl<>(List.of(task));

        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(taskRepository.findByUserIdAndTitleContainingIgnoreCase(user.getId(), "Spring", pageable)).thenReturn(page);

        Page<TaskResponse> resultado = taskService.search(USER_EMAIL, "Spring", pageable);

        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getTitle()).contains("Spring");
    }
}