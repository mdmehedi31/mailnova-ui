package com.mailnova.view.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Reusable "icon badge + heading + subtitle" header, used both for the page
 * title ("Create Email Campaign") and for each step's Main Content Header
 * (e.g. "Schedule & Review"). The badge accent color is passed in so each
 * step keeps its own theme (blue / emerald / purple / orange) while sharing
 * one layout.
 */
public class SectionHeader extends HorizontalLayout {

    /**
     * @param icon        glyph shown inside the badge
     * @param accentClass CSS modifier class controlling the badge color, e.g. "mn-accent-blue"
     * @param title       heading text
     * @param subtitle    supporting description text
     * @param large       true for the page-level title (bigger type), false for in-card step headers
     */
    public SectionHeader(VaadinIcon icon, String accentClass, String title, String subtitle, boolean large) {
        addClassName("mn-section-header");
        setSpacing(true);
        setAlignItems(Alignment.START);
        setWidthFull();

        Icon glyph = icon.create();
        glyph.addClassName("mn-section-badge-icon");
        Div badge = new Div(glyph);
        badge.addClassName("mn-section-badge");
        badge.addClassName(accentClass);
        if (large) {
            badge.addClassName("mn-section-badge-lg");
        }

        Component heading = large ? new H2(title) : new H3(title);
        heading.addClassName(large ? "mn-page-title" : "mn-step-title");

        Span sub = new Span(subtitle);
        sub.addClassName(large ? "mn-page-subtitle" : "mn-step-subtitle");

        VerticalLayout text = new VerticalLayout(heading, sub);
        text.addClassName("mn-section-header-text");
        text.setPadding(false);
        text.setSpacing(false);

        add(badge, text);
        setFlexGrow(1, text);
    }
}
