package com.app.todos.infrastructure.configuration.security;

import com.app.todos.application.gateway.security.AuthGateway;
import com.app.todos.application.usecases.security.AuthUseCase;
import com.app.todos.application.usecases.security.PasswordUseCase;
import com.app.todos.infrastructure.gateway.security.AuthRepoGateway;
import com.app.todos.infrastructure.gateway.security.TokenProvider;
import com.app.todos.infrastructure.mapper.user.UserEntityMapper;
import com.app.todos.infrastructure.persistence.users.UserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
public class AuthConfiguration {
    @Bean
    public AuthUseCase authUseCase(AuthGateway authGateway) {
        return new AuthUseCase(authGateway);
    }

    @Bean
    public AuthGateway authGateway(PasswordUseCase passwordUseCase, UserRepo userRepo, TokenProvider tokenService, AuthenticationManager authenticationManager, UserEntityMapper userEntityMapper) {
        return new AuthRepoGateway(passwordUseCase, userRepo, tokenService, authenticationManager, userEntityMapper);
    }

    @Bean
    public TokenProvider tokenProvider() {
        return new TokenProvider();
    }
}
