package com.mailnova.view.templatecreate;


import com.mailnova.DTO.EmailTemplateDto;
import com.mailnova.DTO.EmailTemplateTypeDTO;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import java.time.format.DateTimeFormatter;

/** Right-hand "Template Preview" panel: read-only view of the selected template. */
public class TemplatePreviewPanel extends Div {

    private final TemplateActions actions;
    private final Runnable onClose;

    public TemplatePreviewPanel(TemplateActions actions, Runnable onClose) {
        this.actions = actions;
        this.onClose = onClose;
        addClassName("mn-preview");
    }

    public void show(EmailTemplateDto t, EmailTemplateTypeDTO templateTypeDTO) {
        removeAll();
        setVisible(t != null);
        if (t == null) return;

        Button close = Ui.plainIconButton(VaadinIcon.CLOSE_SMALL, "Close preview", "mn-close");
        close.addClickListener(e -> onClose.run());
        Div head = Ui.div("mn-preview-head", Ui.span("Template Preview", "mn-preview-title"), close);

        Button star = Ui.plainIconButton(false ? VaadinIcon.STAR : VaadinIcon.STAR_O, "Favorite",
                false ? "mn-star mn-star-on" : "mn-star");
        star.addClickListener(e -> actions.toggleFavorite(t));
        Div badgeRow = Ui.div("mn-tcard-top", new SourceBadge(t.getAiBuild()), star);

        Div info = Ui.div("mn-preview-info", badgeRow,
                Ui.span(t.getName(), "mn-preview-name"),
                Ui.span(templateTypeDTO.getName() , "mn-tcard-meta"));

        String safe = Jsoup.clean(t.getEmailBody() == null ? "" : t.getEmailBody(), Safelist.relaxed());
        Div subject = Ui.div("mn-preview-box", Ui.span(t.getEmailSubject() == null ? "" : t.getEmailSubject(), "mn-preview-subject"));
        Div content = Ui.div("mn-preview-box mn-preview-content", new Html("<div>" + safe + "</div>"));

        Div tags = Ui.div("mn-preview-tags", Ui.span(templateTypeDTO !=null && templateTypeDTO.getName()!=null ? templateTypeDTO.getName():"", "mn-tag mn-tag-type"));

        Div meta = Ui.div("mn-preview-meta",
              /*  Ui.div("mn-preview-meta-col", Ui.span("Visibility", "mn-field-label"), new VisibilityBadge(t.getVisibility())),*/
                Ui.div("mn-preview-meta-col", Ui.span("Created", "mn-field-label"),
                        Ui.div("mn-chip-stat", Ui.icon(VaadinIcon.CALENDAR, "13px"),
                                Ui.span(t.getCreatedOn().format(DateTimeFormatter.ofPattern("MMM d, yyyy")), "mn-created"))));

        Button use = Ui.primary("Use Template");
        use.addClickListener(e -> actions.use(t));
        Button edit = Ui.ghost("Edit");
        edit.addClickListener(e -> actions.edit(t));
        Button dup = Ui.ghost("Duplicate");
        dup.addClickListener(e -> actions.duplicate(t));

        Div body = Ui.div("mn-preview-body", info,
                Ui.span("Subject", "mn-field-label"), subject,
                Ui.span("Email Content", "mn-field-label"), content,
                Ui.span("Tags", "mn-field-label"), tags, meta);
        add(head, body, Ui.div("mn-preview-foot", use, edit, dup));
    }
}
