package com.app.todos.Enums.Todos;

public enum Status {

    PENDING("pending"), STARTED("started"), DONE("done");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}


