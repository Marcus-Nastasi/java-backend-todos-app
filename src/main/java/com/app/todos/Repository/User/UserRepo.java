package com.app.todos.Repository.User;

import com.app.todos.Models.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigInteger;

public interface UserRepo extends JpaRepository<User, BigInteger> {

    UserDetails findByEmail(String email);
}



