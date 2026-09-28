package com.mailnova.view.campaign;

import com.mailnova.DTO.EmailCampaignDto;
import com.mailnova.service.CompanyProfileService;
import com.mailnova.service.EmailCampaignService;
import com.mailnova.service.EmailTemplateService;
import com.mailnova.view.MainLayout;
import com.mailnova.view.components.SectionHeader;
import com.mailnova.view.components.SummaryPanel;
import com.mailnova.view.components.WizardStepper;
import com.mailnova.view.campaign.model.CampaignDraft;
import com.mailnova.view.campaign.steps.AudienceStep;
import com.mailnova.view.campaign.steps.ContentStep;
import com.mailnova.view.campaign.steps.DetailsStep;
import com.mailnova.view.campaign.steps.ScheduleReviewStep;
import com.mailnova.view.utils.CustomNotification;
import com.mailnova.view.utils.NotificationType;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "campaigns/create", layout = MainLayout.class)
@PageTitle("Create Email Campaign | MailNova")
public class CreateCampaignView extends VerticalLayout {

    private final CampaignDraft draft = new CampaignDraft();
    private final SummaryPanel summaryPanel = new SummaryPanel();
    private  EmailCampaignDto emailCampaignDto = new EmailCampaignDto();
    private final Div wizardSlot = new Div();
    private final Div mainContentSlot = new Div();
    private final EmailCampaignService emailCampaignService;
    private Component[] stepComponents;
    private  Button backBtn = new Button("Back", VaadinIcon.ARROW_LEFT.create());
    private  Button saveDraftBtn = new Button("Save Draft");
    private  Button nextBtn = new Button("Next", VaadinIcon.ARROW_RIGHT.create());
    private CompanyProfileService companyProfileService;
    private EmailTemplateService emailTemplateService;
    private int currentStep = 1;
    private  Integer userId=1;
    public CreateCampaignView(CompanyProfileService companyProfileService, EmailTemplateService emailTemplateService, EmailCampaignService emailCampaignService, EmailCampaignService emailCampaignService1) {
        this.companyProfileService = companyProfileService;
        this.emailTemplateService = emailTemplateService;
        this.emailCampaignService = emailCampaignService1;

        addClassName("mn-page");
        setPadding(false);
        setSpacing(false);
        setWidthFull();

        add(buildBreadcrumb());
        add(buildPageHeader());

        wizardSlot.addClassName("mn-wizard-slot");
        wizardSlot.setWidthFull();
        add(wizardSlot);

        add(buildContentArea());
        add(buildActionBar());

        renderStep();

       // initStepComponents();
    }

  /*  private void initStepComponents() {
        stepComponents = new Component[]{
                new DetailsStep(draft),
                new AudienceStep(draft),
                new ContentStep(draft),
                new ScheduleReviewStep(draft)
        };
    }
*/
    private Component buildBreadcrumb() {
        Icon back = VaadinIcon.ARROW_LEFT.create();
        back.addClassName("mn-breadcrumb-icon");
        Anchor link = new Anchor("campaigns", "Back to Campaigns");
        link.addClassName("mn-breadcrumb-link");
        HorizontalLayout breadcrumb = new HorizontalLayout(back, link);
        breadcrumb.addClassName("mn-breadcrumb");
        breadcrumb.setAlignItems(Alignment.CENTER);
        breadcrumb.setSpacing(true);
        return breadcrumb;
    }

    private Component buildPageHeader() {
        return new SectionHeader(
                VaadinIcon.ENVELOPE, "mn-accent-indigo",
                "Create Email Campaign",
                "Set up your campaign details, choose recipients, design your email and schedule it for delivery.",
                true);
    }

    private Component buildContentArea() {
        mainContentSlot.addClassName("mn-main-content");

        Div mainCard = new Div(mainContentSlot);
        mainCard.addClassName("mn-card");
        mainCard.addClassName("mn-main-card");

        Div summaryColumn = new Div(summaryPanel);
        summaryColumn.addClassName("mn-summary-column");

        HorizontalLayout content = new HorizontalLayout(mainCard, summaryColumn);
        content.addClassName("mn-content-area");
        content.setWidthFull();
        content.setFlexGrow(1, mainCard);
        return content;
    }

    private Component buildActionBar() {
        HorizontalLayout bar = new HorizontalLayout();
        bar.addClassName("mn-action-bar");
        bar.setWidthFull();
        bar.setAlignItems(Alignment.CENTER);

        backBtn = new Button("Back", new Icon(VaadinIcon.ARROW_LEFT));
        backBtn.addClassName("mn-btn-secondary");
        backBtn.addClickListener(e -> {
            if (currentStep > 0) goToStep(currentStep - 1);
        });

        Div spacer = new Div();

        saveDraftBtn = new Button("Save Draft");
        saveDraftBtn.addClassName("mn-btn-secondary");
        saveDraftBtn.addClickListener(e ->
                Notification.show("Campaign saved as draft", 2500, Notification.Position.TOP_END));

        nextBtn = new Button("Next", new Icon(VaadinIcon.ARROW_RIGHT));
        nextBtn.setIconAfterText(true);
        nextBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        nextBtn.addClassName("mn-btn-primary");
        nextBtn.addClickListener(e -> {
            if (currentStep == WizardStepper.STEP_LABELS.length) {


                emailCampaignDto.setCreatedBy(userId);
                EmailCampaignDto saveResponse= emailCampaignService.createEmailCampaign(emailCampaignDto);
                if (saveResponse != null) {

                    if(saveResponse.getMessage() !=null && !saveResponse.getMessage().isBlank()) {
                        CustomNotification.warning(saveResponse.getMessage(), NotificationType.ERROR.name());
                    }else {
                        emailCampaignDto= new EmailCampaignDto();
                        CustomNotification.success("Campaign created successfully!", NotificationType.SUCCESS.name());
                    }
                }else{
                    emailCampaignDto= new EmailCampaignDto();
                    CustomNotification.warning("Campaign creation failed!", NotificationType.ERROR.name());

                }
            } else {
                if (isValid()) {
                    goToStep(currentStep + 1);
                }
            }
        });

        /*scheduleBtn = new Button("Schedule Campaign", new Icon(VaadinIcon.PAPERPLANE));
        scheduleBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        scheduleBtn.addClassName("mn-btn-primary");
        scheduleBtn.addClickListener(e ->
                Notification.show("Campaign scheduled successfully!", 3000, Notification.Position.TOP_END));
*/
        bar.add(backBtn, spacer, saveDraftBtn, nextBtn/*, scheduleBtn*/);
        bar.setFlexGrow(1, spacer);
        return bar;
    }

    private boolean isValid() {

        if (currentStep == 1) {
            if (emailCampaignDto.getName() == null || emailCampaignDto.getName().isBlank()) {
                CustomNotification.warning("Required Fields Missing", "Email campaign name is missing");
                return false;
            }

            if (draft.getSenderEmail() == null || draft.getSenderEmail().isBlank()) {
                CustomNotification.warning("Required Fields Missing", "Sender email is missing");
                return false;
            }
        }
        return true;
    }

    private void goToStep(int step) {
        if (step < 1 || step > WizardStepper.STEP_LABELS.length) {
            return;
        }
        currentStep = step;
        renderStep();
    }

    private void renderStep() {
        wizardSlot.removeAll();
        wizardSlot.add(new WizardStepper(currentStep));

        mainContentSlot.removeAll();
        mainContentSlot.add(switch (currentStep) {
            case 1 -> new DetailsStep(draft, summaryPanel, emailCampaignDto, companyProfileService);
            case 2 -> new AudienceStep(draft, summaryPanel,emailCampaignDto);
            case 3 -> new ContentStep(draft, summaryPanel,emailCampaignDto,emailTemplateService,companyProfileService);
            default -> new ScheduleReviewStep(draft, summaryPanel,emailCampaignDto);
        });

        backBtn.setEnabled(currentStep > 1);
        boolean lastStep = currentStep == WizardStepper.STEP_LABELS.length;
        nextBtn.setText(lastStep ? "Schedule Campaign" : "Next");
        nextBtn.setIcon(lastStep ? VaadinIcon.PAPERPLANE.create() : VaadinIcon.ARROW_RIGHT.create());
        nextBtn.addClassName(lastStep ? "mn-btn-schedule" : "mn-btn-next");
    }
}
