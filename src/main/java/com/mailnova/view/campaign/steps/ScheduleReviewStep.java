package com.mailnova.view.campaign.steps;

import com.mailnova.DTO.EmailCampaignDto;
import com.mailnova.enums.SendChoice;
import com.mailnova.view.components.SectionHeader;
import com.mailnova.view.components.SummaryPanel;
import com.mailnova.view.campaign.model.CampaignDraft;
import com.mailnova.view.utils.Utils;
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
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Slf4j
public class ScheduleReviewStep extends VerticalLayout {

  /*  private final Div scheduleFields = new Div();
    private final Div infoBox = new Div();
    private final Span infoText = new Span();*/
    private final CampaignDraft draft;
    private EmailCampaignDto emailCampaignDto;
    DatePicker date = new DatePicker("Date");
    TimePicker time = new TimePicker("Time");
    ComboBox<String> timezone = new ComboBox<>("Timezone");
    private SummaryPanel summaryPanel;
    public ScheduleReviewStep(CampaignDraft draft, SummaryPanel summary, EmailCampaignDto emailCampaignDto) {

        this.draft = draft;
        this.emailCampaignDto = emailCampaignDto;
        this.summaryPanel = summary;

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

        RadioButtonGroup<SendChoice> sendChoice = new RadioButtonGroup<>();
        sendChoice.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        sendChoice.setItems(SendChoice.values());
        sendChoice.setItemLabelGenerator(SendChoice::getLabel);
        sendChoice.addClassName("mn-radio-group");
        Div laterFields = new Div();
        laterFields.addClassName("mn-later-fields");

        if(draft.isSendNow() ) {
            sendChoice.setValue(SendChoice.SEND_NO);
        }else{
            sendChoice.setValue(SendChoice.SCHEDULE_FOR_LATER);
            laterFields.setVisible(true);
        }

        date.setRequiredIndicatorVisible(true);
        date.setValue(LocalDate.now().plusDays(10));
        date.setWidthFull();
        time.setRequiredIndicatorVisible(true);
        time.setValue(LocalTime.of(16, 0));
        time.setWidthFull();

        /*timezone.setItems("America/New_York (EST)", "Asia/Dhaka (GMT+6)", "Europe/London (GMT)",
                "America/Los_Angeles (PST)", "Asia/Tokyo (JST)");*/
        List<String> timeZones =getTimeZones();

        timezone.setItems(timeZones);
        timezone.setValue(timeZones.get(0));
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
        Span noteText = new Span();
        noteText.addClassName("mn-schedule-note-text");
        scheduleNote.add(clock, noteText);
        laterFields.add(scheduleNote);

        emailCampaignDto.setSendScheduleDate(Instant.now().plus(3, ChronoUnit.MINUTES));

       // draft.setSendNow(true);
        sendChoice.addValueChangeListener(e -> {

            log.info("Selected send choice: {}", e.getValue());

            if(e.getValue().equals(SendChoice.SCHEDULE_FOR_LATER)) {
                log.info("Selected send choice for later campaign");
                laterFields.setVisible(true);
                draft.setSendNow(false);
                draft.setScheduleDate(LocalDate.now().plusDays(10));
                draft.setScheduleTime(LocalTime.of(16, 0));
                summaryPanel.refresh(draft);

            }else{
                log.info("Selected send choice for now campaign");
                laterFields.setVisible(false);
                draft.setSendNow(true);
                draft.setScheduleDate(LocalDate.now());
                draft.setScheduleTime(LocalTime.now().plus(5, ChronoUnit.MINUTES));
                summaryPanel.refresh(draft);
            }
            buildInfoInformation(draft.getScheduleDate(), draft.getScheduleTime(),draft.getTimezone(),noteText);
        });

        Div additional = new Div();
        additional.addClassName("mn-additional-options");
        H4 addTitle = new H4("Additional Settings");
        addTitle.addClassName("mn-panel-title");

        //Div testRow = toggleRow("Send a test email to myself"/*, checked -> draft.setSendTestEmail(checked)*/);
        //Div draftRow = toggleRow("Save as draft"/*, checked -> draft.setSaveAsDraft(checked)*/);

       // additional.add(addTitle, testRow, draftRow);

        panel.add(title, sendChoice, laterFields/*, additional*/);
        date.setI18n(new DatePicker.DatePickerI18n().setDateFormat("dd.MM.yyyy"));
        date.setLocale(new Locale("en", "US"));

        date.addValueChangeListener(e -> {
            log.info("Selected date: {}", e.getValue());
            draft.setScheduleDate(e.getValue());
            summaryPanel.refresh(draft);
            setScheduleData();
            buildInfoInformation(draft.getScheduleDate(), draft.getScheduleTime(),draft.getTimezone(),noteText);
        });

        time.addValueChangeListener(e ->{
            log.info("Selected time: {}", e.getValue());
            draft.setScheduleTime(e.getValue());
            summaryPanel.refresh(draft);
            setScheduleData();
            buildInfoInformation(draft.getScheduleDate(), draft.getScheduleTime(),draft.getTimezone(),noteText);

        });
        timezone.addValueChangeListener(e ->{
            draft.setTimezone(e.getValue());
                setScheduleData();
            buildInfoInformation(draft.getScheduleDate(), draft.getScheduleTime(),draft.getTimezone(),noteText);
        });
        if (sendChoice.getValue()!=null && sendChoice.getValue() == SendChoice.SEND_NO) {
            laterFields.setVisible(false);
            draft.setScheduleDate(LocalDate.now());
            draft.setScheduleTime(LocalTime.now().plus(30, ChronoUnit.MINUTES));
        }
        draft.setScheduleDate(date.getValue());
        draft.setScheduleTime(time.getValue());
        draft.setTimezone(timezone.getValue());
        buildInfoInformation(draft.getScheduleDate(), draft.getScheduleTime(),timezone.getValue(),noteText);
        return panel;
    }

    public List<String> getTimeZones(){

        List<String> zones = ZoneId.getAvailableZoneIds()
                .stream()
                .filter(id -> id.contains("/"))
                .filter(id -> !id.startsWith("Etc/"))
                .filter(id -> !id.startsWith("SystemV/"))
                .sorted()
                .toList();
        return zones;
    }

    private void buildInfoInformation(LocalDate localDate, LocalTime localTime, String timeZone, Span scheduleNote) {

        if (date == null || time == null) {
            scheduleNote.setText("Choose a date and time to schedule your campaign.");
            return;
        }
        String month = localDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        int hour = localTime.getHour() % 12 == 0 ? 12 : localTime.getHour() % 12;
        String ampm = localTime.getHour() < 12 ? "AM" : "PM";
        String tzLong = timezone == null ? "" : timeZone.replaceAll("\\s*\\(.*\\)", "");
        scheduleNote.setText(String.format("Your campaign will be sent on %s %d, %d at %02d:%02d %s (%s Time).",
                month, localDate.getDayOfMonth(), localDate.getYear(), hour, localTime.getMinute(), ampm, tzLong));

    }
    private void setScheduleData(){

        if(date.getValue() != null && time.getValue() != null && timezone.getValue() != null) {

            Instant scheduleDate = Utils.buildEmailScheduleTime(date.getValue(),time.getValue(),timezone.getValue());
            emailCampaignDto.setSendScheduleDate(scheduleDate);
            emailCampaignDto.setTimezone(timezone.getValue());
        }


    }

  /*
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
    }*/

   /* private Component toggleRow(String label) {
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
    }*/

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
