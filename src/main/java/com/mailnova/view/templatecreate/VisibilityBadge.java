package com.mailnova.view.templatecreate;


import com.mailnova.view.campaign.model.Visibility;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;

public class VisibilityBadge extends Span {
    public VisibilityBadge(Visibility v) {
        addClassNames("mn-vis-badge", v == Visibility.PUBLIC ? "mn-vis-public" : "mn-vis-private");
        add(Ui.icon(v == Visibility.PUBLIC ? VaadinIcon.CHECK_CIRCLE_O : VaadinIcon.LOCK, "12px"),
            new Span(v.getLabel()));
    }
}
