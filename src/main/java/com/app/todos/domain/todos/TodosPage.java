package com.app.todos.domain.todos;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class TodosPage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int page;
    private int per_page;
    private int total;
    private List<Todo> data;

    public TodosPage() {}

    public TodosPage(int page, int per_page, int total, List<Todo> data) {
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

    public List<Todo> getData() {
        return data;
    }

    public void setData(List<Todo> data) {
        this.data = data;
    }
}
