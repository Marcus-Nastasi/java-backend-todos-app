package com.app.todos.Repository.Todos;

import com.app.todos.Models.Todos.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface TodosRepo extends JpaRepository<Todo, BigInteger> {
}




