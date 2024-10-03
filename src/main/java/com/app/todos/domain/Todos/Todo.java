package com.app.todos.domain.Todos;

import com.app.todos.resources.enums.todos.Priority;
import com.app.todos.resources.enums.todos.Status;
import jakarta.persistence.*;

import java.math.BigInteger;
import java.time.LocalDate;

@Entity
@Table(name = "todos")
public class Todo {

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

    public Todo() {}

    public Todo(BigInteger user_id, String client, String title, String description, String link, LocalDate due, Priority priority) {
        this.user_id = user_id;
        this.client = client;
        this.title = title;
        this.description = description;
        this.link = link;
        this.creation = LocalDate.now();
        this.due = due;
        this.status = Status.PENDING;
        this.priority = priority;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getUser_id() {
        return user_id;
    }

    public void setUser_id(BigInteger user_id) {
        this.user_id = user_id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LocalDate getCreation() {
        return creation;
    }

    public void setCreation(LocalDate creation) {
        this.creation = creation;
    }

    public LocalDate getDue() {
        return due;
    }

    public void setDue(LocalDate due) {
        this.due = due;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}





