package com.app.todos.infrastructure.configuration;

import com.app.todos.application.gateway.auth.AuthGateway;
import com.app.todos.application.usecases.auth.AuthUseCase;
import com.app.todos.infrastructure.gateway.auth.AuthRepoGateway;
import com.app.todos.infrastructure.gateway.auth.TokenProvider;
import com.app.todos.infrastructure.mapper.user.UserEntityMapper;
import com.app.todos.infrastructure.persistence.users.UserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthConfiguration {
    @Bean
    public AuthUseCase authUseCase(AuthGateway authGateway) {
        return new AuthUseCase(authGateway);
    }

    @Bean
    public AuthGateway authGateway(PasswordEncoder passwordEncoder, UserRepo userRepo, TokenProvider tokenService, AuthenticationManager authenticationManager, UserEntityMapper userEntityMapper) {
        return new AuthRepoGateway(passwordEncoder, userRepo, tokenService, authenticationManager, userEntityMapper);
    }
}
