package com.emobile.springtodo.model;

public enum TodoStatus {
    TO_DO,
    IN_PROGRESS,
    COMPLETED;

    public static boolean isValid(TodoStatus status) {
        for (TodoStatus s : values()) {
            if (s == status) {
                return true;
            }
        }
        return false;
    }
}
