package com.mailnova.view.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

/** Tiny factory helpers so component classes stay declarative. */
public final class Ui {
    private Ui() {}
    private static void addCls(Component c, String cls) {
        for (String n : cls.split(" ")) if (!n.isBlank()) c.getElement().getClassList().add(n);
    }
    public static Div div(String cls, Component... c) { Div d = new Div(c); addCls(d, cls); return d; }
    public static Span span(String cls, String t) { Span s = new Span(t); addCls(s, cls); return s; }
    public static Icon icon(VaadinIcon i, String cls) { Icon x = new Icon(i); addCls(x, cls); return x; }
    public static Anchor link(String t, String href, String cls) { Anchor a = new Anchor(href, t); addCls(a, cls); return a; }
    /** Rounded colored tile (or gradient orb) holding an icon; tone = teal|indigo|purple|pink|orange|blue|cyan|green. */
    public static Div tile(VaadinIcon i, String tone, boolean orb) {
        return div("mn-tile mn-tone--" + tone + (orb ? " mn-tile--orb" : ""), icon(i, "mn-icon"));
    }
    public static Span pill(String text, String tone) { return span("mn-pill mn-tone--" + tone, text); }
    public static Div heading(String tone, String pill, String title, String sub) {
        Div d = div("mn-section-head", pill(pill, tone), h(2, "mn-h2", title));
        if (!sub.isBlank()) d.add(span("mn-sub", sub));
        return d;
    }
    public static Component h(int level, String cls, String t) {
        Component c = level == 1 ? new H1(t) : level == 2 ? new H2(t) : new H3(t);
        addCls(c, cls); return c;
    }
}
