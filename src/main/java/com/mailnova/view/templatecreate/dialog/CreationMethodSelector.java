package com.mailnova.view.templatecreate.dialog;


import com.mailnova.view.campaign.model.TemplateSource;
import com.vaadin.flow.component.html.Div;
import java.util.function.Consumer;

/** Entry point of the dialog: pick Manual or AI Generate. */
public class CreationMethodSelector extends Div {

    private final MethodCard manual;
    private final MethodCard ai;

    public CreationMethodSelector(Consumer<TemplateSource> onChoose) {
        addClassName("mn-method-grid");
        manual = new MethodCard(TemplateSource.MANUAL, "Create Manually",
                "Write the subject and body yourself with the rich text editor.",
                "Full control over layout and copy", () -> choose(TemplateSource.MANUAL, onChoose));
        ai = new MethodCard(TemplateSource.AI, "Generate with AI",
                "Pick a type, describe the email and let AI write the first draft.",
                "You can edit everything afterwards", () -> choose(TemplateSource.AI, onChoose));
        add(manual, ai);
    }

    private void choose(TemplateSource source, Consumer<TemplateSource> onChoose) {
        manual.setSelected(source == TemplateSource.MANUAL);
        ai.setSelected(source == TemplateSource.AI);
        onChoose.accept(source);
    }
}
