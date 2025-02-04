package com.emobile.springtodo.model;

public enum TodoStatus {
    TO_DO("К выполнению"),
    IN_PROGRESS("В работе"),
    COMPLETED("Выполнено");

    private final String displayName;

    TodoStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
