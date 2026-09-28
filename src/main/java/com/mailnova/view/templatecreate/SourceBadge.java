package com.mailnova.view.templatecreate;


import com.mailnova.view.campaign.model.TemplateSource;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;

/** Pill flagging AI Generated vs Manual (the "AI Badge" / "Status Badge" of the spec). */
public class SourceBadge extends Span {
    public SourceBadge(TemplateSource source) {
        addClassName("mn-badge");
        if (source == TemplateSource.AI) {
            addClassName("mn-badge-ai");
            add(Ui.icon(VaadinIcon.MAGIC, "11px"), new Span("AI Generated"));
        } else {
            addClassName("mn-badge-manual");
            add(Ui.icon(VaadinIcon.FILE_TEXT_O, "11px"), new Span("Manual"));
        }
    }
}
