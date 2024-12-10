package com.app.todos.infrastructure.configuration.user;

import com.app.todos.adapters.mapper.UserDtoMapper;
import com.app.todos.application.gateway.security.PasswordGateway;
import com.app.todos.application.gateway.users.UserGateway;
import com.app.todos.application.usecases.users.UserUseCase;
import com.app.todos.infrastructure.gateway.security.TokenProvider;
import com.app.todos.infrastructure.gateway.users.UserRepoGateway;
import com.app.todos.infrastructure.mapper.user.UserEntityMapper;
import com.app.todos.infrastructure.persistence.users.UserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfiguration {
    @Bean
    public UserUseCase userUseCase(UserGateway userGateway, PasswordGateway passwordGateway, TokenProvider tokenProvider) {
        return new UserUseCase(userGateway, passwordGateway, tokenProvider);
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
