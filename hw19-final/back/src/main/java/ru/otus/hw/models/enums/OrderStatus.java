package ru.otus.hw.models.enums;


import lombok.Getter;

@Getter
public enum OrderStatus {

    CREATED("Create"),
    APPROVED("Approved");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public boolean isCreated() {
        return this == CREATED;
    }

    public boolean isProcessing() {
        return this == APPROVED;
    }

}

