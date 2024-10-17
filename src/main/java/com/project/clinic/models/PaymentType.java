package com.project.clinic.models;

public enum PaymentType {
    Cash("Cash"),
    INSURANCE("Insurance");

    private final String displayName;

    PaymentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
