package com.mailnova.view.campaign.steps;

import com.mailnova.view.campaign.components.SectionHeader;
import com.mailnova.view.campaign.components.SummaryPanel;
import com.mailnova.view.campaign.model.CampaignDraft;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.List;

/**
 * Step 3 — Email Content. Purple theme.
 * Template selection, subject/preview text fields, and a live desktop/mobile
 * email preview alongside template metadata.
 */
public class ContentStep extends VerticalLayout {

    private final Div previewFrame = new Div();

    public ContentStep(CampaignDraft draft, SummaryPanel summary) {
        addClassName("mn-step-content");
        setPadding(false);
        setSpacing(false);
        setWidthFull();

        add(new SectionHeader(
                VaadinIcon.FILE_TEXT, "mn-accent-purple",
                "Content",
                "Design your email content and select a template.",
                false));

        add(buildTemplateModeTabs());
        add(buildTemplateFields(draft, summary));
        add(buildPreviewSection());

        draft.setTemplateName("Black Friday Promotion");
        summary.refresh(draft);
    }

    private Component buildTemplateModeTabs() {
        Button existing = new Button("Use Existing Template", VaadinIcon.FILE_TEXT_O.create());
        existing.addClassName("mn-tab-button");
        existing.addClassName("mn-tab-button-active");

        Button create = new Button("Create New Template", VaadinIcon.PLUS.create());
        create.addClassName("mn-tab-button");

        HorizontalLayout tabs = new HorizontalLayout(existing, create);
        tabs.addClassName("mn-tabs");
        tabs.setSpacing(true);
        tabs.setWidthFull();
        return tabs;
    }

    private Component buildTemplateFields(CampaignDraft draft, SummaryPanel summary) {
        ComboBox<String> template = new ComboBox<>("Email Template");
        template.setItems(List.of("Black Friday Promotion", "Welcome Series", "Monthly Newsletter", "Cart Recovery"));
        template.setValue("Black Friday Promotion");
        template.addClassName("mn-template-select");
        template.setWidthFull();
        template.addValueChangeListener(e -> {
            draft.setTemplateName(e.getValue());
            summary.refresh(draft);
        });

        TextField subject = new TextField("Email Subject");
        subject.setPlaceholder("Don't miss our Black Friday deals!");
        subject.setRequiredIndicatorVisible(true);
        subject.setMaxLength(100);
        subject.setWidthFull();
        subject.setValueChangeMode(ValueChangeMode.LAZY);
        subject.addValueChangeListener(e -> draft.setEmailSubject(e.getValue()));
        subject.setHelperText("0/100");
        subject.addValueChangeListener(e ->
                subject.setHelperText(e.getValue().length() + "/100"));

        TextField previewText = new TextField("Preview Text");
        previewText.setPlaceholder("Save up to 50% this Friday...");
        previewText.setMaxLength(90);
        previewText.setWidthFull();
        previewText.setValueChangeMode(ValueChangeMode.LAZY);
        previewText.addValueChangeListener(e -> draft.setPreviewText(e.getValue()));
        previewText.setHelperText("0/90");
        previewText.addValueChangeListener(e ->
                previewText.setHelperText(e.getValue().length() + "/90"));

        VerticalLayout fields = new VerticalLayout(template, subject, previewText);
        fields.addClassName("mn-content-fields");
        fields.setPadding(false);
        fields.setSpacing(true);
        return fields;
    }

    private Component buildPreviewSection() {
        H4 title = new H4("Email Preview");
        title.addClassName("mn-preview-title");

        Button desktopBtn = new Button("Desktop");
        desktopBtn.addClassName("mn-preview-toggle");
        desktopBtn.addClassName("mn-preview-toggle-active");
        Button mobileBtn = new Button("Mobile");
        mobileBtn.addClassName("mn-preview-toggle");

        desktopBtn.addClickListener(e -> {
            previewFrame.removeClassName("mn-preview-mobile");
            desktopBtn.addClassName("mn-preview-toggle-active");
            mobileBtn.removeClassName("mn-preview-toggle-active");
        });
        mobileBtn.addClickListener(e -> {
            previewFrame.addClassName("mn-preview-mobile");
            mobileBtn.addClassName("mn-preview-toggle-active");
            desktopBtn.removeClassName("mn-preview-toggle-active");
        });

        HorizontalLayout toggle = new HorizontalLayout(desktopBtn, mobileBtn);
        toggle.addClassName("mn-preview-toggle-group");

        HorizontalLayout header = new HorizontalLayout(title, toggle);
        header.addClassName("mn-preview-header");
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.setFlexGrow(1, title);

        previewFrame.addClassName("mn-preview-frame");
        previewFrame.add(buildEmailMock());

        Div templateDetails = buildTemplateDetailsCard();

        HorizontalLayout previewRow = new HorizontalLayout(previewFrame, templateDetails);
        previewRow.addClassName("mn-preview-row");
        previewRow.setWidthFull();
        previewRow.setFlexGrow(2, previewFrame);
        previewRow.setFlexGrow(1, templateDetails);

        VerticalLayout section = new VerticalLayout(header, previewRow);
        section.addClassName("mn-preview-section");
        section.setPadding(false);
        section.setSpacing(true);
        return section;
    }

    private Component buildEmailMock() {
        Icon logoIcon = VaadinIcon.ENVELOPE.create();
        logoIcon.addClassName("mn-preview-logo-icon");
        Span logoText = new Span("MailNova");
        logoText.addClassName("mn-preview-logo-text");
        HorizontalLayout logo = new HorizontalLayout(logoIcon, logoText);
        logo.addClassName("mn-preview-logo");
        logo.setAlignItems(Alignment.CENTER);

        H4 heading = new H4("Black Friday Sale");
        heading.addClassName("mn-preview-heading");
        Span body = new Span("Save up to 50% this Friday");
        body.addClassName("mn-preview-body");

        Button cta = new Button("Shop Now");
        cta.addClassName("mn-preview-cta");

        Span footer = new Span("Thanks for being with us!");
        footer.addClassName("mn-preview-footer");

        Icon fb = VaadinIcon.FACEBOOK.create();
        Icon ig = VaadinIcon.INSTITUTION.create();
        Icon tw = VaadinIcon.TWITTER.create();
        for (Icon i : List.of(fb, ig, tw)) {
            i.addClassName("mn-preview-social-icon");
        }
        HorizontalLayout socials = new HorizontalLayout(fb, ig, tw);
        socials.addClassName("mn-preview-socials");

        VerticalLayout mock = new VerticalLayout(logo, heading, body, cta, footer, socials);
        mock.addClassName("mn-preview-mock");
        mock.setPadding(false);
        mock.setSpacing(false);
        mock.setAlignItems(Alignment.CENTER);
        return mock;
    }

    private Div buildTemplateDetailsCard() {
        H5 title = new H5("Template Details");
        title.addClassName("mn-template-details-title");

        VerticalLayout rows = new VerticalLayout();
        rows.setPadding(false);
        rows.setSpacing(true);
        rows.add(detailRow("Name", "Black Friday Promotion"));
        rows.add(detailRow("Type", "Promotional"));
        rows.add(detailRow("Created On", "Sep 10, 2026"));

        Anchor previewFull = new Anchor("#", "Preview Full Template");
        previewFull.addClassName("mn-template-details-link");
        Icon linkIcon = VaadinIcon.EXTERNAL_LINK.create();
        linkIcon.addClassName("mn-template-details-link-icon");
        HorizontalLayout linkRow = new HorizontalLayout(previewFull, linkIcon);
        linkRow.setSpacing(false);
        linkRow.setAlignItems(Alignment.CENTER);

        VerticalLayout content = new VerticalLayout(title, rows, linkRow);
        content.addClassName("mn-template-details-content");
        content.setPadding(false);
        content.setSpacing(true);

        Div card = new Div(content);
        card.addClassName("mn-template-details-card");
        return card;
    }

    private Component detailRow(String label, String value) {
        Span labelSpan = new Span(label);
        labelSpan.addClassName("mn-template-details-label");
        Span valueSpan = new Span(value);
        valueSpan.addClassName("mn-template-details-value");
        VerticalLayout row = new VerticalLayout(labelSpan, valueSpan);
        row.setPadding(false);
        row.setSpacing(false);
        return row;
    }
}
