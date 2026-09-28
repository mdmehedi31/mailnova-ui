package com.mailnova.view.campaign.model;

public enum Visibility {
    PUBLIC("Public"), PRIVATE("Private");

    private final String label;
    Visibility(String label) { this.label = label; }
    public String getLabel() { return label; }
}
