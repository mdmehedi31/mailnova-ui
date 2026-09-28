package com.mailnova.view.components;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;

public class FinalCtaSection extends Div {
    public FinalCtaSection(String title, String text) {
        addClassName("mn-cta");
        Div left = Ui.div("mn-cta__deco mn-cta__deco--left", Ui.icon(VaadinIcon.PAPERPLANE, "mn-cta__plane"),
            Ui.div("mn-floatcard mn-floatcard--tilt-l", Ui.span("mn-dash__label", "Better Engagement"), Ui.span("mn-dash__value", "+68%")));
        Div right = Ui.div("mn-cta__deco mn-cta__deco--right",
            Ui.div("mn-floatcard mn-floatcard--tilt-r", Ui.span("mn-dash__label", "More Opens"), Ui.span("mn-dash__value", "+42%")));
        left.getElement().setAttribute("aria-hidden", "true");
        right.getElement().setAttribute("aria-hidden", "true");
        add(left, right, Ui.div("mn-container mn-cta__inner", Ui.span("mn-cta__pill", "Ready to Get Started?"), Ui.h(2, "mn-h2", title), Ui.span("mn-cta__text", text),
                Ui.div("mn-cta__buttons", Ui.link("Create Account", "/signup", "mn-btn mn-btn--white"), Ui.link("Sign In", "/#login", "mn-btn mn-btn--outline-white"))));
    }
}
