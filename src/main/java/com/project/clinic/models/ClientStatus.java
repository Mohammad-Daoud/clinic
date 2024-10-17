package com.project.clinic.models;

public enum ClientStatus {
    OPEN("Open"),
    IN_PROGRESS("In-Progress"),
    CLOSED("Closed");

    private final String displayName;

    ClientStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}