package com.app.todos.infrastructure.configuration;

import com.app.todos.adapters.mapper.UserDtoMapper;
import com.app.todos.application.gateway.users.UserGateway;
import com.app.todos.application.usecases.users.UserUseCase;
import com.app.todos.infrastructure.gateway.auth.TokenService;
import com.app.todos.infrastructure.gateway.users.UserRepoGateway;
import com.app.todos.infrastructure.mapper.user.UserEntityMapper;
import com.app.todos.infrastructure.persistence.users.UserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserConfiguration {
    @Bean
    public UserUseCase userUseCase(UserGateway userGateway, PasswordEncoder passwordEncoderGateway, TokenService tokenService) {
        return new UserUseCase(userGateway, passwordEncoderGateway, tokenService);
    }

    @Bean
    public UserGateway userGateway(UserRepo userRepo, UserEntityMapper userEntityMapper) {
        return new UserRepoGateway(userRepo, userEntityMapper);
    }

    @Bean
    public UserDtoMapper userDtoMapper() {
        return new UserDtoMapper();
    }

    @Bean
    public UserEntityMapper userEntityMapper() {
        return new UserEntityMapper();
    }
}
