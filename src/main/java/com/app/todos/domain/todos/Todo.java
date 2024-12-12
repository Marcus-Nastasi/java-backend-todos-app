package com.app.todos.domain.todos;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;

public class Todo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private BigInteger id;
    private BigInteger user_id;
    private String client;
    private String title;
    private String description;
    private String link;
    private LocalDate creation;
    private LocalDate due;
    private Status status;
    private Priority priority;
    private LocalDate last_updated;

    public Todo() {}

    public Todo(BigInteger id, BigInteger user_id, String client, String title, String description, String link, LocalDate creation, LocalDate due, Status status, Priority priority, LocalDate last_updated) {
        this.id = id;
        this.user_id = user_id;
        this.client = client;
        this.title = title;
        this.description = description;
        this.link = link;
        this.creation = creation;
        this.due = due;
        this.status = status;
        this.priority = priority;
        this.last_updated = last_updated;
    }

    public Todo updateDetails(Todo updatedTodo) {
        this.setTitle(updatedTodo.getTitle());
        this.setClient(updatedTodo.getClient());
        this.setDescription(updatedTodo.getDescription());
        this.setLink(updatedTodo.getLink());
        this.setDue(updatedTodo.getDue());
        this.setPriority(updatedTodo.getPriority());
        return this;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDate getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(LocalDate last_updated) {
        this.last_updated = last_updated;
    }
}
