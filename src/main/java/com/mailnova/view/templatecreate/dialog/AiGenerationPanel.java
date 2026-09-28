package com.mailnova.view.templatecreate.dialog;


import com.mailnova.service.TemplateService;
import com.mailnova.view.campaign.model.EmailTemplate;
import com.mailnova.view.campaign.model.TemplateSource;
import com.mailnova.view.campaign.model.TemplateTaxonomy;
import com.mailnova.view.campaign.model.Visibility;
import com.mailnova.view.templatecreate.Ui;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

import java.util.concurrent.CompletableFuture;

/**
 * AI Generate path: Type & Subtype -> Describe -> (loading) -> Review.
 * Only the way the content is produced differs from Manual; the review step renders the
 * generated draft into the SAME {@link TemplateContentEditor}, fully editable.
 */
public class AiGenerationPanel extends Div {

    public enum Step { TYPE, DESCRIBE, LOADING, REVIEW }

    private final TemplateService service;
    private final Runnable onStepChange;
    private final Runnable onDirty;

    private final Select<String> type = new Select<>();
    private final Select<String> subtype = new Select<>();
    private final TextField name = new TextField("Template Name");
    private final TextField purpose = new TextField("Email purpose");
    private final TextArea instructions = new TextArea("Additional instructions");
    private final TemplateContentEditor editor;
    private final Div stepper = Ui.div("mn-stepper");
    private final Div stepBody = Ui.div("mn-step-body");

    private Step step = Step.TYPE;
    private boolean generating;
    private String error;

    public AiGenerationPanel(TemplateService service, Runnable onStepChange, Runnable onDirty) {
        this.service = service;
        this.onStepChange = onStepChange;
        this.onDirty = onDirty;
        addClassName("mn-ai-panel");
        editor = new TemplateContentEditor("Subject", "Email Content", onDirty);

        type.setItems(TemplateTaxonomy.types());
        type.setWidthFull();
        type.addClassName("mn-input");
        subtype.setWidthFull();
        subtype.addClassName("mn-input");
        type.addValueChangeListener(e -> {
            subtype.setItems(TemplateTaxonomy.subtypesOf(e.getValue()));
            subtype.setValue(TemplateTaxonomy.subtypesOf(e.getValue()).get(0));
            onDirty.run();
        });
        type.setValue(TemplateTaxonomy.types().get(0));

        for (var f : new com.vaadin.flow.component.Component[]{name, purpose, instructions}) f.getElement().getClassList().add("mn-input");
        name.setWidthFull();
        name.setPlaceholder("e.g., Interview Invitation");
        name.setRequiredIndicatorVisible(true);
        purpose.setWidthFull();
        purpose.setPlaceholder("e.g., Invite a candidate to a first interview");
        instructions.setWidthFull();
        instructions.setMinHeight("96px");
        instructions.setPlaceholder("Tone, must-have details, placeholders to include…");
        instructions.setHelperText("The more context you give, the better the first draft.");
        name.addValueChangeListener(e -> { name.setInvalid(false); onDirty.run(); });

        add(stepper, stepBody);
        render();
    }

    public Step getStep() { return step; }
    public boolean isGenerating() { return generating; }
    public String getTemplateName() { return name.getValue() == null || name.getValue().isBlank() ? "Untitled Template" : name.getValue(); }
    public String getType() { return type.getValue(); }
    public String getSubtype() { return subtype.getValue(); }

    /** Advances one step; from DESCRIBE it validates and starts generation. */
    public void next() {
        if (step == Step.TYPE) {
            setStep(Step.DESCRIBE);
        } else if (step == Step.DESCRIBE) {
            if (name.getValue() == null || name.getValue().isBlank()) {
                name.setInvalid(true);
                name.setErrorMessage("Template name is required");
                return;
            }
            generate();
        }
    }

    public void back() {
        if (step == Step.DESCRIBE) setStep(Step.TYPE);
        else if (step == Step.REVIEW) setStep(Step.DESCRIBE);
    }

    private void generate() {
        if (generating) return; // prevent duplicate requests
        generating = true;
        error = null;
        setStep(Step.LOADING);
        UI ui = UI.getCurrent();
        String t = type.getValue(), s = subtype.getValue(), n = name.getValue(),
                p = purpose.getValue(), i = instructions.getValue();
        CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(1400); } catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
            return service.generate(t, s, n, p, i);
        }).whenComplete((draft, ex) -> ui.access(() -> {
            generating = false;
            if (ex != null || draft == null) {
                error = "We couldn't generate the email. Please try again.";
                setStep(Step.DESCRIBE);
            } else {
                editor.setSubject(draft.subject());
                editor.setBodyHtml(draft.body());
                setStep(Step.REVIEW);
            }
        }));
    }

    private void setStep(Step s) {
        step = s;
        render();
        onStepChange.run();
    }

    private void render() {
        stepper.removeAll();
        stepBody.removeAll();
        stepper.setVisible(step != Step.REVIEW);
        int current = step == Step.TYPE ? 0 : step == Step.DESCRIBE ? 1 : 2;
        String[] labels = {"Type & Subtype", "Describe", "Review"};
        for (int i = 0; i < 3; i++) {
            if (i > 0) stepper.add(Ui.div("mn-step-line" + (i <= current ? " mn-step-line-done" : "")));
            Div dot = Ui.div("mn-step-dot", Ui.span(i < current ? "✓" : String.valueOf(i + 1), ""));
            if (i == current) dot.addClassName("mn-step-dot-active");
            else if (i < current) dot.addClassName("mn-step-dot-done");
            Div st = Ui.div("mn-step", dot, Ui.span(labels[i], i == current ? "mn-step-label mn-step-label-active" : "mn-step-label"));
            stepper.add(st);
        }
        switch (step) {
            case TYPE -> stepBody.add(hero("What type of template do you need?"),
                    Ui.div("mn-ai-fields", Ui.div("mn-field", Ui.span("Type", "mn-field-label"), type),
                            Ui.div("mn-field", Ui.span("Subtype", "mn-field-label"), subtype)));
            case DESCRIBE -> {
                stepBody.add(hero("Describe what this email should say"));
                if (error != null) stepBody.add(Ui.div("mn-error", Ui.icon(VaadinIcon.WARNING, "14px"), Ui.span(error, "")));
                stepBody.add(Ui.div("mn-ai-fields", name, purpose, instructions));
            }
            case LOADING -> {
                ProgressBar bar = new ProgressBar();
                bar.setIndeterminate(true);
                bar.addClassName("mn-ai-progress");
                stepBody.add(Ui.div("mn-ai-loading", hero("Generating your email with AI…"), bar,
                        Ui.span("This usually takes a few seconds. You can edit the result afterwards.", "mn-ai-loading-sub")));
            }
            case REVIEW -> stepBody.add(editor);
        }
    }

    private Div hero(String headline) {
        H3 h = new H3(headline);
        h.addClassName("mn-ai-hero-title");
        return Ui.div("mn-ai-hero", Ui.div("mn-ai-hero-icon", Ui.icon(VaadinIcon.MAGIC, "22px")), h);
    }

    /** Current draft as a template (source = AI), ready to save or to open in the manual editor. */
    public EmailTemplate toTemplate(Visibility visibility) {
        return new EmailTemplate(getTemplateName(), type.getValue(), subtype.getValue(),
                editor.getSubject(), editor.getBodyHtml(), TemplateSource.AI, visibility);
    }
}
