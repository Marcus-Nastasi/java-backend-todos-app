package com.app.todos.Enums.Todos;

public enum Priority {

    LOW("low"), MEDIUM("medium"), HIGH("high");

    private final String Priority;

    Priority(String priority) {
        Priority = priority;
    }

    public String getPriority() {
        return Priority;
    }
}



