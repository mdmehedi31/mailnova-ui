package com.mailnova.view.templatecreate.dialog;

import com.mailnova.view.campaign.model.EmailTemplate;
import com.mailnova.view.campaign.model.TemplateSource;
import com.mailnova.view.campaign.model.TemplateTaxonomy;
import com.mailnova.view.campaign.model.Visibility;
import com.mailnova.view.templatecreate.Ui;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;

/**
 * Template information + content. Used by the Manual path, by "Edit" on an existing template,
 * and by "Edit" from the AI review — always with the same {@link TemplateContentEditor}.
 */
public class TemplateInformationForm extends Div {

    private final TextField name = new TextField("Template Name");
    private final Select<String> type = new Select<>();
    private final Select<String> typeSubtype = new Select<>();
    private final TextField subtype = new TextField("Subtype");
    private final RadioButtonGroup<Visibility> visibility = new RadioButtonGroup<>();
    private final TemplateContentEditor content;

    private final EmailTemplate base;
    private final TemplateSource source;
    private boolean syncing;

    public TemplateInformationForm(EmailTemplate existing, TemplateSource source, Runnable onChange) {
        this.base = existing;
        this.source = source;
        addClassName("mn-info-form");
        content = new TemplateContentEditor("Email Subject", null, onChange);

        name.setRequiredIndicatorVisible(true);
        name.setPlaceholder("e.g., Interview Invitation");
        name.setWidthFull();
        name.addClassName("mn-input");
        name.addValueChangeListener(e -> { name.setInvalid(false); onChange.run(); });

        type.setItems(TemplateTaxonomy.types());
        type.setWidthFull();
        type.addClassName("mn-input");
        typeSubtype.setWidthFull();
        typeSubtype.addClassName("mn-input");
        subtype.setWidthFull();
        subtype.addClassName("mn-input");

        type.addValueChangeListener(e -> {
            if (e.getValue() == null) return;
            typeSubtype.setItems(TemplateTaxonomy.subtypesOf(e.getValue()));
            if (!syncing) typeSubtype.setValue(TemplateTaxonomy.subtypesOf(e.getValue()).get(0));
            onChange.run();
        });
        typeSubtype.addValueChangeListener(e -> {
            if (e.getValue() != null && !syncing) subtype.setValue(e.getValue());
        });

        visibility.setItems(Visibility.values());
        visibility.setItemLabelGenerator(Visibility::getLabel);
        visibility.addThemeVariants(RadioGroupVariant.LUMO_HELPER_ABOVE_FIELD);
        visibility.addClassName("mn-radio-row");
        visibility.addValueChangeListener(e -> onChange.run());

        Div typeCol = Ui.div("mn-field", Ui.span("Type", "mn-field-label"),
                Ui.div("mn-type-pair", type, typeSubtype));
        Div visCol = Ui.div("mn-field", Ui.span("Visibility", "mn-field-label"), visibility);

        add(Ui.div("mn-form-grid", name, typeCol, subtype, visCol), content);
        load(existing);
    }

    private void load(EmailTemplate t) {
        syncing = true;
        type.setValue(t != null && t.getType() != null ? t.getType() : TemplateTaxonomy.types().get(0));
        typeSubtype.setItems(TemplateTaxonomy.subtypesOf(type.getValue()));
        String sub = t != null && t.getSubtype() != null ? t.getSubtype()
                : TemplateTaxonomy.subtypesOf(type.getValue()).get(0);
        if (TemplateTaxonomy.subtypesOf(type.getValue()).contains(sub)) typeSubtype.setValue(sub);
        subtype.setValue(sub);
        syncing = false;
        visibility.setValue(t != null ? t.getVisibility() : Visibility.PUBLIC);
        if (t != null) {
            name.setValue(t.getName() == null ? "" : t.getName());
            content.setSubject(t.getSubject());
            content.setBodyHtml(t.getBody());
        }
    }

    /** Validates required fields and shows inline errors. */
    public boolean validate() {
        boolean ok = name.getValue() != null && !name.getValue().isBlank();
        name.setInvalid(!ok);
        name.setErrorMessage("Template name is required");
        return ok;
    }

    /** Writes the form into the existing template (edit) or a new one (create). */
    public EmailTemplate toTemplate(boolean draft) {
        EmailTemplate t = base != null ? base : new EmailTemplate();
        t.setName(name.getValue() == null || name.getValue().isBlank() ? "Untitled draft" : name.getValue().trim());
        t.setType(type.getValue());
        t.setSubtype(subtype.getValue() == null || subtype.getValue().isBlank() ? typeSubtype.getValue() : subtype.getValue());
        t.setSubject(content.getSubject());
        t.setBody(content.getBodyHtml());
        t.setVisibility(visibility.getValue());
        t.setSource(base != null ? base.getSource() : source);
        t.setDraft(draft);
        return t;
    }
}
