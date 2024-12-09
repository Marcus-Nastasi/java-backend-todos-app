package com.app.todos.domain.todos;

import lombok.Getter;

@Getter
public enum Status {

    PENDING("pending"), PROGRESS("progress"), DONE("done");

    private final String status;

    Status(String status) {
        this.status = status;
    }
}
