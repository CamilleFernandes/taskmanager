package com.seuprojeto.taskmanager.repository;

import com.seuprojeto.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// @Repository — indica que essa interface acessa o banco de dados
// JpaRepository<User, Long> — já nos dá save, findAll, findById, delete, etc.
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data cria a query automaticamente pelo nome do método!
    Optional<User> findByEmail(String email);

    // Verifica se já existe um usuário com esse email
    boolean existsByEmail(String email);
}
