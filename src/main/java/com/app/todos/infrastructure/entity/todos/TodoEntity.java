package com.app.todos.infrastructure.entity.todos;

import com.app.todos.domain.todos.Priority;
import com.app.todos.domain.todos.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;

@Entity
@Table(name = "todos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "bigint")
    private BigInteger id;
    @Column
    private BigInteger user_id;
    @Column
    private String client;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private String link;
    @Column
    private LocalDate creation;
    @Column
    private LocalDate due;
    @Column
    @Enumerated(value = EnumType.ORDINAL)
    private Status status;
    @Column
    @Enumerated(value = EnumType.ORDINAL)
    private Priority priority;
    @Column(name = "last_updated")
    private LocalDate last_updated;

    public TodoEntity(
            BigInteger user_id,
            String client,
            String title,
            String description,
            String link,
            LocalDate creation,
            LocalDate due,
            Priority priority,
            LocalDate last_updated
    ) {
        this.user_id = user_id;
        this.client = client;
        this.title = title;
        this.description = description;
        this.link = link;
        this.creation = creation;
        this.due = due;
        this.status = Status.PENDING;
        this.priority = priority;
        this.last_updated = last_updated;
    }
}
