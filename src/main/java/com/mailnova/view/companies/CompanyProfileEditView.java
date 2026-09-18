package com.mailnova.view.companies;


import com.mailnova.DTO.CompanyProfileDto;
import com.mailnova.view.MainLayout;
import com.mailnova.view.service.CompanyProfileService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Route(value = "companies/edit", layout = MainLayout.class)
@PageTitle("Edit Company Profile | MailNova")
public class CompanyProfileEditView extends VerticalLayout implements HasUrlParameter<String> {

    private final CompanyProfileService companyService;

    private CompanyProfileDto company;
    private CompanyPreviewBuilder.Handle previewHandle;

    private final TextField nameField = new TextField("Company Name");
    private final TextField companyEmailField = new TextField("Company Email");
    private final TextField sendingAddressField = new TextField("Email Sending Address");
    private final TextField contactEmailField = new TextField("Contact Email");
    private final TextField phoneField = new TextField("Phone Number");
    private final TextField websiteField = new TextField("Website");
    private final TextArea addressField = new TextArea("Address");
    private final ComboBox<String> timezoneField = new ComboBox<>("Time Zone");

    private Integer userId =1;
    private static Logger log = LoggerFactory.getLogger(CompanyProfileEditView.class);
    public CompanyProfileEditView(CompanyProfileService companyService) {
        this.companyService = companyService;
        addClassName("mn-page");
        setPadding(true);
        setSpacing(false);
        setWidthFull();
        getStyle().set("max-width", "1400px").set("margin", "0 auto").set("box-sizing", "border-box");
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String companyId) {

       // List<CompanyProfileDto> all = companyService.findAll(userId);

        log.info("Company Id is : " + companyId);
        if(companyId != null) {
            Integer comId= Integer.parseInt(companyId);
            this.company = companyService.findById(comId);
        }else{
            this.company = new CompanyProfileDto();
        }


        removeAll();
        add(buildPageHeader());
        add(buildBody());
    }

    private Div buildPageHeader() {
        Div icon = new Div(new Icon(VaadinIcon.OFFICE));
        icon.addClassName("mn-header-icon");

        H2 title = new H2("Company Profile");
        title.addClassName("mn-page-title");

        Paragraph description = new Paragraph(
                "Manage your company information. This information will be used in your email campaigns, "
                        + "sender details and in the footer of your emails.");
        description.addClassName("mn-page-description");

        VerticalLayout textBlock = new VerticalLayout(title, description);
        textBlock.setPadding(false);
        textBlock.setSpacing(false);

        HorizontalLayout titleRow = new HorizontalLayout(icon, textBlock);
        titleRow.addClassName("mn-page-title-row");
        titleRow.setAlignItems(Alignment.START);
        titleRow.setSpacing(true);

        Icon paperPlane = new Icon(VaadinIcon.PAPERPLANE_O);
        paperPlane.addClassName("mn-banner-icon");
        Span bannerText = new Span("Build stronger connections with your audience.");
        bannerText.addClassName("mn-banner-text");
        HorizontalLayout banner = new HorizontalLayout(paperPlane, bannerText);
        banner.addClassName("mn-hero-banner");
        banner.setAlignItems(Alignment.CENTER);
        banner.setSpacing(true);

        HorizontalLayout header = new HorizontalLayout(titleRow, banner);
        header.addClassName("mn-edit-page-header");
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        return new Div(header);
    }

    private HorizontalLayout buildBody() {
        Div formCard = buildFormCard();
        formCard.getStyle().set("flex", "3 1 0").set("min-width", "0").set("width", "auto");

        CompanyPreviewBuilder.Handle handle = new CompanyPreviewBuilder.Handle(company);
        this.previewHandle = handle;
        handle.card.getStyle().set("flex", "2 1 0").set("min-width", "0").set("width", "auto");

        HorizontalLayout body = new HorizontalLayout(formCard, handle.card);
        body.addClassName("mn-edit-body");
        body.setWidthFull();
        body.getStyle().set("align-items", "flex-start").set("gap", "1.25rem");
        body.setFlexGrow(3, formCard);
        body.setFlexGrow(2, handle.card);
        return body;
    }

    private Div buildFormCard() {
        H3 title = new H3("Company Information");
        title.addClassName("mn-card-title");
        Paragraph subtitle = new Paragraph("Update your company details below.");
        subtitle.addClassName("mn-card-subtitle");

        configureFields();

        Div row1 = fieldRow(required(nameField), required(companyEmailField));
        Div row2 = fieldRow(required(sendingAddressField), contactEmailField);
        Div row3 = fieldRow(phoneField, websiteField);

        addressField.setWidthFull();
        addressField.addClassName("mn-address-field");

        Div logoUpload = buildLogoUploadField();
        Div row4 = fieldRow(logoUpload, timezoneField);

        VerticalLayout fields = new VerticalLayout(row1, row2, row3, addressField, row4);
        fields.setPadding(false);
        fields.addClassName("mn-form-fields");

        Button cancel = new Button("Cancel");
        cancel.addClassName("mn-secondary-button");
        cancel.addClickListener(e -> UI.getCurrent().navigate(CompanyListView.class));

        Button save = new Button("Save Changes", new Icon(VaadinIcon.CHECK_SQUARE_O));
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClassName("mn-primary-button");
        save.addClickListener(e -> saveChanges());

        HorizontalLayout actions = new HorizontalLayout(cancel, save);
        actions.addClassName("mn-form-actions");
        actions.setJustifyContentMode(JustifyContentMode.END);
        actions.setWidthFull();

        Hr divider = new Hr();
        divider.addClassName("mn-form-divider");

        Div card = new Div(title, subtitle, fields, divider, actions);
        card.addClassName("mn-card");
        card.addClassName("mn-form-card");
        return card;
    }

    private void configureFields() {
        nameField.setValue(nullSafe(company.getName()));
        companyEmailField.setValue(nullSafe(company.getCompanyEmail()));
        sendingAddressField.setValue(nullSafe(company.getFromEmailSendAddress()));
        contactEmailField.setValue(nullSafe(company.getContactEmail()));
        phoneField.setValue(nullSafe(company.getPhoneNo()));
        websiteField.setValue(nullSafe(company.getWebsite()));
        addressField.setValue(nullSafe(company.getAddress()));

        timezoneField.setItems(
                "(GMT-08:00) America/Los_Angeles",
                "(GMT-05:00) America/New_York",
                "(GMT+00:00) Europe/London",
                "(GMT+06:00) Asia/Dhaka",
                "(GMT+10:00) Australia/Sydney");
        timezoneField.setValue(nullSafe("(GMT+06:00) Asia/Dhaka"));
        timezoneField.setWidthFull();

        for (TextField field : List.of(nameField, companyEmailField, sendingAddressField,
                contactEmailField, phoneField, websiteField)) {
            field.setWidthFull();
        }

        nameField.setPrefixComponent(new Icon(VaadinIcon.OFFICE));
        companyEmailField.setPrefixComponent(new Icon(VaadinIcon.ENVELOPE_O));
        sendingAddressField.setPrefixComponent(new Icon(VaadinIcon.ENVELOPE_O));
        contactEmailField.setPrefixComponent(new Icon(VaadinIcon.ENVELOPE_O));
        phoneField.setPrefixComponent(new Icon(VaadinIcon.PHONE));
        websiteField.setPrefixComponent(new Icon(VaadinIcon.LINK));

        nameField.addValueChangeListener(e -> {
            company.setName(e.getValue());
            previewHandle.update(company);
        });
        companyEmailField.addValueChangeListener(e -> company.setCompanyEmail(e.getValue()));
        sendingAddressField.addValueChangeListener(e -> {
            company.setFromEmailSendAddress(e.getValue());
            previewHandle.update(company);
        });
        contactEmailField.addValueChangeListener(e -> company.setContactEmail(e.getValue()));
        phoneField.addValueChangeListener(e -> {
            company.setPhoneNo(e.getValue());
            previewHandle.update(company);
        });
        websiteField.addValueChangeListener(e -> {
            company.setWebsite(e.getValue());
            previewHandle.update(company);
        });
        addressField.addValueChangeListener(e -> {
            company.setAddress(e.getValue());
            previewHandle.update(company);
        });
      //  timezoneField.addValueChangeListener(e -> company.setTimezone(e.getValue()));
    }

    private Div buildLogoUploadField() {
        H4 label = new H4("Company Logo");
        label.addClassName("mn-form-label");

        Div currentLogo = CompanyListView.buildAvatar(company, "mn-logo-current");
        Span currentLogoName = new Span("MailNova");
        currentLogoName.addClassName("mn-logo-current-name");
        HorizontalLayout currentLogoBox = new HorizontalLayout(currentLogo, currentLogoName);
        currentLogoBox.addClassName("mn-logo-current-box");
        currentLogoBox.setAlignItems(Alignment.CENTER);

        //Buffer buffer = new Buffer();
        Upload upload = new Upload();
        upload.setAcceptedFileTypes("image/svg+xml", "image/png", "image/jpeg");
        upload.setMaxFileSize(2 * 1024 * 1024);
        upload.setDropAllowed(true);
        upload.setWidthFull();
        // Replace Upload's default "Drop files here / Upload..." text with copy
        // that matches the design, instead of hand-building the drop-zone markup.
        Upload i18n = new Upload();
      /*  i18n.setDropFiles(new Upload.I18N.DropFiles()
                .setOne("Click to upload")
                .setMany("Click to upload"));*/
   /*     i18n.setAddFiles(new Upload().setUploadHandler(null)
                .setOne("SVG, PNG or JPG (max 2MB)")
                .setMany("SVG, PNG or JPG (max 2MB)"));*/
     //   upload.setI18n(i18n);
        upload.addClassName("mn-upload");

        HorizontalLayout row = new HorizontalLayout(currentLogoBox, upload);
        row.addClassName("mn-logo-upload-row");
        row.setAlignItems(Alignment.STRETCH);
        row.setWidthFull();

        Div field = new Div(label, row);
        field.addClassName("mn-form-field-group");
        return field;
    }

    private Div fieldRow(com.vaadin.flow.component.Component left, com.vaadin.flow.component.Component right) {
        Div row = new Div(left, right);
        row.addClassName("mn-field-row");
        row.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "1fr 1fr")
                .set("gap", "1.1rem")
                .set("width", "100%");
        return row;
    }

    private TextField required(TextField field) {
        field.addClassName("mn-required-field");
        return field;
    }

    private void saveChanges() {

        if(company!=null) {

            if(company.getId() ==null){
                companyService.update(company);
            }else{
                companyService.save(company);
            }
        }
        Notification notification = Notification.show("Company profile saved.", 3000,
                Notification.Position.TOP_END);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private static String nullSafe(String value) {
        return value == null ? "" : value;
    }
}
