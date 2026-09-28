package com.mailnova.view.components;

import com.mailnova.view.dashboard.CampaignData;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.List;

/**
 * "Campaigns — {Company}" panel: header with status filters and search,
 * the campaign data grid, and pagination footer.
 */
public class CampaignPanel extends Div {

    private final H2 title;
    private final Grid<CampaignData> grid = new Grid<>();
    private List<CampaignData> allCampaigns;

    public CampaignPanel(List<CampaignData> campaigns, String companyName) {
        this.allCampaigns = campaigns;

        addClassName("mn-panel");
        getStyle().set("margin-top", "32px");

        title = new H2();
        add(buildHeader(companyName, campaigns.size()));
        add(buildGrid(campaigns));
        add(buildFooter(campaigns.size()));
    }

    private FlexLayout buildHeader(String companyName, int total) {
        Icon icon = VaadinIcon.ENVELOPES_O.create();
        Div iconWrap = new Div(icon);
        iconWrap.addClassName("mn-panel-icon");
        iconWrap.getStyle().set("display", "flex");

        title.setText("Campaigns — " + companyName);
        Paragraph subtitle = new Paragraph(total + " total campaigns · updated 5 minutes ago");

        Div titleBlock = new Div(title, subtitle);
        titleBlock.addClassName("mn-panel-title");

        HorizontalLayout titleRow = new HorizontalLayout(iconWrap, titleBlock);
        titleRow.addClassName("mn-panel-title-row");
        titleRow.setAlignItems(HorizontalLayout.Alignment.START);

        HorizontalLayout segmented = buildStatusFilter();

        TextField search = new TextField();
        search.setPlaceholder("Filter campaigns");
        search.setPrefixComponent(VaadinIcon.SEARCH.create());
        search.setWidth("200px");
        search.addValueChangeListener(e -> filter(e.getValue()));

        HorizontalLayout controls = new HorizontalLayout(segmented, search);
        controls.addClassName("mn-panel-controls");
        controls.setAlignItems(HorizontalLayout.Alignment.CENTER);

        FlexLayout header = new FlexLayout(titleRow, controls);
        header.addClassName("mn-panel-head");
        header.setWidthFull();
        return header;
    }

    private HorizontalLayout buildStatusFilter() {
        Button all = new Button("All");
        Button running = new Button("Running");
        Button draft = new Button("Draft");
        Button completed = new Button("Completed");

        Button[] buttons = {all, running, draft, completed};
        for (Button b : buttons) {
            b.getElement().getThemeList().add("tertiary");
        }
        all.addClassName("active");

        all.addClickListener(e -> { setActive(buttons, all); applyStatusFilter(null); });
        running.addClickListener(e -> { setActive(buttons, running); applyStatusFilter(CampaignData.CampaignStatus.RUNNING); });
        draft.addClickListener(e -> { setActive(buttons, draft); applyStatusFilter(CampaignData.CampaignStatus.DRAFT); });
        completed.addClickListener(e -> { setActive(buttons, completed); applyStatusFilter(CampaignData.CampaignStatus.COMPLETED); });

        HorizontalLayout segmented = new HorizontalLayout(buttons);
        segmented.addClassName("mn-segmented");
        return segmented;
    }

    private void setActive(Button[] buttons, Button active) {
        for (Button b : buttons) {
            b.removeClassName("active");
        }
        active.addClassName("active");
    }

    private void applyStatusFilter(CampaignData.CampaignStatus status) {
        List<CampaignData> filtered = status == null
                ? allCampaigns
                : allCampaigns.stream().filter(c -> c.status() == status).toList();
        grid.setItems(filtered);
    }

    private void filter(String text) {
        if (text == null || text.isBlank()) {
            grid.setItems(allCampaigns);
            return;
        }
        String needle = text.toLowerCase();
        grid.setItems(allCampaigns.stream()
                .filter(c -> c.name().toLowerCase().contains(needle))
                .toList());
    }

    private Grid<CampaignData> buildGrid(List<CampaignData> campaigns) {
        grid.addClassName("mn-grid");
        grid.setItems(campaigns);
        grid.setAllRowsVisible(true);
        grid.setWidthFull();

        grid.addColumn(new ComponentRenderer<>(this::renderNameCell))
                .setHeader("Campaign Name").setAutoWidth(true).setFlexGrow(1);

        grid.addColumn(new ComponentRenderer<>(this::renderStatusBadge))
                .setHeader("Status").setAutoWidth(true);

        grid.addColumn(c -> c.sent()).setHeader("Sent").setAutoWidth(true);
        grid.addColumn(c -> c.delivered()).setHeader("Delivered").setAutoWidth(true);

        grid.addColumn(new ComponentRenderer<>(this::renderOpenRate))
                .setHeader("Open Rate").setAutoWidth(true);

        grid.addColumn(c -> c.clickRate()).setHeader("Click Rate").setAutoWidth(true);

        grid.addColumn(new ComponentRenderer<>(this::renderFailed))
                .setHeader("Failed").setAutoWidth(true);

        Div wrapper = new Div(grid);
        wrapper.getStyle().set("overflow-x", "auto");
        add(wrapper);
        return grid;
    }

    private HorizontalLayout renderNameCell(CampaignData campaign) {
        Icon icon = switch (campaign.type()) {
            case PROMO -> VaadinIcon.BULLSEYE.create();
            case NEWSLETTER -> VaadinIcon.ENVELOPE_OPEN_O.create();
            case DRAFT -> VaadinIcon.FILE_TEXT_O.create();
        };
        icon.setSize("15px");

        Div iconWrap = new Div(icon);
        iconWrap.addClassName("mn-campaign-type-icon");
        iconWrap.addClassName(switch (campaign.type()) {
            case PROMO -> "promo";
            case NEWSLETTER -> "newsletter";
            case DRAFT -> "draft-type";
        });
        iconWrap.getStyle().set("display", "flex");

        Span name = new Span(campaign.name());
        name.addClassName("mn-campaign-name-text");

        HorizontalLayout cell = new HorizontalLayout(iconWrap, name);
        cell.addClassName("mn-campaign-name-cell");
        cell.setAlignItems(HorizontalLayout.Alignment.CENTER);
        return cell;
    }

    private Span renderStatusBadge(CampaignData campaign) {
        Span badge = new Span(campaign.status().label());
        badge.addClassName("mn-badge");
        badge.addClassName(campaign.status().styleClass());
        return badge;
    }

    private Div renderOpenRate(CampaignData campaign) {
        if ("—".equals(campaign.openRate())) {
            Span dash = new Span("—");
            dash.addClassName("mn-muted");
            return new Div(dash);
        }
        Span value = new Span(campaign.openRate());
        value.addClassName("mn-rate-value");

        Div fill = new Div();
        fill.addClassName("mn-rate-fill");
        fill.getStyle().set("width", campaign.openRatePercent() + "%");

        Div track = new Div(fill);
        track.addClassName("mn-rate-track");

        HorizontalLayout cell = new HorizontalLayout(value, track);
        cell.addClassName("mn-rate-cell");
        cell.setAlignItems(HorizontalLayout.Alignment.CENTER);
        return new Div(cell);
    }

    private Span renderFailed(CampaignData campaign) {
        Span span = new Span(campaign.failed());
        if ("—".equals(campaign.failed())) {
            span.addClassName("mn-muted");
        } else {
            span.addClassName("mn-failed-cell");
            span.addClassName(campaign.failedHigh() ? "high" : "low");
        }
        return span;
    }

    private HorizontalLayout buildFooter(int total) {
        int shown = Math.min(7, total);
        Span showing = new Span();
        showing.getElement().setProperty("innerHTML",
                "Showing <strong>1–" + shown + "</strong> of " + total + " campaigns");
        showing.addClassName("mn-showing");

        Button prev = new Button(VaadinIcon.ANGLE_LEFT.create());
        prev.getElement().getThemeList().add("tertiary");
        Button page1 = new Button("1");
        page1.getElement().getThemeList().add("primary");
        Button page2 = new Button("2");
        page2.getElement().getThemeList().add("tertiary");
        Button next = new Button(VaadinIcon.ANGLE_RIGHT.create());
        next.getElement().getThemeList().add("tertiary");

        HorizontalLayout pagination = new HorizontalLayout(prev, page1, page2, next);
        pagination.setAlignItems(HorizontalLayout.Alignment.CENTER);

        HorizontalLayout footer = new HorizontalLayout(showing, pagination);
        footer.addClassName("mn-table-footer");
        footer.setWidthFull();
        footer.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);
        footer.setAlignItems(HorizontalLayout.Alignment.CENTER);
        return footer;
    }

    /** Updates the panel title when a different company is selected. */
    public void setCompanyName(String companyName) {
        title.setText("Campaigns — " + companyName);
    }
}
