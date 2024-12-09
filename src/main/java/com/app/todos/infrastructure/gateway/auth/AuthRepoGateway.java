package com.app.todos.infrastructure.gateway.auth;

import com.app.todos.application.exception.AppException;
import com.app.todos.application.gateway.auth.AuthGateway;
import com.app.todos.domain.users.User;
import com.app.todos.infrastructure.entity.users.UserEntity;
import com.app.todos.infrastructure.mapper.user.UserEntityMapper;
import com.app.todos.infrastructure.persistence.users.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthRepoGateway implements AuthGateway {
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final TokenProvider tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserEntityMapper userEntityMapper;

    public AuthRepoGateway(PasswordEncoder passwordEncoder, UserRepo userRepo, TokenProvider tokenService, AuthenticationManager authenticationManager, UserEntityMapper userEntityMapper) {
        this.passwordEncoder = passwordEncoder;
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
        UserEntity u = userRepo.findByEmail(email);
        if (u == null) throw new AppException("user not found");
        if (!passwordEncoder.matches(password, u.getPassword())) throw new AppException("password does not matches");
        var auth = new UsernamePasswordAuthenticationToken(email, password);
        authenticationManager.authenticate(auth);
        return tokenService.generate(u.getUsername());
    }
}
