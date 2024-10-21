package com.app.todos.resources.repository.Todos;

import com.app.todos.domain.todos.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface TodosRepo extends JpaRepository<Todo, BigInteger> {

    @Query(nativeQuery = true, value =
            "SELECT t.* FROM Todos t " +
            "WHERE(t.user_id = :user_id) " +
            "AND (:query IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND (:query IS NULL OR LOWER(t.description) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND (:status IS NULL OR t.status = :status) " +
            "ORDER BY t.id ASC;")
    Page<Todo> getUserTodos(
        @Param("user_id") BigInteger user_id,
        @Param("query") String query,
        // status: 0 = pending, 1 = in progress, 2 = done
        @Param("status") Integer status,
        Pageable pageable
    );
}
