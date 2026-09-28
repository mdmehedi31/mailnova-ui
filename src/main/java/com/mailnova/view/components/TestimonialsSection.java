package com.mailnova.view.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;

public class TestimonialsSection extends Div {
    private record Quote(String name, String role, String text, String tone) {}
    private static final Quote[] QUOTES = {
        new Quote("Sarah Johnson", "Marketing Manager, Acme Inc.", "MailNova has completely transformed our email marketing. The analytics are incredible and the platform is so easy to use.", "pink"),
        new Quote("Michael Chen", "Growth Lead, BrightTech", "The automation features save us hours every week. Our engagement rates have never been higher!", "blue"),
        new Quote("Emily Rodriguez", "Head of Marketing, GlobalSoft", "Beautiful templates, powerful analytics, and excellent support. MailNova is simply the best.", "purple")};

    public TestimonialsSection() {
        addClassName("mn-section");
        Div grid = Ui.div("mn-grid mn-grid--3");
        for (Quote q : QUOTES) {
            var stars = Ui.span("mn-stars", "★★★★★");
            stars.getElement().setAttribute("aria-label", "5 out of 5 stars");
            grid.add(Ui.div("mn-card mn-testimonial mn-tone--" + q.tone(),
                    Ui.span("mn-avatar", q.name().substring(0, 1)),
                    Ui.div("", Ui.icon(VaadinIcon.QUOTE_LEFT, "mn-quote"), Ui.span("mn-sub", q.text()), Ui.span("mn-card__title mn-testimonial__name", q.name()), Ui.span("mn-muted", q.role()), stars)));
        }
        add(Ui.div("mn-container", Ui.heading("blue", "Loved by 10,000+ Businesses", "Trusted by Teams Around the World", ""), grid));
    }
}
