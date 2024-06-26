package com.app.todos.Repository.Todos;

import com.app.todos.Models.Todos.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface TodosRepo extends JpaRepository<Todo, BigInteger> {

    @Query(nativeQuery = true, value = "SELECT * FROM todos WHERE(user_id=?1);")
    List<Todo> getUserTodos(BigInteger id);

    @Query(nativeQuery = true, value = "SELECT * FROM todos WHERE(user_id=?1 AND status=1);")
    List<Todo> getInProgress(BigInteger id);

    @Query(nativeQuery = true, value = "SELECT * FROM todos WHERE(user_id=?1 AND status=0);")
    List<Todo> getPending(BigInteger id);

    @Query(nativeQuery = true, value = "SELECT * FROM todos WHERE(user_id=?1 AND status=2);")
    List<Todo> getDone(BigInteger id);
}




