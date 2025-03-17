package ru.otus.hw.models.enums;


import lombok.Getter;

@Getter
public enum OrderStatus {

    CREATED("Создан"),
    PROCESSING("В процессе"),
    COMPLETED("Завершен"),
    CANCELLED("Отменен");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public boolean isCreated() {
        return this == CREATED;
    }

    public boolean isProcessing() {
        return this == PROCESSING;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }

    public boolean isCancelled() {
        return this == CANCELLED;
    }
}

