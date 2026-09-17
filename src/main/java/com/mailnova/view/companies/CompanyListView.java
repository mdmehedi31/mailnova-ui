package com.mailnova.view.companies;

import com.mailnova.DTO.CompanyProfileDto;
import com.mailnova.view.MainLayout;
import com.mailnova.view.service.CompanyProfileService;
import com.mailnova.view.utils.Utils;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * "Companies" screen: a searchable list of companies on the left and a
 * read-only preview of the selected company's profile (with a live
 * email preview) on the right. Mirrors the second reference screenshot.
 */
@Route(value = "companies", layout = MainLayout.class)
@PageTitle("Companies | MailNova")
public class CompanyListView extends VerticalLayout {

    private final CompanyProfileService companyService;
    private final VerticalLayout listContainer = new VerticalLayout();
    private final VerticalLayout detailContainer = new VerticalLayout();

    private CompanyProfileDto selectedCompany;
    private static String lastColor = null;
    private Integer userId=1;

    public CompanyListView(CompanyProfileService companyService) {
        this.companyService = companyService;
        lastColor = null;
        addClassName("mn-page");
        setPadding(true);
        setSpacing(false);
        setWidthFull();
        getStyle().set("max-width", "1400px").set("margin", "0 auto").set("box-sizing", "border-box");

        add(buildPageHeader());

        HorizontalLayout body = new HorizontalLayout();
        body.addClassName("mn-companies-body");
        body.setWidthFull();
        body.setFlexGrow(0, listContainer);
        body.setFlexGrow(1, detailContainer);
        // Structural sizing is set inline (in addition to the CSS classes) so it
        // always wins the cascade, regardless of how Lumo's own layout styles load.
        body.getStyle().set("align-items", "flex-start").set("gap", "1.5rem");

        listContainer.addClassName("mn-list-panel");
        listContainer.setPadding(false);
        listContainer.setSpacing(false);
        listContainer.setWidth("340px");
        listContainer.getStyle().set("flex", "0 0 340px").set("min-width", "340px");

        detailContainer.addClassName("mn-detail-panel");
        detailContainer.setPadding(false);
        detailContainer.setSpacing(false);
        detailContainer.setWidthFull();
        detailContainer.getStyle().set("flex", "1 1 0").set("min-width", "0");

        body.add(listContainer, detailContainer);
        add(body);

        List<CompanyProfileDto> companies = companyService.findAll(this.userId);
        buildList(companies);

        if (!companies.isEmpty()) {
            selectCompany(companies.get(0));
        }
    }

    private HorizontalLayout buildPageHeader() {
        H2 title = new H2("Companies");
        title.addClassName("mn-page-title");

        Paragraph description = new Paragraph(
                "Manage your companies. You can add, edit or switch between your companies to run email campaigns.");
        description.addClassName("mn-page-description");

        VerticalLayout titleBlock = new VerticalLayout(title, description);
        titleBlock.setPadding(false);
        titleBlock.setSpacing(false);
        titleBlock.addClassName("mn-page-title-block");

        Button addCompany = new Button("Add Company", new Icon(VaadinIcon.PLUS));
        addCompany.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addCompany.addClassName("mn-primary-button");

        HorizontalLayout header = new HorizontalLayout(titleBlock, addCompany);
        header.addClassName("mn-page-header");
        header.setWidthFull();
        header.setAlignItems(Alignment.START);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        return header;
    }

    private void buildList(List<CompanyProfileDto> companies) {
        TextField search = new TextField();
        search.setPlaceholder("Search companies...");
        search.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        search.setWidthFull();
        search.addClassName("mn-search-field");
        search.addValueChangeListener(e -> filterList(companies, e.getValue()));

        Div searchWrapper = new Div(search);
        searchWrapper.addClassName("mn-search-wrapper");

        VerticalLayout items = new VerticalLayout();
        items.setPadding(false);
        items.setSpacing(false);
        items.addClassName("mn-list-items");
        items.setId("mn-list-items");

        renderItems(items, companies);

        Div infoBanner = buildInfoBanner(companies.size());

        listContainer.removeAll();
        listContainer.add(searchWrapper, items, infoBanner);
    }

    private void filterList(List<CompanyProfileDto> companies, String filter) {
        VerticalLayout items = (VerticalLayout) listContainer.getChildren()
                .filter(c -> "mn-list-items".equals(c.getId().orElse(null)))
                .findFirst()
                .orElseThrow();
        items.removeAll();
        String normalized = filter == null ? "" : filter.trim().toLowerCase();
        List<CompanyProfileDto> filtered = companies.stream()
                .filter(c -> c.getName().toLowerCase().contains(normalized)
                        || c.getCompanyEmail().toLowerCase().contains(normalized))
                .toList();
        renderItems(items, filtered);
    }

    private void renderItems(VerticalLayout items, List<CompanyProfileDto> companies) {
        for (CompanyProfileDto company : companies) {
            items.add(buildListItem(company));
        }
    }

    private Div buildListItem(CompanyProfileDto company) {
        Div avatar = buildAvatar(company, "mn-list-avatar");

        Span name = new Span(company.getName());
        name.addClassName("mn-list-item-name");

        Span email = new Span(company.getCompanyEmail());
        email.addClassName("mn-list-item-email");

        VerticalLayout textBlock = new VerticalLayout(name, email);
        textBlock.setPadding(false);
        textBlock.setSpacing(false);
        textBlock.addClassName("mn-list-item-text");
        // Force the text column to actually take the remaining row width instead
        // of collapsing to its flex-basis; min-width:0 lets it shrink+ellipsize.
        textBlock.getStyle()
                .set("flex", "1 1 0")
                .set("min-width", "0")
                .set("overflow", "hidden");

        String status = "Active";

        if(company.getStatus() !=null && company.getStatus()==false){
            status="Inactive";
        }

        Span badge = buildStatusBadge(status);

        Icon chevron = new Icon(VaadinIcon.CHEVRON_RIGHT_SMALL);
        chevron.addClassName("mn-list-item-chevron");

        VerticalLayout metaBlock = new VerticalLayout(badge, chevron);
        metaBlock.setPadding(false);
        metaBlock.setSpacing(true);
        metaBlock.setAlignItems(Alignment.END);
        metaBlock.addClassName("mn-list-item-meta");
        metaBlock.getStyle().set("flex", "0 0 auto");

        HorizontalLayout row = new HorizontalLayout(avatar, textBlock, metaBlock);
        row.addClassName("mn-list-item");
        row.setWidthFull();
        row.setAlignItems(Alignment.CENTER);
        row.getStyle().set("gap", "0.75rem");
        row.setFlexGrow(1, textBlock);

        if (selectedCompany != null && selectedCompany.getId().equals(company.getId())) {
            row.addClassName("mn-list-item-selected");
        }

        Div wrapper = new Div(row);
        wrapper.addClassName("mn-list-item-wrapper");
        wrapper.addClickListener(e -> selectCompany(company));
        return wrapper;
    }

    private Div buildInfoBanner(int count) {
        Icon icon = new Icon(VaadinIcon.INFO_CIRCLE_O);
        icon.addClassName("mn-info-icon");

        Span headline = new Span("You have " + count + " companies");
        headline.addClassName("mn-info-headline");

        Span body = new Span("Create more companies to manage different brands and email campaigns separately.");
        body.addClassName("mn-info-body");

        VerticalLayout text = new VerticalLayout(headline, body);
        text.setPadding(false);
        text.setSpacing(false);

        HorizontalLayout content = new HorizontalLayout(icon, text);
        content.addClassName("mn-info-banner-content");
        content.setAlignItems(Alignment.START);

        Div banner = new Div(content);
        banner.addClassName("mn-info-banner");
        return banner;
    }

    private void selectCompany(CompanyProfileDto company) {
        this.selectedCompany = company;
        buildList(companyService.findAll(this.userId));
        renderDetail(company);
    }

    private void renderDetail(CompanyProfileDto company) {
        detailContainer.removeAll();
        detailContainer.add(buildDetailHeader(company), buildTabs(company));
    }

    private Div buildDetailHeader(CompanyProfileDto company) {
        Div avatar = buildAvatar(company, "mn-detail-avatar");

        Span name = new Span(company.getName());
        name.addClassName("mn-detail-name");

        String status = "Active";
        if(company.getStatus() !=null && company.getStatus()==false){
            status="Inactive";
        }
        Span badge = buildStatusBadge(status);

        HorizontalLayout nameRow = new HorizontalLayout(name, badge);
        nameRow.setAlignItems(Alignment.CENTER);
        nameRow.setSpacing(true);

        Span subtitle = new Span(company.getCompanyEmail() + "  •  " + company.getAddress());
        subtitle.addClassName("mn-detail-subtitle");

        VerticalLayout textBlock = new VerticalLayout(nameRow, subtitle);
        textBlock.setPadding(false);
        textBlock.setSpacing(false);

        HorizontalLayout identity = new HorizontalLayout(avatar, textBlock);
        identity.setAlignItems(Alignment.CENTER);
        identity.setSpacing(true);

        Button edit = new Button("Edit", new Icon(VaadinIcon.PENCIL));
        edit.addClassName("mn-secondary-button");
        edit.addClickListener(e -> UI.getCurrent().navigate(
                CompanyProfileEditView.class, company.getId().toString()));

        Button more = new Button(new Icon(VaadinIcon.ELLIPSIS_DOTS_H));
        more.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        more.addClassName("mn-icon-only-button");

        HorizontalLayout actions = new HorizontalLayout(edit, more);
        actions.setSpacing(true);

        HorizontalLayout header = new HorizontalLayout(identity, actions);
        header.addClassName("mn-detail-header-row");
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);

        Div card = new Div(header);
        card.addClassName("mn-card");
        card.addClassName("mn-detail-header-card");
        return card;
    }

    private VerticalLayout buildTabs(CompanyProfileDto company) {
        Tab profileTab = new Tab("Profile");
        Tab senderTab = new Tab("Sender Details");
        Tab brandingTab = new Tab("Branding");
        Tab domainTab = new Tab("Domain & DNS");
        Tabs tabs = new Tabs(profileTab, senderTab, brandingTab, domainTab);
        tabs.addClassName("mn-tabs");

        Div profilePanel = buildProfileTabContent(company);
        Div placeholderPanel = buildPlaceholderPanel();
        placeholderPanel.setVisible(false);

        tabs.addSelectedChangeListener(e -> {
            boolean isProfile = tabs.getSelectedTab() == profileTab;
            profilePanel.setVisible(isProfile);
            placeholderPanel.setVisible(!isProfile);
        });

        VerticalLayout wrapper = new VerticalLayout(tabs, profilePanel, placeholderPanel);
        wrapper.setPadding(false);
        wrapper.setSpacing(false);
        wrapper.addClassName("mn-tabs-wrapper");
        return wrapper;
    }

    private Div buildPlaceholderPanel() {
        Paragraph text = new Paragraph("This section is not part of the current design and can be built out later.");
        text.addClassName("mn-placeholder-text");
        Div panel = new Div(text);
        panel.addClassName("mn-card");
        panel.addClassName("mn-placeholder-panel");
        return panel;
    }

    private Div buildProfileTabContent(CompanyProfileDto company) {
        HorizontalLayout columns = new HorizontalLayout();
        columns.addClassName("mn-profile-columns");
        columns.setWidthFull();
        columns.getStyle().set("align-items", "flex-start").set("gap", "1.25rem");

        VerticalLayout leftColumn = new VerticalLayout(buildInfoCard(company), buildLogoCard(company));
        leftColumn.setPadding(false);
        leftColumn.addClassName("mn-profile-left-column");
        leftColumn.getStyle().set("flex", "3 1 0").set("min-width", "0").set("width", "auto");

        Div previewCard = CompanyPreviewBuilder.build(company);
        previewCard.getStyle().set("flex", "2 1 0").set("min-width", "0").set("width", "auto");

        columns.add(leftColumn, previewCard);
        columns.setFlexGrow(3, leftColumn);
        columns.setFlexGrow(2, previewCard);

        Div wrapper = new Div(columns);
        wrapper.addClassName("mn-profile-tab-content");
        return wrapper;
    }

    private Div buildInfoCard(CompanyProfileDto company) {
        H3 title = new H3("Company Information");
        title.addClassName("mn-card-title");
        Paragraph subtitle = new Paragraph("Basic information about your company.");
        subtitle.addClassName("mn-card-subtitle");

        VerticalLayout rows = new VerticalLayout();
        rows.setPadding(false);
        rows.setSpacing(false);
        rows.addClassName("mn-info-rows");
        rows.add(infoRow("Company Name", company.getName()!=null?company.getName():""));
        rows.add(infoRow("Company Email", company.getCompanyEmail()!=null?company.getCompanyEmail():""));
        rows.add(infoRow("Email Sending Address", company.getFromEmailSendAddress()));
        rows.add(infoRow("Contact Email", company.getContactEmail()!=null?company.getContactEmail():""));
        rows.add(infoRow("Phone Number", company.getPhoneNo()!=null?company.getPhoneNo():""));
        rows.add(infoRow("Website", company.getWebsite()!=null?company.getWebsite():""));
        rows.add(infoRow("Address", company.getAddress() !=null?company.getAddress().replace("\n", ", "):""));

        Div card = new Div(title, subtitle, rows);
        card.addClassName("mn-card");
        return card;
    }

    private HorizontalLayout infoRow(String label, String value) {
        Span labelSpan = new Span(label);
        labelSpan.addClassName("mn-info-row-label");

        Span valueSpan = new Span(value);
        valueSpan.addClassName("mn-info-row-value");

        HorizontalLayout row = new HorizontalLayout(labelSpan, valueSpan);
        row.addClassName("mn-info-row");
        row.setWidthFull();
        row.setAlignItems(Alignment.START);
        return row;
    }

    private Div buildLogoCard(CompanyProfileDto company) {
        H3 title = new H3("Company Logo");
        title.addClassName("mn-card-title");

        Div logoBox = new Div();
        logoBox.addClassName("mn-logo-preview-box");
        Div badge = buildAvatar(company, "mn-logo-preview-avatar");
        Span name = new Span("MailNova");
        name.addClassName("mn-logo-preview-name");
        logoBox.add(badge, name);

        Div card = new Div(title, logoBox);
        card.addClassName("mn-card");
        card.addClassName("mn-logo-card");
        return card;
    }

    static Div buildAvatar(CompanyProfileDto company, String extraClass) {
        Div avatar = new Div();
        avatar.addClassName("mn-avatar");
        avatar.addClassName(extraClass);
        String colorCode = getRandomColor();
        if(company.getColorCode() !=null){
            colorCode = company.getColorCode();
        }
        avatar.getStyle().set("background",colorCode);
        avatar.add(new Icon(VaadinIcon.ENVELOPE_O));
        return avatar;
    }

    public static String getRandomColor() {

        List<String> availableColors = Utils.colors.stream()
                .filter(color -> !color.equals(lastColor))
                .toList();

        String selectedColor = availableColors.get(
                ThreadLocalRandom.current().nextInt(availableColors.size())
        );

        lastColor = selectedColor;

        return selectedColor;
    }
    static Span buildStatusBadge(String status) {
        Span badge = new Span(status);
        badge.addClassName("mn-badge");
        badge.addClassName(status.equalsIgnoreCase("Active") ? "mn-badge-active" : "mn-badge-inactive");
        return badge;
    }
}
