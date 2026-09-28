package com.mailnova.view.components;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.icon.VaadinIcon;

public class MailNovaFooter extends Footer {
    public MailNovaFooter() {
        addClassName("mn-footer");
        Div social = Ui.div("mn-footer__social", social("LinkedIn", VaadinIcon.TWITTER), social("Facebook", VaadinIcon.FACEBOOK), social("GitHub", VaadinIcon.AIRPLANE));
        Div cols = Ui.div("mn-footer__cols",
            Ui.div("mn-footer__brand", new MailNovaLogo(true), Ui.span("mn-footer__muted", "Smarter email marketing for a bigger tomorrow."), social),
            column("Product", "Features", "Pricing", "Templates"),
            column("Resources", "Documentation", "Support", "Blog"),
            column("Legal", "Privacy Policy", "Terms"));
        add(Ui.div("mn-container", cols, Ui.span("mn-footer__muted mn-footer__copy", "© 2025 MailNova. All rights reserved.")));
    }

    private static Anchor social(String label, VaadinIcon icon) {
        Anchor a = new Anchor("#", Ui.icon(icon, "mn-icon"));
        a.addClassName("mn-footer__social-link");
        a.getElement().setAttribute("aria-label", label);
        return a;
    }

    private static Div column(String title, String... links) {
        Div c = Ui.div("mn-footer__col", Ui.span("mn-footer__title", title));
        for (String l : links) c.add(Ui.link(l, "#", "mn-footer__link"));
        return c;
    }
}
