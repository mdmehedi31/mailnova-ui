package com.mailnova.view.templatecreate.dialog;

import com.mailnova.view.campaign.model.TemplateSource;
import com.mailnova.view.templatecreate.SourceBadge;
import com.mailnova.view.templatecreate.TemplateArt;
import com.mailnova.view.templatecreate.Ui;
import com.vaadin.flow.component.html.Div;

/**
 * One creation-method card. Uses the same visual language as the provided template cards
 * (source badge, thumbnail on the right, title, description). Manual and AI reuse this one class.
 */
public class MethodCard extends Div {

    public MethodCard(TemplateSource source, String title, String description, String hint, Runnable onChoose) {
        addClassNames("mn-method-card", source == TemplateSource.AI ? "mn-method-ai" : "mn-method-manual");
        getElement().setAttribute("role", "button");
        getElement().setAttribute("tabindex", "0");
        getElement().setAttribute("aria-label", title);

        add(Ui.div("mn-method-top", new SourceBadge(source)),
            TemplateArt.thumb(source),
            Ui.span(title, "mn-method-title"),
            Ui.span(description, "mn-method-desc"),
            Ui.span(hint, "mn-method-hint"));

        addClickListener(e -> onChoose.run());
        getElement().addEventListener("keydown", e -> onChoose.run()).setFilter("event.key === 'Enter' || event.key === ' '");
    }

    public void setSelected(boolean selected) {
        setClassName("mn-method-selected", selected);
    }
}
