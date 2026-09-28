package com.mailnova.view.templatecreate.dialog;

import com.mailnova.DTO.EmailTemplateDto;
import com.mailnova.DTO.EmailTemplateTypeDTO;
import com.mailnova.service.TemplateService;
import com.mailnova.view.campaign.model.TemplateSource;
import com.mailnova.view.templatecreate.SourceBadge;
import com.mailnova.view.templatecreate.TemplateArt;
import com.mailnova.view.templatecreate.Ui;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;

import java.util.List;
import java.util.function.Consumer;

/**
 * The single "Create Template" dialog (never a route). Manual, AI and Edit all run through this one
 * orchestrator and the same {@link DialogShell}; only the body swaps:
 *   chooser -> Manual form | AI wizard -> (review) -> shared editor -> save.
 */
public class CreateTemplateDialog extends Dialog {

    private final TemplateService service;
    private final Consumer<EmailTemplateDto> onSaved;
    private final DialogShell shell = new DialogShell();

    private boolean dirty;
    private AiGenerationPanel wizard;
    private TemplateInformationForm form;
    private List<EmailTemplateTypeDTO> emailTemplateTypeDTOList;

    public CreateTemplateDialog(TemplateService service, Consumer<EmailTemplateDto> onSaved) {
        this.service = service;
        this.onSaved = onSaved;

        emailTemplateTypeDTOList=service.getEmailTemplateTypes();
        addClassName("mn-dialog");
        addThemeVariants(DialogVariant.LUMO_NO_PADDING);
        //setModal(true);
        setDraggable(false);
        setResizable(false);
        setMaxWidth("96vw");
        setMaxHeight("92vh");
        add(shell);
        // Esc / outside click / X all go through the same "unsaved changes?" check.
        addDialogCloseActionListener(e -> attemptClose());

    }

    // ------------------------------------------------------------------ entry points

    public void openChooser() { showChooser(); open(); }

    public void openManual() { showEditor(null, false, this::showChooser, "Create Manual Template"); open(); }

    public void openAi() { showAi(); open(); }

    public void openEdit(EmailTemplateDto t) {
        showEditor(t, t.getAiBuild(), this::attemptClose, "Edit Template");
        open();
    }

    // ------------------------------------------------------------------ screens

    private void showChooser() {
        dirty = false;
        setWidth("680px");
        Div head = Ui.div("mn-dlg-head-row", Ui.span("Create Template", "mn-dlg-title"), closeButton());
        Div body = Ui.div("mn-choose", Ui.span("How would you like to start?", "mn-choose-title"),
                Ui.span("Both options lead to the same editor — you stay in control of the final email.", "mn-choose-sub"),
                new CreationMethodSelector(this::onMethodChosen));
        Button cancel = Ui.ghost("Cancel");
        cancel.addClickListener(e -> attemptClose());
        shell.setBodyClass("");
        shell.set(head, body, Ui.div("mn-foot-row mn-foot-end", cancel));
    }

    private void onMethodChosen(TemplateSource s) {
        if (s == TemplateSource.MANUAL) showEditor(null, false, this::showChooser, "Create Manual Template");
        else showAi();
    }

    /** Manual create, Edit existing, or "Edit" from the AI review — same form, same editor. */
    private void showEditor(EmailTemplateDto existing, boolean isBuildByAI, Runnable back, String title) {
        setWidth("760px");
        form = new TemplateInformationForm(existing, isBuildByAI, () -> dirty = true,service, emailTemplateTypeDTOList);
        Button backBtn = new Button("Back", Ui.icon(VaadinIcon.ARROW_LEFT, "14px"));
        backBtn.addClassName("mn-back-link");
        backBtn.addClickListener(e -> back.run());
        Button draft = Ui.getErrorButton("Cancel");
        draft.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_SUCCESS);
        draft.addClickListener(e -> this.close());
        Button save = Ui.primary(existing != null && service.findAll().contains(existing) ? "Save Changes" : "Save Template");
        save.addClickListener(e -> {
            if (form.validate())
                save(form.toTemplate(false));
        });
        Div head = Ui.div("mn-dlg-head-row",
                Ui.div("mn-head-left", backBtn, Ui.span(title, "mn-dlg-title")));
        Div footer= Ui.div("mn-head-actions", draft, save);
        shell.setBodyClass("");
        shell.set( head,form, footer);
    }

    private void showAi() {
        dirty = false;
        wizard = new AiGenerationPanel(service, this::renderAi, () -> dirty = true,emailTemplateTypeDTOList);
        renderAi();
    }

    /** Header/footer for the current AI step; body is owned by the wizard. */
    private void renderAi() {
        AiGenerationPanel.Step step = wizard.getStep();
        setWidth(step == AiGenerationPanel.Step.REVIEW ? "640px" : "600px");
        switch (step) {
            case TYPE, DESCRIBE -> {
                Button back = new Button(Ui.icon(VaadinIcon.ARROW_LEFT, "14px"));
                back.addClassName("mn-back-link");
                back.getElement().setAttribute("aria-label", "Back");
                back.addClickListener(e -> { if (step == AiGenerationPanel.Step.TYPE) showChooser(); else wizard.back(); });
                Div head = Ui.div("mn-dlg-head-row", Ui.div("mn-head-left", back, Ui.span("Generate with AI", "mn-dlg-title")));
                Button cancel = Ui.ghost("Cancel");
                cancel.addClickListener(e -> attemptClose());
                Button next = Ui.primary(step == AiGenerationPanel.Step.TYPE ? "Next" : "Generate");
                next.addClickListener(e -> wizard.next());
                shell.setBodyClass("");
                shell.set(head, wizard, Ui.div("mn-foot-row", cancel, next));
            }
            case LOADING -> {
                Div head = Ui.div("mn-dlg-head-row", Ui.div("mn-head-left", Ui.span("Generate with AI", "mn-dlg-title")));
                shell.setBodyClass("");
                shell.set(head, wizard, null);
            }
            case REVIEW -> {
                Div head = Ui.div("mn-review-head",
                        Ui.div("mn-review-top", new SourceBadge(true), closeButton()),
                        Ui.div("mn-review-title-row",
                                Ui.div("mn-review-titles", Ui.span(wizard.getTemplateName(), "mn-review-title"),
                                        Ui.span(wizard.getType() + "  •  " , "mn-tcard-meta")),
                                TemplateArt.envelope()));
                Button use = Ui.primary("Use Template");
                use.addClassName("mn-use-wide");
                use.addClickListener(e -> save(wizard.toTemplate()));
                Button edit = Ui.ghost("Edit");
                edit.addClickListener(e -> showEditor(wizard.toTemplate(), true,
                        this::renderAi, "Edit Template"));
                Button dup = Ui.ghost("Duplicate");
                dup.addClickListener(e -> {
                    EmailTemplateDto c = wizard.toTemplate();
                    c.setName(c.getName() + " (Copy)");
                   // service.save(c);
                   // onSaved.accept(c);
                    Notification.show("Copy saved to your templates");
                });
                shell.setBodyClass("mn-review-body");
                shell.set(head, wizard, Ui.div("mn-foot-row mn-foot-review", use, edit, dup));
            }
        }
    }

    // ------------------------------------------------------------------ actions

    private Button closeButton() {
        Button b = Ui.plainIconButton(VaadinIcon.CLOSE_SMALL, "Close", "mn-close");
        b.addClickListener(e -> attemptClose());
        return b;
    }

    private void save(EmailTemplateDto emailTemplateDto) {
        /*service.save(t);
        dirty = false;
        onSaved.accept(t);*/

        EmailTemplateDto saveResponse= service.createEmailTemplate(emailTemplateDto);

        if (saveResponse != null) {
            onSaved.accept(saveResponse);
        }
        close();
    }

    private void attemptClose() {
        if (dirty) new ConfirmDiscardDialog(() -> { dirty = false; close(); }).open();
        else close();
    }
}
