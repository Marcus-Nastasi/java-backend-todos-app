package com.app.todos.infrastructure.gateway.security;

import com.app.todos.application.exception.AppException;
import com.app.todos.application.gateway.security.AuthGateway;
import com.app.todos.application.usecases.security.PasswordUseCase;
import com.app.todos.domain.users.User;
import com.app.todos.infrastructure.mapper.user.UserEntityMapper;
import com.app.todos.infrastructure.persistence.users.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthRepoGateway implements AuthGateway {
    private final PasswordUseCase passwordUseCase;
    private final UserRepo userRepo;
    private final TokenProvider tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserEntityMapper userEntityMapper;

    public AuthRepoGateway(PasswordUseCase passwordEncoder, UserRepo userRepo, TokenProvider tokenService, AuthenticationManager authenticationManager, UserEntityMapper userEntityMapper) {
        this.passwordUseCase = passwordEncoder;
        this.userRepo = userRepo;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public User getByEmail(String email) {
        return userEntityMapper.mapFromUserEntity(userRepo.findByEmail(email));
    }

    @Override
    public String login(String email, String password) {
        User u = getByEmail(email);
        if (u == null) throw new AppException("user not found");
        if (!passwordUseCase.matches(password, u.getPassword()))
            throw new AppException("password does not matches");
        var auth = new UsernamePasswordAuthenticationToken(email, password);
        authenticationManager.authenticate(auth);
        return tokenService.generate(u.getEmail());
    }
}
