package com.seuprojeto.taskmanager.service;

import com.seuprojeto.taskmanager.dto.AuthRequest;
import com.seuprojeto.taskmanager.dto.AuthResponse;
import com.seuprojeto.taskmanager.dto.RegisterRequest;
import com.seuprojeto.taskmanager.model.Role;
import com.seuprojeto.taskmanager.model.User;
import com.seuprojeto.taskmanager.repository.UserRepository;
import com.seuprojeto.taskmanager.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Cadastra um novo usuário e retorna o token JWT
    public AuthResponse register(RegisterRequest request) {

        // Verifica se email já existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado!");
        }

        // Cria o usuário com senha criptografada
        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .active(true)
                .build();

        userRepository.save(user);

        // Gera o token JWT
        var token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    // Faz login e retorna o token JWT
    public AuthResponse login(AuthRequest request) {

        // Autentica o usuário (lança exceção se inválido)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Busca o usuário no banco
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));

        // Gera o token JWT
        var token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}