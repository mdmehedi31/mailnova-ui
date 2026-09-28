package com.mailnova.view;

import com.mailnova.service.SignupService;
import com.mailnova.view.components.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("signup")
@PageTitle("Create your MailNova account")
public class SignupView extends Div {
    private record Feature(VaadinIcon icon, String tone, String title, String text) {}
    private static final Feature[] FEATURES = {
        new Feature(VaadinIcon.ENVELOPE_O, "indigo", "Email Campaigns", "Create and send beautiful email campaigns."),
        new Feature(VaadinIcon.PAPERPLANE, "blue", "Template Library", "Use ready-made or create your own templates."),
        new Feature(VaadinIcon.GROUP, "green", "Audience Management", "Build and manage targeted audience segments."),
        new Feature(VaadinIcon.COG, "purple", "Automation", "Automate your email marketing workflows."),
        new Feature(VaadinIcon.BAR_CHART, "orange", "Advanced Analytics", "Track performance and grow faster."),
        new Feature(VaadinIcon.SHIELD, "blue", "Secure & Reliable", "Your data is always safe and protected.")};
    private static final String[][] LOGOS = {{"Microsoft", "#F25022"}, {"Shopify", "#5A9E3A"}, {"Google", "#4285F4"}, {"Slack", "#E01E5A"}, {"Dropbox", "#0061FF"}, {"Spotify", "#1DB954"}};

    public SignupView(SignupService signupService) {
        addClassName("mn-page");
        Div grid = Ui.div("mn-benefits mn-benefits--rich");
        for (Feature f : FEATURES) {
            grid.add(Ui.div("mn-benefit", Ui.tile(f.icon(), f.tone(), false),
                Ui.div("", Ui.span("mn-card__title", f.title()), Ui.span("mn-sub", f.text()))));
        }
        Div logos = Ui.div("mn-logos");
        for (String[] l : LOGOS) {
            Span s = Ui.span("mn-logos__item", l[0]);
            s.getStyle().set("--logo", l[1]);
            logos.add(s);
        }
        var h1 = Ui.h(1, "mn-h1", "Create Your ");
        h1.getElement().appendChild(Ui.span("mn-gradient-text", "MailNova").getElement());
        h1.getElement().appendChild(new com.vaadin.flow.dom.Element("span").setText(" Account"));
        Div left = Ui.div("mn-hero__content", Ui.pill("Join Thousands of Businesses", "indigo"), h1,
            Ui.span("mn-lead", "Start your email marketing journey today. Create your account to manage campaigns, grow your audience, and achieve better results — all in one place."),
            grid, new DashboardPreview(true), Ui.span("mn-muted mn-trusted", "Trusted by growing businesses worldwide"), logos);
        Div right = Ui.div("mn-signup-col", Ui.div("mn-glow", new SignupForm(signupService)),
            Ui.div("mn-secure", Ui.tile(VaadinIcon.SHIELD, "indigo", false), Ui.div("", Ui.span("mn-card__title", "Your data is safe with us."),
                Ui.span("mn-muted", "We use industry-standard security and encryption to protect your information."))));
        add(new MailNovaNavbar(true), Ui.div("mn-hero mn-hero--signup", Ui.div("mn-container mn-hero__inner mn-hero__inner--signup", left, right)),
            new FinalCtaSection("Ready to grow your business with email?", "Join thousands of companies using MailNova to reach more customers and achieve real results."),
            new MailNovaFooter());
    }
}
