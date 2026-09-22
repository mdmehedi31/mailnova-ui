package com.mailnova.view.campaign.steps;

import com.mailnova.view.campaign.components.SectionHeader;
import com.mailnova.view.campaign.components.SummaryPanel;
import com.mailnova.view.campaign.model.CampaignDraft;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.timepicker.TimePicker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Step 4 — Schedule &amp; Review. Orange theme.
 * Send Now / Schedule for Later toggle, conditional date/time/timezone
 * pickers, additional settings, and a confirmation banner. The detailed
 * review data (campaign name, company, recipients, template, schedule)
 * lives in the shared {@link SummaryPanel} on the right, plus a small
 * content-preview card appended beneath it.
 */
public class ScheduleReviewStep extends VerticalLayout {

    private final Div scheduleFields = new Div();
    private final Div infoBox = new Div();
    private final Span infoText = new Span();
    private final CampaignDraft draft;
    public ScheduleReviewStep(CampaignDraft draft, SummaryPanel summary) {

        this.draft = draft;

        addClassName("mn-step-content");
        setPadding(false);
        setSpacing(false);
        setWidthFull();

        SectionHeader header= new SectionHeader(
                VaadinIcon.CALENDAR, "mn-accent-orange",
                "Schedule & Review",
                "Choose when to send your campaign and review all details before scheduling.",
                false);
        header.getStyle().setPaddingBottom("10px");
        add(header);

        add(buildScheduleOptions()/*buildScheduleOptions(draft, summary)*/);
       // add(buildAdditionalSettings());
        add(buildConfirmationBanner());

        summary.setExtra(buildContentPreviewCard(draft));
        summary.refresh(draft);
    }

    private Div buildScheduleOptions() {
        Div panel = new Div();
        panel.addClassName("mn-schedule-panel");
        H4 title = new H4("Schedule Options");
        title.addClassName("mn-panel-title");

        RadioButtonGroup<String> sendChoice = new RadioButtonGroup<>();
        sendChoice.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        sendChoice.setItems("Send Now", "Schedule for Later");
        sendChoice.setValue("Schedule for Later");
        sendChoice.addClassName("mn-radio-group");

        Div laterFields = new Div();
        laterFields.addClassName("mn-later-fields");

        DatePicker date = new DatePicker("Date");
        date.setRequiredIndicatorVisible(true);
        date.setValue(LocalDate.of(2026, 9, 25));
        date.setWidthFull();

        TimePicker time = new TimePicker("Time");
        time.setRequiredIndicatorVisible(true);
        time.setValue(LocalTime.of(16, 0));
        time.setWidthFull();

        ComboBox<String> timezone = new ComboBox<>("Timezone");
        timezone.setItems("America/New_York (EST)", "Asia/Dhaka (GMT+6)", "Europe/London (GMT)",
                "America/Los_Angeles (PST)", "Asia/Tokyo (JST)");
        timezone.setValue("America/New_York (EST)");
        timezone.setWidthFull();
        timezone.addClassName("mn-timezone-select");

        FlexLayout dateTimeRow = new FlexLayout(date, time);
        dateTimeRow.addClassName("mn-datetime-row");
        dateTimeRow.setFlexGrow(1, date);
        dateTimeRow.setFlexGrow(1, time);

        laterFields.add(dateTimeRow, timezone);

        Div scheduleNote = new Div();
        scheduleNote.addClassName("mn-schedule-note");
        Icon clock = new Icon(VaadinIcon.CLOCK);
        Span noteText = new Span("Your campaign will be sent on September 25, 2026 at 4:00 PM (Eastern Time).");
        noteText.addClassName("mn-schedule-note-text");
        scheduleNote.add(clock, noteText);
        laterFields.add(scheduleNote);

        sendChoice.addValueChangeListener(e -> {
            boolean isLater = "Schedule for Later".equals(e.getValue());
            laterFields.setVisible(isLater);
            draft.setSendNow(!isLater);
        });

        Div additional = new Div();
        additional.addClassName("mn-additional-options");
        H4 addTitle = new H4("Additional Settings");
        addTitle.addClassName("mn-panel-title");

        //Div testRow = toggleRow("Send a test email to myself"/*, checked -> draft.setSendTestEmail(checked)*/);
        //Div draftRow = toggleRow("Save as draft"/*, checked -> draft.setSaveAsDraft(checked)*/);

       // additional.add(addTitle, testRow, draftRow);

        panel.add(title, sendChoice, laterFields/*, additional*/);

        date.addValueChangeListener(e -> draft.setScheduleDate(e.getValue()));
        time.addValueChangeListener(e -> draft.setScheduleTime(e.getValue()));
        timezone.addValueChangeListener(e -> draft.setTimezone(e.getValue()));
        draft.setScheduleDate(date.getValue());
        draft.setScheduleTime(time.getValue());
        draft.setTimezone(timezone.getValue());
        draft.setSendNow(false);

        return panel;
    }

    private Component buildScheduleOptions(CampaignDraft draft, SummaryPanel summary) {
        H5 title = new H5("Schedule Options");
        title.addClassName("mn-schedule-title");

        RadioButtonGroup<String> options = new RadioButtonGroup<>();
        options.setItems("Send Now", "Schedule for Later");
        options.setValue(draft.isSendNow() ? "Send Now" : "Schedule for Later");
        options.addClassName("mn-schedule-radio");

        DatePicker date = new DatePicker("Date");
        date.setRequiredIndicatorVisible(true);
        date.setValue(LocalDate.now().plusDays(3));
        date.setWidthFull();

        TimePicker time = new TimePicker("Time");
        time.setRequiredIndicatorVisible(true);
        time.setValue(LocalTime.of(16, 0));
        time.setWidthFull();

        ComboBox<String> timezone = new ComboBox<>("Timezone");
        timezone.setItems(
                "America/New_York (EST)",
                "America/Los_Angeles (PST)",
                "Asia/Dhaka (BST)",
                "Europe/London (GMT)",
                "Asia/Tokyo (JST)"
        );
        timezone.setValue("America/New_York (EST)");
        timezone.setWidthFull();

        scheduleFields.addClassName("mn-schedule-fields");
        HorizontalLayout dateTimeRow = new HorizontalLayout(date, time);
        dateTimeRow.addClassName("mn-schedule-datetime-row");
        dateTimeRow.setWidthFull();
        VerticalLayout scheduleFieldsContent = new VerticalLayout(dateTimeRow, timezone);
        scheduleFieldsContent.setPadding(false);
        scheduleFieldsContent.setSpacing(true);
        scheduleFields.add(scheduleFieldsContent);

        Icon clock = VaadinIcon.CLOCK.create();
        clock.addClassName("mn-info-icon");
        infoText.addClassName("mn-schedule-info-text");
        HorizontalLayout infoRow = new HorizontalLayout(clock, infoText);
        infoRow.setAlignItems(Alignment.CENTER);
        infoRow.setSpacing(true);
        infoBox.addClassName("mn-schedule-info-box");
        infoBox.add(infoRow);

        Runnable syncDraft = () -> {
            boolean sendNow = "Send Now".equals(options.getValue());
            draft.setSendNow(sendNow);
            draft.setScheduleDate(date.getValue());
            draft.setScheduleTime(time.getValue());
            draft.setTimezone(timezone.getValue());
            scheduleFields.setVisible(!sendNow);
            infoBox.setVisible(!sendNow);
            updateInfoText(date.getValue(), time.getValue(), timezone.getValue());
            summary.refresh(draft);
        };

        options.addValueChangeListener(e -> syncDraft.run());
        date.addValueChangeListener(e -> syncDraft.run());
        time.addValueChangeListener(e -> syncDraft.run());
        timezone.addValueChangeListener(e -> syncDraft.run());
        syncDraft.run();

        VerticalLayout content = new VerticalLayout(title, options, scheduleFields, infoBox);
        content.addClassName("mn-schedule-content");
        content.setPadding(false);
        content.setSpacing(true);

        Div card = new Div(content);
        card.addClassName("mn-card");
        card.addClassName("mn-schedule-card");
        return card;
    }

    private void updateInfoText(LocalDate date, LocalTime time, String timezone) {
        if (date == null || time == null) {
            infoText.setText("Choose a date and time to schedule your campaign.");
            return;
        }
        String month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int hour = time.getHour() % 12 == 0 ? 12 : time.getHour() % 12;
        String ampm = time.getHour() < 12 ? "AM" : "PM";
        String tzLong = timezone == null ? "" : timezone.replaceAll("\\s*\\(.*\\)", "");
        infoText.setText(String.format("Your campaign will be sent on %s %d, %d at %02d:%02d %s (%s Time).",
                month, date.getDayOfMonth(), date.getYear(), hour, time.getMinute(), ampm, tzLong));
    }

    private Component buildAdditionalSettings() {
        H5 title = new H5("Additional Settings");
        title.addClassName("mn-schedule-title");

        Component testEmailRow = toggleRow("Send a test email to myself");
        Component draftRow = toggleRow("Save as draft");

        VerticalLayout content = new VerticalLayout(title, testEmailRow, draftRow);
        content.addClassName("mn-additional-settings");
        content.setPadding(false);
        content.setSpacing(true);

        Div card = new Div(content);
        card.addClassName("mn-card");
        card.addClassName("mn-additional-settings-card");
        return card;
    }

    private Component toggleRow(String label) {
        Span text = new Span(label);
        text.addClassName("mn-toggle-label");

        com.vaadin.flow.component.checkbox.Checkbox toggle = new com.vaadin.flow.component.checkbox.Checkbox();
        toggle.addClassName("mn-toggle-switch");

        HorizontalLayout row = new HorizontalLayout(text, toggle);
        row.addClassName("mn-toggle-row");
        row.setWidthFull();
        row.setAlignItems(Alignment.CENTER);
        row.setFlexGrow(1, text);
        return row;
    }

    private Component buildConfirmationBanner() {
        Icon check = VaadinIcon.CHECK_CIRCLE.create();
        check.addClassName("mn-confirmation-icon");

        H4 title = new H4("Everything looks good!");
        title.addClassName("mn-confirmation-title");
        Span sub = new Span("Your campaign is ready to be scheduled. You can always review or edit it before it's sent.");
        sub.addClassName("mn-confirmation-subtitle");

        VerticalLayout text = new VerticalLayout(title, sub);
        text.setPadding(false);
        text.setSpacing(false);

        HorizontalLayout banner = new HorizontalLayout(check, text);
        banner.addClassName("mn-confirmation-banner");
        banner.setAlignItems(Alignment.CENTER);
        banner.setSpacing(true);
        banner.setWidthFull();
        return banner;
    }

    private Div buildContentPreviewCard(CampaignDraft draft) {
        H5 title = new H5("Content Preview");
        title.addClassName("mn-content-preview-title");

        Icon logoIcon = VaadinIcon.ENVELOPE.create();
        logoIcon.addClassName("mn-preview-logo-icon");
        Span logoText = new Span("MailNova");
        logoText.addClassName("mn-preview-logo-text");
        HorizontalLayout logo = new HorizontalLayout(logoIcon, logoText);
        logo.addClassName("mn-preview-logo");
        logo.setAlignItems(Alignment.CENTER);

        H4 heading = new H4("Black Friday Sale");
        heading.addClassName("mn-mini-preview-heading");
        Span body = new Span("Save up to 50% this Friday");
        body.addClassName("mn-mini-preview-body");

        VerticalLayout thumb = new VerticalLayout(logo, heading, body);
        thumb.addClassName("mn-mini-preview-thumb");
        thumb.setPadding(false);
        thumb.setSpacing(false);
        thumb.setAlignItems(Alignment.CENTER);

        Anchor viewDetails = new Anchor("#", "View Content Details →");
        viewDetails.addClassName("mn-content-preview-link");

        VerticalLayout content = new VerticalLayout(title, thumb, viewDetails);
        content.addClassName("mn-content-preview-content");
        content.setPadding(false);
        content.setSpacing(true);

        Div card = new Div(content);
        card.setWidthFull();
        card.addClassName("mn-card");
        card.addClassName("mn-content-preview-card");
        return card;
    }
}
