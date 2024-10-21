package com.app.todos.resources.repository.Todos;

import com.app.todos.domain.todos.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

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
            "WHERE(t.user_id = :user_id) " +
            "AND (:query IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND (:query IS NULL OR LOWER(t.description) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "ORDER BY t.id ASC;")
    Page<Todo> searchByTitleOrDesc(
            @Param("user_id") BigInteger user_id,
            @Param("query") String query,
            Pageable pageable
    );
}
