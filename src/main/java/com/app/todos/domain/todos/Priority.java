package com.app.todos.domain.todos;

import lombok.Getter;

@Getter
public enum Priority {

    LOW("low"), MEDIUM("medium"), HIGH("high");

    private final String Priority;

    Priority(String priority) {
        Priority = priority;
    }
}
