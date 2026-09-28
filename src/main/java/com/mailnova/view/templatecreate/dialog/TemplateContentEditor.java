package com.mailnova.view.templatecreate.dialog;

import com.mailnova.view.templatecreate.Ui;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.richtexteditor.RichTextEditor;
import com.vaadin.flow.component.textfield.TextField;
import org.jsoup.Jsoup;

/**
 * THE editor: subject + Vaadin RichTextEditor (bold/italic/underline/strike, alignment, lists,
 * links, undo/redo). Manual and AI-generated templates both render into this same component.
 */
public class TemplateContentEditor extends Div {

    private final TextField subject = new TextField();
    private final RichTextEditor editor = new RichTextEditor();
    private final HasValue<HasValue.ValueChangeEvent<String>, String> html = editor.asHtml();
    private final com.vaadin.flow.component.html.Span count = Ui.span("0 characters", "mn-rte-count");

    /** @param subjectLabel label above the subject field ("Email Subject" / "Subject") */
    public TemplateContentEditor(String subjectLabel, String bodyLabel, Runnable onChange) {
        addClassName("mn-content-editor");
        subject.setWidthFull();
        subject.setPlaceholder("e.g. Interview for {{jobTitle}} - {{companyName}}");
        subject.addClassName("mn-input");
        subject.addValueChangeListener(e -> onChange.run());

        editor.setWidthFull();
        editor.addClassName("mn-rte");
        html.addValueChangeListener(e -> {
            count.setText(Jsoup.parse(e.getValue() == null ? "" : e.getValue()).text().length() + " characters");
            onChange.run();
        });

        Div rteFooter = Ui.div("mn-rte-foot", Ui.span("Use {{placeholders}} for dynamic content", "mn-rte-hint"), count);
        add(Ui.span(subjectLabel, "mn-field-label"), subject);
        if (bodyLabel != null) add(Ui.span(bodyLabel, "mn-field-label"));
        add(Ui.div("mn-rte-wrap", editor, rteFooter));
    }

    public String getSubject() { return subject.getValue(); }

    public void setSubject(String s) { subject.setValue(s == null ? "" : s); }

    public String getBodyHtml() { return html.getValue(); }

    public void setBodyHtml(String h) { html.setValue(h == null ? "" : h); }
}
