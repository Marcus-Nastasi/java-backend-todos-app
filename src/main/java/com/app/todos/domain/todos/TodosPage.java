package com.app.todos.domain.todos;

import com.app.todos.infrastructure.entity.todos.TodoEntity;

import java.util.List;

public class TodosPage {
    private int page;
    private int per_page;
    private int total;
    private List<TodoEntity> data;

    public TodosPage() {}

    public TodosPage(int page, int per_page, int total, List<TodoEntity> data) {
        this.page = page;
        this.per_page = per_page;
        this.total = total;
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<TodoEntity> getData() {
        return data;
    }

    public void setData(List<TodoEntity> data) {
        this.data = data;
    }
}
