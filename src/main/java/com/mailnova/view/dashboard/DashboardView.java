package com.mailnova.view.dashboard;

import com.mailnova.view.components.AnalyticsPanel;
import com.mailnova.view.components.CampaignPanel;
import com.mailnova.view.components.CompanySelector;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * The single-page MailNova dashboard: top nav, company selector,
 * campaign table and campaign analytics, in that order.
 */
@Route("dashboard")
@PageTitle("MailNova — Dashboard")
public class DashboardView extends VerticalLayout {

    public DashboardView(DashboardDataService dataService) {
        setPadding(false);
        setSpacing(false);
        setWidthFull();
        getStyle().set("background", "var(--mn-bg)");


        Div page = new Div();
        page.addClassName("mn-page");

        CompanySelector companySelector = new CompanySelector(dataService.getCompanies());
        companySelector.selectFirstByDefault();

        CampaignPanel campaignPanel = new CampaignPanel(
                dataService.getCampaigns(),
                companySelector.getSelectedCompany().name());

        AnalyticsPanel analyticsPanel = new AnalyticsPanel(dataService.getRecipients());

        companySelector.addCompanySelectedListener(e ->
                campaignPanel.setCompanyName(e.getCompany().name()));

        page.add(companySelector, campaignPanel, analyticsPanel);
        add(page);

        Footer footer = new Footer();
        footer.addClassName("mn-footer");
        footer.setText("© 2024 MailNova. All rights reserved.");
        add(footer);
    }
}
