package com.mailnova.view.templatecreate;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;

/** KPI tile: colored icon tile + label + value + optional trend. */
public class StatCard extends Div {

    /** color: one of green, blue, purple, orange, teal. */
    public StatCard(VaadinIcon icon, String color, String label, String value, String trend) {
        this(icon, color, label, value, trend, false);
    }

    private StatCard(VaadinIcon icon, String color, String label, String value, String trend, boolean nameValue) {
        addClassNames("mn-stat", "mn-stat-" + color);
        Div text = Ui.div("mn-stat-text", Ui.span(label, "mn-stat-label"),
                Ui.span(value, nameValue ? "mn-stat-value mn-stat-value-sm" : "mn-stat-value"));
        if (trend != null) text.add(Ui.span(trend, nameValue ? "mn-stat-trend mn-stat-trend-plain" : "mn-stat-trend"));
        add(Ui.div("mn-stat-icon", Ui.icon(icon, "20px")), text);
    }

    /** "Most Used" variant: template name + use count instead of a big number. */
    public static StatCard mostUsed(String name, int uses) {
        return new StatCard(VaadinIcon.FIRE, "orange", "Most Used", name, uses + " uses", true);
    }
}
