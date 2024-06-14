package com.app.todos.Services.Users;

import com.app.todos.DTOs.Users.NewUserDTO;
import com.app.todos.Models.Users.User;
import com.app.todos.Repository.User.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void newUser(NewUserDTO data) {
        String encoded = passwordEncoder.encode(data.password());
        User u = new User(data.name(), data.email(), encoded);
        userRepo.save(u);
    }
}



