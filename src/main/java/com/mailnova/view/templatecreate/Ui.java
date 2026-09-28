package com.mailnova.view.templatecreate;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

/** Tiny factory helpers so component classes stay readable. */
public final class Ui {
    private Ui() { }

    public static Div div(String cls, Component... children) {
        Div d = new Div(children);
        if (!cls.isBlank()) d.addClassNames(cls.split(" "));
        return d;
    }

    public static Span span(String text, String cls) {
        Span s = new Span(text);
        if (!cls.isBlank()) s.addClassNames(cls.split(" "));
        return s;
    }

    public static Icon icon(VaadinIcon v, String size) {
        Icon i = v.create();
        i.setSize(size);
        return i;
    }

    /** Square, bordered icon-only button (used in card action rows). */
    public static Button iconButton(VaadinIcon icon, String tooltip) {
        Button b = new Button(icon(icon, "15px"));
        b.addClassName("mn-icon-btn");
        b.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        b.getElement().setAttribute("title", tooltip);
        b.getElement().setAttribute("aria-label", tooltip);
        return b;
    }

    /** Borderless icon button (star, more, close). */
    public static Button plainIconButton(VaadinIcon icon, String tooltip, String cls) {
        Button b = new Button(icon(icon, "14px"));
        b.addClassName("mn-plain-btn");
        b.addClassNames(cls.split(" "));
        b.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        b.getElement().setAttribute("title", tooltip);
        b.getElement().setAttribute("aria-label", tooltip);
        return b;
    }

    public static Button primary(String text) {
        Button b = new Button(text);
        b.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        b.addClassName("mn-btn-primary");
        return b;
    }

    public static Button ghost(String text) {
        Button b = new Button(text);
        b.addClassName("mn-btn-ghost");
        return b;
    }
}
