package com.mailnova.view.campaign.steps;

import com.mailnova.DTO.CompanyProfileDto;
import com.mailnova.DTO.EmailCampaignDto;
import com.mailnova.enums.CampaignType;
import com.mailnova.service.CompanyProfileService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Step 1 — Campaign Details. Blue theme.
 * Fields: Campaign Name, Description, Company, Sender Name, Sender Email,
 * Reply-To Email, Campaign Tags, Campaign Type. Two columns on
 * desktop/tablet, one column on mobile.
 */
public class DetailsStep extends VerticalLayout {


    ComboBox<CompanyProfileDto> company = new ComboBox<>("Company");

    private EmailCampaignDto emailCampaignDto;
    private CompanyProfileService companyProfileService;
    private Integer userid=1;
    TextField campaignName = new TextField("Campaign Name");
    EmailField senderEmail = new EmailField("Sender Email");
    TextArea description = new TextArea("Description");
    EmailField replyTo = new EmailField("Reply-To Email");
    TextField senderName = new TextField("Sender Name");
    TextField tags = new TextField("Campaign Tags");
    ComboBox<CampaignType> campaignType = new ComboBox<>("Campaign Type");
    List<CompanyProfileDto> companyProfilList=null;

    private static final Logger log = LoggerFactory.getLogger(DetailsStep.class);
    public DetailsStep(CampaignDraft draft, SummaryPanel summary,
                       EmailCampaignDto emailCampaignDto,CompanyProfileService companyProfileService) {

        this.emailCampaignDto = emailCampaignDto;
        this.companyProfileService = companyProfileService;
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

        loadData();
        setData();
    }

    private void setData() {

        if(emailCampaignDto !=null){

            if(emailCampaignDto.getName() !=null && !emailCampaignDto.getName().isBlank()){
                campaignName.setValue(emailCampaignDto.getName());
            }

            if (emailCampaignDto.getDescription() !=null && !emailCampaignDto.getDescription().isBlank()){
                description.setValue(emailCampaignDto.getDescription());
            }

            if (emailCampaignDto.getCompanyId() !=null && companyProfilList != null){
                CompanyProfileDto companyProfileDto =companyProfilList.stream().filter(e->e.getId().equals(emailCampaignDto.getCompanyId()))
                        .findAny().orElse(null);
                if(companyProfileDto !=null){
                    company.setValue(companyProfileDto);

                    if (companyProfileDto.getName() !=null){
                        senderName.setValue(companyProfileDto.getName());
                    }

                    if (companyProfileDto.getFromEmailSendAddress() !=null){
                        senderEmail.setValue(companyProfileDto.getFromEmailSendAddress());
                    }
                }
            }

            if(emailCampaignDto.getRepliedTo() !=null && !emailCampaignDto.getRepliedTo().isBlank()){
                replyTo.setValue(emailCampaignDto.getRepliedTo());
            }

            if (emailCampaignDto.getTags() !=null && !emailCampaignDto.getTags().isBlank()){
                tags.setValue(emailCampaignDto.getTags());
            }

            if (emailCampaignDto.getCampaignType() !=null && !emailCampaignDto.getCampaignType().isBlank()){

                log.info("Campaign type value is :: {}, :: {}", emailCampaignDto.getCampaignType(), CampaignType.valueOf(emailCampaignDto.getCampaignType()));
                campaignType.setValue(CampaignType.valueOf(emailCampaignDto.getCampaignType()));
            }
        }
    }


    private FormLayout buildForm(CampaignDraft draft, SummaryPanel summary) {

        campaignName.setPlaceholder("e.g. Black Friday Sale");
        campaignName.setRequiredIndicatorVisible(true);
        campaignName.setWidthFull();
        campaignName.setValueChangeMode(ValueChangeMode.LAZY);
        campaignName.addValueChangeListener(e -> {
            if(e.getValue() != null) {
                draft.setCampaignName(e.getValue());
                summary.refresh(draft);
                emailCampaignDto.setName(e.getValue());
            }
        });

        description.setPlaceholder("Promotional email campaign for Black Friday.");
        description.setMaxLength(500);
        description.setWidthFull();
        description.addClassName("mn-description-area");
        description.setValueChangeMode(ValueChangeMode.LAZY);
        description.addValueChangeListener(e ->
        {
            if(e.getValue() != null) {
                draft.setDescription(e.getValue());
                emailCampaignDto.setDescription(e.getValue());
            }
        });

      //  company.setItems(List.of("Acme Corporation", "Nova Retail Group", "Skyline Ventures", "Brightpath Inc."));
    //    company.setValue("Acme Corporation");
        company.setWidthFull();
        company.setItemLabelGenerator(CompanyProfileDto::getName);

        company.addValueChangeListener(e -> {

            if(e.getValue() != null) {
                draft.setCompanyProfile(e.getValue().getName());
                summary.refresh(draft);
                emailCampaignDto.setCompanyId(e.getValue().getId());
                if (e.getValue().getName() != null) {
                    senderName.setValue(e.getValue().getName());
                }

                if (e.getValue().getFromEmailSendAddress() != null) {
                    senderEmail.setValue(e.getValue().getFromEmailSendAddress());
                }
            }
        });

        senderName.setReadOnly(true);
        senderName.setPlaceholder("e.g. MailNova Marketing");
        senderName.setWidthFull();
        senderName.addValueChangeListener(e -> {
            if(e.getValue() != null) {
                draft.setSenderName(e.getValue());

            }
        });

        senderEmail.setReadOnly(true);
        senderEmail.setPlaceholder("marketing@acme.com");
        senderEmail.setRequiredIndicatorVisible(true);
        senderEmail.setWidthFull();
        senderEmail.setValueChangeMode(ValueChangeMode.LAZY);
        senderEmail.addValueChangeListener(e -> {
            if(e.getValue() != null) {
                draft.setSenderEmail(e.getValue());
                summary.refresh(draft);
            }
        });


        replyTo.setPlaceholder("support@acme.com");
        replyTo.setWidthFull();
        replyTo.addValueChangeListener(e -> {
            draft.setReplyToEmail(e.getValue());
        });

        tags.setPlaceholder("e.g. sale, seasonal, vip");
        tags.setWidthFull();
        tags.setHelperText("Separate multiple tags with commas");
        tags.addValueChangeListener(e -> {
            if(e.getValue() != null) {
                draft.setTags(e.getValue());
                emailCampaignDto.setTags(e.getValue());
            }
        });

        campaignType.setItems(CampaignType.values());
        campaignType.setWidthFull();
        campaignType.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                draft.setCampaignType(e.getValue().name());
                emailCampaignDto.setCampaignType(e.getValue().name());
            }
        });

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
    private void loadData() {
        companyProfilList = this.companyProfileService.getAllCompanyByCreator(userid);
        if (companyProfilList != null && companyProfilList.size() > 0) {
            company.setItems(companyProfilList);

            if(emailCampaignDto==null || emailCampaignDto.getCompanyId() == null) {
                company.setValue(companyProfilList.get(0));
            }
        }
    }
}
