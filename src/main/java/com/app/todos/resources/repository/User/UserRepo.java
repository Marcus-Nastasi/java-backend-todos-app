package com.app.todos.resources.repository.User;

import com.app.todos.domain.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigInteger;

public interface UserRepo extends JpaRepository<User, BigInteger> {

    UserDetails findByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT * FROM users WHERE(email=?1)")
    User findByUsername(String user);
}



