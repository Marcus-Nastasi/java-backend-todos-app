package com.app.todos.resources.repository.Todos;

import com.app.todos.domain.Todos.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface TodosRepo extends JpaRepository<Todo, BigInteger> {

    @Query(nativeQuery = true, value = "SELECT * FROM todos WHERE(user_id=?1);")
    Page<Todo> getUserTodos(
        BigInteger id,
        Pageable pageable
    );

    @Query(nativeQuery = true, value = "SELECT * FROM todos WHERE(user_id=?1 AND status=1);")
    List<Todo> getInProgress(BigInteger id);

    @Query(nativeQuery = true, value = "SELECT * FROM todos WHERE(user_id=?1 AND status=0);")
    List<Todo> getPending(BigInteger id);

    @Query(nativeQuery = true, value = "SELECT * FROM todos WHERE(user_id=?1 AND status=2);")
    List<Todo> getDone(BigInteger id);
}
