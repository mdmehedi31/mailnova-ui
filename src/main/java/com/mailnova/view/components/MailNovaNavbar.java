package com.mailnova.view.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.icon.VaadinIcon;

/** Sticky navbar; collapses behind a hamburger below 768px. */
public class MailNovaNavbar extends Div {
    public MailNovaNavbar(boolean signupPage) {
        addClassName("mn-navbar");
        Nav nav = new Nav();
        nav.addClassName("mn-nav");
        nav.getElement().setAttribute("aria-label", "Primary");
        for (String s : new String[]{"Features", "Solutions", "Pricing", "Templates", "About"}) {
            nav.add(Ui.link(s, s.equals("Features") ? "/#features" : "#", "mn-nav__link"));
        }
        Div actions = Ui.div("mn-nav__actions");
        if (signupPage) {
            actions.add(Ui.link("Sign In", "/#login", "mn-btn mn-btn--secondary mn-btn--sm"),
                    Ui.link("Create Account", "/signup", "mn-btn mn-btn--primary mn-btn--sm"));
        } else {
            actions.add(Ui.link("Sign Up", "/signup", "mn-btn mn-btn--primary mn-btn--sm"),
                    Ui.link("Login", "/#login", "mn-btn mn-btn--secondary mn-btn--sm"));
        }
        Button burger = new Button(Ui.icon(VaadinIcon.MENU, "mn-icon"));
        burger.addClassName("mn-burger");
        burger.getElement().setAttribute("aria-label", "Toggle menu");
        burger.addClickListener(e -> {
            getClassNames().set("mn-navbar--open", !getClassNames().contains("mn-navbar--open"));
        });
        add(Ui.div("mn-container mn-navbar__inner", new MailNovaLogo(false), nav, actions, burger));
    }
}
