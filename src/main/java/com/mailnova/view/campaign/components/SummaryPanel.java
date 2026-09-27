package com.mailnova.view.campaign.components;


import com.mailnova.view.campaign.model.CampaignDraft;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Shared "Campaign Summary" card shown on the right of every wizard step.
 * A single instance is reused across Details / Audience / Content /
 * Schedule &amp; Review; only its row values and (optionally) one extra
 * slot underneath (Quick Tips on Audience, Content Preview on Schedule &amp;
 * Review) change between steps. It always stays in the right column and
 * never moves under the main content.
 */
public class SummaryPanel extends VerticalLayout {

    private final Span campaignNameValue = new Span("—");
    private final Span companyValue = new Span("—");
    private final Span fromEmailValue = new Span("—");
    private final Span recipientsValue = new Span("—");
    private final Span scheduleValue = new Span("—");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM dd, yyyy hh:mm a");

    private static final Logger log = LoggerFactory.getLogger(SummaryPanel.class);
    private final VerticalLayout extraSlot = new VerticalLayout();

    public SummaryPanel() {
        addClassName("mn-summary-panel");
        setPadding(false);
        setSpacing(false);
        setWidthFull();

        add(buildCard());

        extraSlot.setPadding(false);
        extraSlot.setSpacing(true);
        extraSlot.addClassName("mn-summary-extra");
        add(extraSlot);
    }

    private Component buildCard() {
        H3 title = new H3("Campaign Summary");
        title.addClassName("mn-summary-title");

        Span hint = new Span("Your campaign details will appear here.");
        hint.addClassName("mn-summary-hint");

        VerticalLayout header = new VerticalLayout(title, hint);
        header.setPadding(false);
        header.setSpacing(false);
        header.addClassName("mn-summary-header");

        Div divider = new Div();
        divider.addClassName("mn-summary-divider");

        VerticalLayout rows = new VerticalLayout();
        rows.addClassName("mn-summary-rows");
        rows.setPadding(false);
        rows.setSpacing(false);
        rows.add(row(VaadinIcon.ENVELOPE, "mn-accent-indigo", "Campaign Name", campaignNameValue));
        rows.add(row(VaadinIcon.BUILDING, "mn-accent-blue", "Company", companyValue));
        rows.add(row(VaadinIcon.ENVELOPE_O, "mn-accent-orange", "From Email", fromEmailValue));
        rows.add(row(VaadinIcon.USERS, "mn-accent-green", "Recipients", recipientsValue));
        rows.add(row(VaadinIcon.CLOCK, "mn-accent-purple", "Schedule", scheduleValue));

        VerticalLayout card = new VerticalLayout(header, divider, rows);
        card.addClassName("mn-card");
        card.addClassName("mn-summary-card");
        card.setPadding(false);
        card.setSpacing(false);
        return card;
    }

    private Component row(VaadinIcon icon, String accentClass, String label, Span valueSpan) {
        Icon glyph = icon.create();
        glyph.addClassName("mn-summary-row-glyph");
        Div iconBadge = new Div(glyph);
        iconBadge.addClassName("mn-summary-row-icon");
        iconBadge.addClassName(accentClass);

        Span labelSpan = new Span(label);
        labelSpan.addClassName("mn-summary-row-label");
        valueSpan.addClassName("mn-summary-row-value");

        VerticalLayout text = new VerticalLayout(labelSpan, valueSpan);
        text.setPadding(false);
        text.setSpacing(false);

        HorizontalLayout r = new HorizontalLayout(iconBadge, text);
        r.addClassName("mn-summary-row");
        r.setAlignItems(Alignment.CENTER);
        r.setSpacing(true);
        r.setWidthFull();
        return r;
    }

    /** Replace whatever is shown beneath the summary card (Quick Tips, Content Preview, ...). */
    public void setExtra(Component... components) {
        extraSlot.removeAll();
        extraSlot.add(components);
    }

    /** Re-render every row from the current draft state. Call after any field changes. */
    public void refresh(CampaignDraft draft) {
        campaignNameValue.setText(blankToDash(draft.getCampaignName()));
        companyValue.setText(blankToDash(draft.getCompanyProfile()));
        fromEmailValue.setText(blankToDash(draft.getSenderEmail()));
        recipientsValue.setText(0 + " recipients");
        scheduleValue.setText(getScheduleDate(draft));
    }


    public String getScheduleDate(CampaignDraft draft){

        log.info("Send is now :: {}", draft.isSendNow());

        if(draft== null)
            return "";

        if (draft.getScheduleDate() == null && draft.getScheduleTime() == null)
            return "";

        LocalDate date = LocalDate.now();
        LocalTime time =LocalTime.MIDNIGHT;

        if (draft.getScheduleDate() != null) {
            date = draft.getScheduleDate();
        }

        if (draft.getScheduleTime() != null) {
            time = draft.getScheduleTime();
        }

        LocalDateTime dateTime=LocalDateTime.of(date, time);

        if (draft.isSendNow()){
            dateTime= LocalDateTime.now().plusMinutes(5);
            log.info("The time is now {}", dateTime);
        }
        else {
            log.info("The Scheduled date time value is :: {}", dateTime);
        }
        return formatter.format(dateTime);
    }
    private String blankToDash(String value) {
        return (value == null || value.isBlank()) ? "—" : value;
    }
}
