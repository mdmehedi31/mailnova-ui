package com.mailnova.view.campaign.steps;

import com.mailnova.view.campaign.components.SectionHeader;
import com.mailnova.view.campaign.components.SummaryPanel;
import com.mailnova.view.campaign.model.CampaignDraft;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.List;

/**
 * Step 1 — Campaign Details. Blue theme.
 * Fields: Campaign Name, Description, Company, Sender Name, Sender Email,
 * Reply-To Email, Campaign Tags, Campaign Type. Two columns on
 * desktop/tablet, one column on mobile.
 */
public class DetailsStep extends VerticalLayout {

    public DetailsStep(CampaignDraft draft, SummaryPanel summary) {
        addClassName("mn-step-content");
        setPadding(false);
        setSpacing(false);
        setWidthFull();

        add(new SectionHeader(
                VaadinIcon.ENVELOPE, "mn-accent-blue",
                "Campaign Details",
                "Give your campaign a name and brief description.",
                false));

        add(buildForm(draft, summary));
    }

    private FormLayout buildForm(CampaignDraft draft, SummaryPanel summary) {
        TextField campaignName = new TextField("Campaign Name");
        campaignName.setPlaceholder("e.g. Black Friday Sale");
        campaignName.setRequiredIndicatorVisible(true);
        campaignName.setWidthFull();
        campaignName.setValueChangeMode(ValueChangeMode.LAZY);
        campaignName.addValueChangeListener(e -> {
            draft.setCampaignName(e.getValue());
            summary.refresh(draft);
        });

        TextArea description = new TextArea("Description");
        description.setPlaceholder("Promotional email campaign for Black Friday.");
        description.setMaxLength(500);
        description.setWidthFull();
        description.addClassName("mn-description-area");
        description.setValueChangeMode(ValueChangeMode.LAZY);
        description.addValueChangeListener(e -> draft.setDescription(e.getValue()));

        ComboBox<String> company = new ComboBox<>("Company");
        company.setItems(List.of("Acme Corporation", "Nova Retail Group", "Skyline Ventures", "Brightpath Inc."));
        company.setValue("Acme Corporation");
        company.setWidthFull();
        company.addValueChangeListener(e -> {
            draft.setCompanyProfile(e.getValue());
            summary.refresh(draft);
        });

        TextField senderName = new TextField("Sender Name");
        senderName.setPlaceholder("e.g. MailNova Marketing");
        senderName.setWidthFull();
        senderName.addValueChangeListener(e -> draft.setSenderName(e.getValue()));

        EmailField senderEmail = new EmailField("Sender Email");
        senderEmail.setPlaceholder("marketing@acme.com");
        senderEmail.setRequiredIndicatorVisible(true);
        senderEmail.setWidthFull();
        senderEmail.setValueChangeMode(ValueChangeMode.LAZY);
        senderEmail.addValueChangeListener(e -> {
            draft.setSenderEmail(e.getValue());
            summary.refresh(draft);
        });

        EmailField replyTo = new EmailField("Reply-To Email");
        replyTo.setPlaceholder("support@acme.com");
        replyTo.setWidthFull();
        replyTo.addValueChangeListener(e -> draft.setReplyToEmail(e.getValue()));

        TextField tags = new TextField("Campaign Tags");
        tags.setPlaceholder("e.g. sale, seasonal, vip");
        tags.setWidthFull();
        tags.setHelperText("Separate multiple tags with commas");
        tags.addValueChangeListener(e -> draft.setTags(e.getValue()));

        ComboBox<String> campaignType = new ComboBox<>("Campaign Type");
        campaignType.setItems(List.of("Promotional", "Newsletter", "Transactional", "Announcement", "Re-engagement"));
        campaignType.setWidthFull();
        campaignType.addValueChangeListener(e -> draft.setCampaignType(e.getValue()));

        FormLayout form = new FormLayout();
        form.addClassName("mn-form");
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("640px", 2)
        );
        form.add(campaignName, 2);
        form.add(description, 2);
        form.add(company, senderName);
        form.add(senderEmail, replyTo);
        form.add(tags, campaignType);
        return form;
    }
}
