package com.mailnova.view;

import com.mailnova.view.components.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("MailNova — Reach Every Inbox. Grow Every Campaign.")
public class HomeView extends Div {
    public HomeView() {
        addClassName("mn-page");
        Div preview = Ui.div("mn-container mn-preview", new DashboardPreview(false),
            Ui.div("mn-preview__copy", Ui.pill("Real Results, Real Growth", "pink"), Ui.h(2, "mn-h2", "Powerful Dashboard. Real Results."),
                Ui.span("mn-sub", "Get a complete overview of your email campaigns, audience growth and performance metrics — all in one beautiful, easy-to-use dashboard."),
                Ui.span("mn-check", "Real-time campaign performance"), Ui.span("mn-check", "Audience growth tracking"),
                Ui.span("mn-check", "Detailed analytics and reports"), Ui.span("mn-check", "Quick access to recent activity")));
        add(new MailNovaNavbar(false), new HeroSection(), Ui.div("mn-section mn-section--soft", preview), new FeaturesSection(),
            new HowItWorksSection(), new TestimonialsSection(),
            new FinalCtaSection("Ready to launch your next email campaign?", "Join thousands of businesses using MailNova to grow their audience and drive real results."),
            new MailNovaFooter());
    }
}
