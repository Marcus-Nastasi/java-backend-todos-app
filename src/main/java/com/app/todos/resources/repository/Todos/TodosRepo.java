package com.app.todos.resources.repository.Todos;

import com.app.todos.domain.metrics.dtos.MetricsQntByPriorDto;
import com.app.todos.domain.metrics.dtos.MetricsQntByStatResponseDto;
import com.app.todos.domain.todos.Todo;
import org.hibernate.type.descriptor.jdbc.SmallIntJdbcType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    @Query(nativeQuery = true, value =
            "SELECT t.* FROM Todos t WHERE t.user_id = :user_id " +
            "AND (:query IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND (:query IS NULL OR LOWER(t.description) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (t.creation >= :from) " +
            "AND (t.creation <= :to) " +
            "ORDER BY t.id ASC;")
    List<Todo> massiveQuery(
            @Param("user_id") BigInteger user_id,
            @Param("query") String query,
            // status: 0 = pending, 1 = in progress, 2 = done
            @Param("status") Integer status,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @Query(nativeQuery = true, value = "SELECT t.* FROM Todos t " +
            "WHERE t.user_id = :userId " +
            "AND (t.creation >= :from) " +
            "AND (t.creation <= :to);")
    List<Todo> findAllByUserId(
            @Param("userId") BigInteger userId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @Query(nativeQuery = true, value = """
            SELECT COUNT(CASE WHEN t.status = 0 THEN 1 END) AS pending,
            COUNT(CASE WHEN t.status = 1 THEN 1 END) AS inProgress,
            COUNT(CASE WHEN t.status = 2 THEN 1 END) AS done,
            COUNT(*) AS total\s
            FROM todos t
            WHERE t.user_id = :user_id\s
            AND (t.creation >= :from)\s
            AND (t.creation <= :to);""")
    Map<String, Long> findQuantityByStatus(
            @Param("user_id") BigInteger user_id,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    @Query(nativeQuery = true, value = """
            SELECT COUNT(CASE WHEN t.priority = 0 THEN 1 END) AS high,
            COUNT(CASE WHEN t.priority = 1 THEN 1 END) AS medium,
            COUNT(CASE WHEN t.priority = 2 THEN 1 END) AS low
            FROM todos t
            WHERE t.user_id = :user_id;""")
    Map<String, Long> findQuantityByPriority(BigInteger user_id);
}
