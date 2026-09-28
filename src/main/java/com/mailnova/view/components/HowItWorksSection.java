package com.mailnova.view.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;

public class HowItWorksSection extends Div {
    public HowItWorksSection() {
        addClassNames("mn-section", "mn-section--tinted");
        String[][] steps = {{"1", "Create Campaign", "Design your email, set your goals, and schedule your campaign.", "purple"},
                {"2", "Select Audience", "Choose from your lists or create smart segments.", "cyan"},
                {"3", "Send & Track Results", "Watch your campaign perform and get detailed insights.", "pink"}};
        VaadinIcon[] icons = {VaadinIcon.ENVELOPE_O, VaadinIcon.GROUP, VaadinIcon.BAR_CHART};
        Div row = Ui.div("mn-steps");
        for (int i = 0; i < steps.length; i++) {
            row.add(Ui.div("mn-step", Ui.tile(icons[i], steps[i][3], true), Ui.span("mn-card__title", steps[i][0] + ". " + steps[i][1]), Ui.span("mn-sub", steps[i][2])));
        }
        add(Ui.div("mn-container", Ui.heading("cyan", "Simple Process", "Get Started in 3 Simple Steps", ""), row));
    }
}
