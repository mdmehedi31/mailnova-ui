package com.mailnova.view.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;

public class HeroSection extends Div {
    private record Item(VaadinIcon icon, String tone, String text) {}
    private static final Item[] BENEFITS = {new Item(VaadinIcon.ENVELOPE_O, "teal", "Email Campaign Management"), new Item(VaadinIcon.COG, "green", "Delivery Automation"),
        new Item(VaadinIcon.GROUP, "indigo", "Audience Management"), new Item(VaadinIcon.BAR_CHART, "purple", "Campaign Analytics"),
        new Item(VaadinIcon.FILE_TEXT_O, "pink", "Template Management"), new Item(VaadinIcon.BOLT, "purple", "Marketing Productivity")};
    private static final Item[] TRUST = {new Item(VaadinIcon.BOLT, "green", "Fast Delivery"), new Item(VaadinIcon.BAR_CHART, "blue", "Campaign Analytics"),
        new Item(VaadinIcon.GROUP, "cyan", "Audience Segmentation"), new Item(VaadinIcon.FILE_TEXT_O, "indigo", "Template Library")};

    public HeroSection() {
        addClassName("mn-hero");
        Div benefits = Ui.div("mn-benefits");
        for (Item i : BENEFITS) benefits.add(Ui.div("mn-benefit", Ui.tile(i.icon(), i.tone(), false), Ui.span("", i.text())));
        Div trust = Ui.div("mn-trust");
        for (Item i : TRUST) trust.add(Ui.div("mn-trust__item mn-tone--" + i.tone(), Ui.icon(i.icon(), "mn-icon"), Ui.span("", i.text())));
        Div headline = Ui.div("mn-hero__headline", Ui.span("", "Reach Every Inbox."), Ui.span("mn-gradient-text", "Grow Every Campaign."));
        var h1 = Ui.h(1, "mn-h1", ""); h1.getElement().appendChild(headline.getElement());
        Div content = Ui.div("mn-hero__content", Ui.pill("Next Generation Email Marketing Platform", "indigo"), h1,
            Ui.span("mn-lead", "MailNova is a powerful email marketing platform that helps you create, manage and automate campaigns, build your audience, and track performance — all in one place."),
            benefits,
            Ui.div("mn-hero__cta", Ui.link("Start Free", "/signup", "mn-btn mn-btn--primary"), Ui.link("Explore Features", "/#features", "mn-btn mn-btn--secondary")),
            trust);
        Div note = Ui.div("mn-note", Ui.span("", "Your campaigns start here!"), Ui.icon(VaadinIcon.PAPERPLANE, "mn-note__plane"));
        note.getElement().setAttribute("aria-hidden", "true");
        add(Ui.div("mn-container mn-hero__inner", content, Ui.div("mn-glow", new LoginCard(), note)));
    }
}
