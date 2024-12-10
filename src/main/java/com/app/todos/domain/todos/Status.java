package com.app.todos.domain.todos;

public enum Status {

    PENDING("pending"), PROGRESS("progress"), DONE("done");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
