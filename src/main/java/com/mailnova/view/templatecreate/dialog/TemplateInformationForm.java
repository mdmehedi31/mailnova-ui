package com.mailnova.view.templatecreate.dialog;

import com.mailnova.DTO.EmailTemplateDto;
import com.mailnova.DTO.EmailTemplateTypeDTO;
import com.mailnova.service.TemplateService;
import com.mailnova.view.campaign.model.Visibility;
import com.mailnova.view.templatecreate.Ui;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;

/**
 * Template information + content. Used by the Manual path, by "Edit" on an existing template,
 * and by "Edit" from the AI review — always with the same {@link TemplateContentEditor}.
 */
public class TemplateInformationForm extends Div {

    private final TextField name = new TextField("Template Name");
    private final Select<EmailTemplateTypeDTO> type = new Select<>();
   /* private final Select<String> typeSubtype = new Select<>();*/
 //   private final TextField subtype = new TextField("Subtype");
   // private final RadioButtonGroup<Visibility> visibility = new RadioButtonGroup<>();
    private final TemplateContentEditor content;
    private final TemplateService service;
    private final EmailTemplateDto base;
    private final boolean source;
    private boolean syncing;
    private Integer createdBy=1;
    private List<EmailTemplateTypeDTO> emailTypes;
    public TemplateInformationForm(EmailTemplateDto existing,boolean isBuildByAi,
                                   Runnable onChange, TemplateService service,
                                   List<EmailTemplateTypeDTO> emailTemplateTypes) {
        this.base = existing;
        this.source = isBuildByAi;
        this.service = service;
        this.emailTypes = emailTemplateTypes;
        addClassName("mn-info-form");
        content = new TemplateContentEditor("Email Subject", null, onChange);

        name.setRequiredIndicatorVisible(true);
        name.setPlaceholder("e.g., Interview Invitation");
        name.setWidthFull();
       // name.addClassName("mn-input");
        name.addValueChangeListener(e -> { name.setInvalid(false); onChange.run(); });


        type.setItems(emailTypes);
        type.setItemLabelGenerator(EmailTemplateTypeDTO::getName);
        type.setWidthFull();
        type.addClassName("mn-input");/*
        typeSubtype.setWidthFull();
        typeSubtype.addClassName("mn-input");*/
    /*    subtype.setWidthFull();
        subtype.addClassName("mn-input");
*/
   /*     type.addValueChangeListener(e -> {
            if (e.getValue() == null) return;
            typeSubtype.setItems(TemplateTaxonomy.subtypesOf(e.getValue()));
            if (!syncing) typeSubtype.setValue(TemplateTaxonomy.subtypesOf(e.getValue()).get(0));
            onChange.run();
        });*/
      /*  typeSubtype.addValueChangeListener(e -> {
            if (e.getValue() != null && !syncing) subtype.setValue(e.getValue());
        });*/

       /* visibility.setItems(Visibility.values());
        visibility.setItemLabelGenerator(Visibility::getLabel);
        visibility.addThemeVariants(RadioGroupVariant.LUMO_HELPER_ABOVE_FIELD);
        visibility.addClassName("mn-radio-row");
        visibility.addValueChangeListener(e -> onChange.run());*/

        Div typeCol = Ui.div("mn-field", Ui.span("Type", "mn-field-label"),
                Ui.div("mn-type-pair", type));
     //   Div visCol = Ui.div("mn-field", Ui.span("Visibility", "mn-field-label"), visibility);

        add(Ui.div(null, name),Ui.div(null, typeCol), content);
        load(existing);
    }

    private void load(EmailTemplateDto t) {
        syncing = true;
        EmailTemplateTypeDTO existingType;
        if (t !=null && t.getTypeId() != null) {

            existingType= emailTypes.stream().filter(e->e.getId().
                    equals(t.getTypeId())).findAny().orElse(null);
            if (existingType != null) {
                type.setValue(existingType);
            }
        }
    //    typeSubtype.setItems(TemplateTaxonomy.subtypesOf(type.getValue()));
       /* String sub = t != null && t.getSubtype() != null ? t.getSubtype()
                : TemplateTaxonomy.subtypesOf(type.getValue()).get(0);
        if (TemplateTaxonomy.subtypesOf(type.getValue()).contains(sub)) typeSubtype.setValue(sub)
          subtype.setValue(sub);;*/

        syncing = false;
        if (t != null) {
            name.setValue(t.getName() == null ? "" : t.getName());
            content.setSubject(t.getEmailSubject());
            content.setBodyHtml(t.getEmailBody());
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
    public EmailTemplateDto toTemplate(boolean isBuildByAi) {
        EmailTemplateDto t = base != null ? base : new EmailTemplateDto();
        t.setName(name.getValue() == null || name.getValue().isBlank() ? "Untitled draft" : name.getValue().trim());
        t.setTypeId(type.getValue() !=null? type.getValue().getId():null);
        t.setEmailSubject(content.getSubject());
        t.setEmailBody(content.getBodyHtml());
        t.setPublic(isBuildByAi);
        t.setCreatedBy(createdBy);

        return t;
    }
}
