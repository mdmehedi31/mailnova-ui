package com.mailnova.view.components;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Div;

public class MailNovaLogo extends Div {
    private static final String MARK = "<svg class='mn-logo__mark' viewBox='0 0 40 30' aria-hidden='true'><defs>"
        + "<linearGradient id='mnLogoGrad' x1='0' y1='0' x2='1' y2='1'><stop offset='0' stop-color='#2563EB'/>"
        + "<stop offset='.55' stop-color='#7C3AED'/><stop offset='1' stop-color='#06B6D4'/></linearGradient></defs>"
        + "<path d='M5 26V7c0-1.4 1.7-2 2.6-1L20 19 32.4 6c.9-1 2.6-.4 2.6 1v19' fill='none' stroke='url(#mnLogoGrad)' "
        + "stroke-width='6' stroke-linecap='round' stroke-linejoin='round'/></svg>";

    public MailNovaLogo(boolean dark) {
        addClassNames("mn-logo", dark ? "mn-logo--dark" : "mn-logo--light");
        add(new Html(MARK), Ui.span("mn-logo__text", "MailNova"));
    }
}
