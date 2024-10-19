package com.app.todos.resources.repository.Todos;

import com.app.todos.domain.todos.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface TodosRepo extends JpaRepository<Todo, BigInteger> {

    @Query(nativeQuery = true, value = "SELECT * FROM todos WHERE(user_id=?1);")
    Page<Todo> getUserTodos(
        BigInteger id,
        Pageable pageable
    );

    @Query(nativeQuery = true, value = "SELECT * FROM todos WHERE(user_id=?1 AND status=1);")
    Page<Todo> getInProgress(
        BigInteger id,
        Pageable pageable
    );

    @Query(nativeQuery = true, value = "SELECT * FROM todos WHERE(user_id=?1 AND status=0);")
    Page<Todo> getPending(
        BigInteger id,
        Pageable pageable
    );

    @Query(nativeQuery = true, value = "SELECT * FROM todos WHERE(user_id=?1 AND status=2);")
    Page<Todo> getDone(
        BigInteger id,
        Pageable pageable
    );

    @Query(nativeQuery = true, value =
            "SELECT t.* FROM Todos t " +
            "LEFT JOIN users u " +
            "ON t.user_id = u.id  " +
            "WHERE(:query IS NULL OR t.title LIKE CONCAT('%', :query, '%') " +
            "OR t.description LIKE CONCAT('%', :query, '%')) " +
            "ORDER BY t.id ASC;")
    Page<Todo> searchByTitle(
            @Param("query") String query,
            Pageable pageable
    );
}
