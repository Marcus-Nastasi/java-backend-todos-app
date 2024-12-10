package com.app.todos.infrastructure.persistence.users;

import com.app.todos.infrastructure.entity.users.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface UserRepo extends JpaRepository<UserEntity, BigInteger> {

    UserEntity findByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT * FROM users WHERE(email=?1)")
    UserEntity findByUsername(String user);
}
