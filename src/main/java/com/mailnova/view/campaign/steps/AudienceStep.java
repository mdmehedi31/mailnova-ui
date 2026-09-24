package com.mailnova.view.campaign.steps;


import com.mailnova.DTO.EmailCampaignDto;
import com.mailnova.service.CompanyProfileService;
import com.mailnova.view.campaign.components.SectionHeader;
import com.mailnova.view.campaign.components.SummaryPanel;
import com.mailnova.view.campaign.model.CampaignDraft;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.regex.Pattern;

/**
 * Step 2 — Audience Selection. Emerald green theme.
 * Two ways to build the recipient list: import an Excel/CSV file, or paste
 * email addresses manually. Both sections are always visible (matching the
 * reference design), joined by an "OR" divider, with a live recipient count.
 */
public class AudienceStep extends VerticalLayout {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");

    private final Span totalRecipientsValue = new Span("0 recipients");
    private final CampaignDraft draft;
    private final SummaryPanel summary;
    private EmailCampaignDto emailCampaignDto;
    public AudienceStep(CampaignDraft draft, SummaryPanel summary, EmailCampaignDto emailCampaignDto) {

        this.emailCampaignDto = emailCampaignDto;
        this.draft = draft;
        this.summary = summary;

        addClassName("mn-step-content");
        setPadding(false);
        setSpacing(false);
        setWidthFull();

        add(new SectionHeader(
                VaadinIcon.USERS, "mn-accent-green",
                "Audience",
                "Select your recipient list or add recipients manually.",
                false));

        add(buildMethodTabs());
        add(buildImportRow());
        add(buildDivider("OR"));
        add(buildManualEntry());
        add(buildTotalRecipients());

        summary.setExtra(buildQuickTips());
    }

    private Component buildMethodTabs() {
        Button importTab = new Button("Import from Excel", VaadinIcon.UPLOAD.create());
        importTab.addClassName("mn-tab-button");
        importTab.addClassName("mn-tab-button-active");

        Button manualTab = new Button("Add Manually", VaadinIcon.USER.create());
        manualTab.addClassName("mn-tab-button");

        HorizontalLayout tabs = new HorizontalLayout(importTab, manualTab);
        tabs.addClassName("mn-tabs");
        tabs.setSpacing(true);
        tabs.setWidthFull();
        return tabs;
    }

    private Component buildImportRow() {
        Div uploadCard = buildUploadCard();
        Div requirementsCard = buildRequirementsCard();

        HorizontalLayout row = new HorizontalLayout(uploadCard, requirementsCard);
        row.addClassName("mn-import-row");
        row.setWidthFull();
        row.setSpacing(true);
        row.setFlexGrow(1, uploadCard);
        row.setFlexGrow(1, requirementsCard);
        return row;
    }

    private Div buildUploadCard() {
        Icon cloud = VaadinIcon.CLOUD_UPLOAD_O.create();
        cloud.addClassName("mn-upload-icon");

        H4 title = new H4("Drag & drop your Excel file here");
        title.addClassName("mn-upload-title");

        Span hint = new Span("Supports .xlsx and .xls files (max 10MB)");
        hint.addClassName("mn-upload-hint");

      //  Upload buffer = new MemoryBuffer();
        Upload upload = new Upload();
        upload.setAcceptedFileTypes(".xlsx", ".xls", ".csv");
        upload.setMaxFiles(1);
        upload.setDropAllowed(true);
        upload.setUploadButton(new Button("Choose File"));
        upload.addClassName("mn-upload-widget");
        upload.getElement().setAttribute("aria-label", "Choose File");

      //  upload.addSucceededListener(e -> simulateParsedImport());

        VerticalLayout content = new VerticalLayout(cloud, title, hint, upload);
        content.addClassName("mn-upload-content");
        content.setAlignItems(Alignment.CENTER);
        content.setSpacing(false);
        content.setPadding(false);

        Div card = new Div(content);
        card.addClassName("mn-upload-card");
        return card;
    }

    private Div buildRequirementsCard() {
        H4 title = new H4("Excel File Requirements");
        title.addClassName("mn-requirements-title");

        UnorderedList list = new UnorderedList();
        list.addClassName("mn-requirements-list");
        list.add(requirement("First row should contain headers"));
        list.add(requirement("Email column is required"));
        list.add(requirement("Max file size: 10MB"));
        list.add(requirement("Supports .xlsx and .xls formats"));

        Anchor download = new Anchor("#", "Download sample file");
        download.addClassName("mn-download-link");
        Icon downloadIcon = VaadinIcon.DOWNLOAD.create();
        downloadIcon.addClassName("mn-download-icon");
        HorizontalLayout downloadRow = new HorizontalLayout(downloadIcon, download);
        downloadRow.setSpacing(true);
        downloadRow.addClassName("mn-download-row");

        VerticalLayout content = new VerticalLayout(title, list, downloadRow);
        content.addClassName("mn-requirements-content");
        content.setPadding(false);
        content.setSpacing(true);

        Div card = new Div(content);
        card.addClassName("mn-requirements-card");
        return card;
    }

    private ListItem requirement(String text) {
        Icon check = VaadinIcon.CHECK.create();
        check.addClassName("mn-requirement-check");
        ListItem item = new ListItem(check, new Span(text));
        item.addClassName("mn-requirement-item");
        return item;
    }

    private Component buildDivider(String label) {
        Div line1 = new Div();
        line1.addClassName("mn-divider-line");
        Div line2 = new Div();
        line2.addClassName("mn-divider-line");
        Span text = new Span(label);
        text.addClassName("mn-divider-label");

        HorizontalLayout divider = new HorizontalLayout(line1, text, line2);
        divider.addClassName("mn-divider");
        divider.setAlignItems(Alignment.CENTER);
        divider.setWidthFull();
        divider.setFlexGrow(1, line1);
        divider.setFlexGrow(1, line2);
        return divider;
    }

    private Component buildManualEntry() {
        H4 title = new H4("Add Emails Manually");
        title.addClassName("mn-manual-title");
        Span subtitle = new Span("Enter email addresses separated by comma or one per line.");
        subtitle.addClassName("mn-manual-subtitle");

        TextArea emails = new TextArea();
        emails.setPlaceholder("e.g. john@example.com, sarah@example.com");
        emails.setWidthFull();
        emails.addClassName("mn-manual-textarea");
        emails.setMaxLength(1000);
        emails.setValueChangeMode(ValueChangeMode.LAZY);
        emails.addValueChangeListener(e -> parseManualEmails(e.getValue()));

        Icon infoIcon = VaadinIcon.INFO_CIRCLE_O.create();
        infoIcon.addClassName("mn-info-icon");
        Span infoText = new Span("You can add multiple emails, separated by commas or new lines.");
        infoText.addClassName("mn-info-text");
        HorizontalLayout infoBox = new HorizontalLayout(infoIcon, infoText);
        infoBox.addClassName("mn-info-box");
        infoBox.setAlignItems(Alignment.CENTER);
        infoBox.setSpacing(true);

        VerticalLayout section = new VerticalLayout(title, subtitle, emails, infoBox);
        section.addClassName("mn-manual-section");
        section.setPadding(false);
        section.setSpacing(true);
        return section;
    }

    private Component buildTotalRecipients() {
        Icon icon = VaadinIcon.USERS.create();
        icon.addClassName("mn-total-icon");
        Div iconBadge = new Div(icon);
        iconBadge.addClassName("mn-total-icon-badge");

        Span label = new Span("Total Recipients");
        label.addClassName("mn-total-label");
        totalRecipientsValue.addClassName("mn-total-value");

        VerticalLayout text = new VerticalLayout(label, totalRecipientsValue);
        text.setPadding(false);
        text.setSpacing(false);

        HorizontalLayout row = new HorizontalLayout(iconBadge, text);
        row.addClassName("mn-total-row");
        row.setAlignItems(Alignment.CENTER);
        row.setSpacing(true);
        return row;
    }

    private Component buildQuickTips() {
        Icon bulb = VaadinIcon.LIGHTBULB.create();
        bulb.addClassName("mn-tips-icon");
        H4 title = new H4("Quick Tips");
        title.addClassName("mn-tips-title");
        HorizontalLayout header = new HorizontalLayout(bulb, title);
        header.setAlignItems(Alignment.CENTER);
        header.setSpacing(true);

        UnorderedList list = new UnorderedList();
        list.addClassName("mn-tips-list");
        list.add(new ListItem(new Span("Use a clean Excel file with proper headers.")));
        list.add(new ListItem(new Span("Make sure your email list is valid and up to date.")));
        list.add(new ListItem(new Span("You can always add or remove recipients later.")));

        VerticalLayout content = new VerticalLayout(header, list);
        content.addClassName("mn-tips-content");
        content.setPadding(false);
        content.setSpacing(true);

        Div card = new Div(content);
        card.addClassName("mn-tips-card");
        return card;
    }

    // ------------------------------------------------------------------
    // Data handling
    // ------------------------------------------------------------------

    private void simulateParsedImport() {
        // Placeholder for a real Apache POI / CSV parse. Populates the
        // draft with representative counts so the rest of the wizard
        // (summary, review) reflects an imported list.
        draft.setTotalRows(1240);
        draft.setValidEmails(1198);
        draft.setInvalidEmails(42);
        refreshCounts();
    }

    private void parseManualEmails(String rawText) {
        if (rawText == null || rawText.isBlank()) {
            draft.setTotalRows(0);
            draft.setValidEmails(0);
            draft.setInvalidEmails(0);
            refreshCounts();
            return;
        }
        String[] tokens = rawText.split("[,\\n]+");
        int valid = 0;
        int invalid = 0;
        for (String token : tokens) {
            String email = token.trim();
            if (email.isEmpty()) continue;
            if (EMAIL_PATTERN.matcher(email).matches()) {
                valid++;
            } else {
                invalid++;
            }
        }
        draft.setTotalRows(valid + invalid);
        draft.setValidEmails(valid);
        draft.setInvalidEmails(invalid);
        refreshCounts();
    }

    private void refreshCounts() {
        totalRecipientsValue.setText(draft.getTotalRows() + " recipients");
        summary.refresh(draft);
    }
}
