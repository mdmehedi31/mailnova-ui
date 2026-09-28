package com.mailnova.view.campaign.model;

/** How the template content was originally produced. */
public enum TemplateSource {
    MANUAL("Manual"), AI("AI Generated");

    private final String label;
    TemplateSource(String label) { this.label = label; }
    public String getLabel() { return label; }
}
