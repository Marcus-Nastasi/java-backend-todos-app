package com.app.todos.infrastructure.persistence.todos;

import com.app.todos.infrastructure.entity.todos.TodoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Map;

@Repository
public interface TodosRepo extends JpaRepository<TodoEntity, BigInteger> {

    @Query(nativeQuery = true, value =
            "SELECT t.* FROM Todos t WHERE t.user_id = :user_id " +
            "AND ((:query IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "OR (:query IS NULL OR LOWER(t.description) LIKE LOWER(CONCAT('%', :query, '%')))) " +
            "AND (:client IS NULL OR LOWER(t.client) LIKE LOWER(CONCAT('%', :client, '%'))) " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:priority IS NULL OR t.priority = :priority) " +
            "AND (t.creation >= :from) " +
            "AND (t.creation <= :to) " +
            "AND (t.due <= :due) " +
            "GROUP BY t.id ORDER BY t.id ASC;")
    Page<TodoEntity> getUserTodos(
        @Param("user_id") BigInteger user_id,
        @Param("query") String query,
        @Param("client") String client,
        // status: 0 = pending, 1 = in progress, 2 = done
        @Param("status") Integer status,
        // priority: 0 = low, 1 = medium, 2 = high
        @Param("priority") Integer priority,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to,
        @Param("due") LocalDate due,
        Pageable pageable
    );

    @Query(nativeQuery = true, value = """
            SELECT COUNT(*) AS total,\s
            COUNT(CASE WHEN t.status = 0 THEN 1 END) AS pending,\s
            COUNT(CASE WHEN t.status = 1 THEN 1 END) AS inProgress,
            COUNT(CASE WHEN t.status = 2 THEN 1 END) AS done,
            COUNT(CASE WHEN t.priority = 0 THEN 1 END) AS low,
            COUNT(CASE WHEN t.priority = 1 THEN 1 END) AS medium,
            COUNT(CASE WHEN t.priority = 2 THEN 1 END) AS high,
            COUNT(CASE WHEN t.due < NOW() AND t.status != 2 THEN 1 END) AS overdue,
            COUNT(CASE WHEN t.due > NOW() AND t.status != 2 THEN 1 END) AS future,
            CASE\s
                WHEN COUNT(*) = 0 THEN 0
                ELSE ROUND(COUNT(CASE WHEN t.status = 2 THEN 1 END) * 1.0 / COUNT(*), 2)
            END AS completion_rate
            FROM todos t WHERE t.user_id = :user_id
            AND (:client IS NULL OR LOWER(t.client) LIKE LOWER(CONCAT('%', :client, '%')))
            AND (t.creation >= :from)
            AND (t.creation <= :to);""")
    Map<String, BigDecimal> metricsQuery(
            @Param("user_id") BigInteger user_id,
            @Param("client") String client,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
