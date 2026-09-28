package com.mailnova.view.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;

public class FeaturesSection extends Div {
    private record Feature(VaadinIcon icon, String tone, String title, String text) {}
    private static final Feature[] FEATURES = {
        new Feature(VaadinIcon.ENVELOPE_O, "blue", "Campaign Management", "Create, schedule, manage, and monitor campaigns."),
        new Feature(VaadinIcon.GROUP, "cyan", "Audience Segmentation", "Build targeted recipient groups."),
        new Feature(VaadinIcon.FILE_TEXT_O, "purple", "Template Builder", "Create and reuse email templates."),
        new Feature(VaadinIcon.BAR_CHART, "indigo", "Analytics & Reporting", "Track engagement and campaign performance."),
        new Feature(VaadinIcon.SHIELD, "green", "Delivery Monitoring", "View sent, delivered, failed, and opened metrics."),
        new Feature(VaadinIcon.FOLDER_O, "teal", "Smart Collections", "Filter and organize recipient lists.")};

    public FeaturesSection() {
        addClassName("mn-section");
        setId("features");
        Div grid = Ui.div("mn-grid mn-grid--3");
        for (Feature f : FEATURES) {
            grid.add(Ui.div("mn-card mn-feature mn-tone--" + f.tone(), Ui.tile(f.icon(), f.tone(), true),
                    Ui.div("mn-feature__body", Ui.span("mn-card__title", f.title()), Ui.span("mn-sub", f.text())),
                    Ui.div("mn-feature__arrow", Ui.icon(VaadinIcon.ANGLE_RIGHT, "mn-icon"))));
        }
        add(Ui.div("mn-container", Ui.heading("indigo", "Powerful Features", "Everything You Need for Email Success",
                "Powerful features designed to help you create, manage and optimize your email marketing campaigns with ease."), grid));
    }
}
