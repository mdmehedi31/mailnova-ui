package com.mailnova.view.components;


import com.mailnova.view.dashboard.RecipientData;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import java.util.List;

/**
 * The "Campaign Analytics" section: campaign summary header, six KPI cards,
 * a delivery-breakdown donut chart and a recipient activity table.
 */
public class AnalyticsPanel extends Div {

    public AnalyticsPanel(List<RecipientData> recipients) {
        addClassName("mn-panel");
        getStyle().set("margin-top", "24px");

        add(buildSummaryHeader());
        add(buildKpiRow());
        add(buildAnalyticsGrid(recipients));
    }

    private FlexLayout buildSummaryHeader() {
        H2 name = new H2("Summer Sale Blast");
        Span status = new Span("Running");
        status.addClassName("mn-badge");
        status.addClassName("running");

        HorizontalLayout titleRow = new HorizontalLayout(name, status);
        titleRow.addClassName("mn-cs-title-row");
        titleRow.setAlignItems(HorizontalLayout.Alignment.CENTER);

        Icon calendar = VaadinIcon.CALENDAR_O.create();
        calendar.setSize("14px");
        Span dateRange = new Span("Jun 12 – Jun 19, 2024");
        Span dot = new Span("·");
        dot.getStyle().set("color", "#CBD5E1");
        Span sender = new Span("Sent from Nova Retail Co.");

        HorizontalLayout sub = new HorizontalLayout(calendar, dateRange, dot, sender);
        sub.addClassName("mn-cs-sub");
        sub.setAlignItems(HorizontalLayout.Alignment.CENTER);

        VerticalLayout left = new VerticalLayout(titleRow, sub);
        left.setPadding(false);
        left.setSpacing(false);

        Button export = new Button("Export Report", VaadinIcon.DOWNLOAD.create());
        export.addClassName("mn-btn-outline");

        FlexLayout header = new FlexLayout(left, export);
        header.addClassName("mn-campaign-summary-head");
        header.setWidthFull();
        return header;
    }

    private Div buildKpiRow() {
        Div row = new Div();
        row.addClassName("mn-kpi-row");

        row.add(kpiCard(VaadinIcon.PAPERPLANE_O, "sent", "24,500", "Total Sent"));
        row.add(kpiCard(VaadinIcon.CHECK, "delivered", "23,980", "Delivered"));
        row.add(kpiCard(VaadinIcon.ENVELOPE_OPEN_O, "opened", "10,362", "Opened"));
        row.add(kpiCard(VaadinIcon.CURSOR, "clicked", "2,744", "Clicked"));
        row.add(kpiCard(VaadinIcon.WARNING, "bounced", "520", "Bounced"));
        row.add(kpiCard(VaadinIcon.USER_CLOCK, "unsub", "184", "Unsubscribed"));
        return row;
    }

    private Div kpiCard(VaadinIcon icon, String colorClass, String number, String label) {
        Icon i = icon.create();
        i.setSize("16px");
        Div iconWrap = new Div(i);
        iconWrap.addClassName("mn-kpi-icon");
        iconWrap.addClassName(colorClass);
        iconWrap.getStyle().set("display", "flex");

        Span numberSpan = new Span(number);
        numberSpan.addClassName("mn-kpi-number");
        Span labelSpan = new Span(label);
        labelSpan.addClassName("mn-kpi-label");

        Div text = new Div(numberSpan, labelSpan);

        Div card = new Div(iconWrap, text);
        card.addClassName("mn-kpi-card");
        return card;
    }

    private Div buildAnalyticsGrid(List<RecipientData> recipients) {
        Div grid = new Div(buildDonutPanel(), buildRecipientPanel(recipients));
        grid.addClassName("mn-analytics-grid");
        return grid;
    }

    private Div buildDonutPanel() {
        H3 heading = new H3("Delivery Breakdown");
        Paragraph subtitle = new Paragraph("Distribution of send outcomes");
        Div head = new Div(heading, subtitle);
        head.addClassName("mn-subpanel-head");

        DonutChart chart = new DonutChart(97.9, 1.4, 0.7,
                "#2563EB", "#F59E0B", "#EF4444", "97.9%", "Delivered");

        Div legend = new Div(
                legendItem("#2563EB", "Delivered"),
                legendItem("#F59E0B", "Bounced"),
                legendItem("#EF4444", "Failed")
        );
        legend.addClassName("mn-legend");
        legend.getStyle().set("display", "flex");

        VerticalLayout donutWrap = new VerticalLayout(chart, legend);
        donutWrap.addClassName("mn-donut-wrap");
        donutWrap.setAlignItems(VerticalLayout.Alignment.CENTER);
        donutWrap.setPadding(false);

        Div panel = new Div(head, donutWrap);
        panel.addClassName("mn-subpanel");
        return panel;
    }

    private HorizontalLayout legendItem(String color, String label) {
        Div swatch = new Div();
        swatch.addClassName("mn-legend-swatch");
        swatch.getStyle().set("background", color);

        HorizontalLayout item = new HorizontalLayout(swatch, new Span(label));
        item.addClassName("mn-legend-item");
        item.setAlignItems(HorizontalLayout.Alignment.CENTER);
        return item;
    }

    private Div buildRecipientPanel(List<RecipientData> recipients) {
        H3 heading = new H3("Recipient Activity");
        Div headText = new Div(heading);
        headText.addClassName("mn-subpanel-head");

        TextField search = new TextField();
        search.setPlaceholder("Search recipients");
        search.setPrefixComponent(VaadinIcon.SEARCH.create());
        search.setWidth("200px");

        Grid<RecipientData> grid = new Grid<>();
        grid.addClassName("mn-grid");
        grid.setItems(recipients);
        grid.setAllRowsVisible(true);
        grid.setWidthFull();

        grid.addColumn(RecipientData::email).setHeader("Email Address").setAutoWidth(true).setFlexGrow(1);
        grid.addColumn(new ComponentRenderer<>(this::renderStatus)).setHeader("Status").setAutoWidth(true);
        grid.addColumn(RecipientData::lastActivity).setHeader("Last Activity").setAutoWidth(true);

        search.addValueChangeListener(e -> {
            String needle = e.getValue() == null ? "" : e.getValue().toLowerCase();
            if (needle.isBlank()) {
                grid.setItems(recipients);
            } else {
                grid.setItems(recipients.stream()
                        .filter(r -> r.email().toLowerCase().contains(needle))
                        .toList());
            }
        });

        HorizontalLayout head = new HorizontalLayout(headText, search);
        head.addClassName("mn-recipient-head");
        head.setWidthFull();
        head.setAlignItems(HorizontalLayout.Alignment.CENTER);

        Div gridWrap = new Div(grid);
        gridWrap.getStyle().set("overflow-x", "auto");
        gridWrap.getStyle().set("margin-top", "18px");

        Div panel = new Div(head, gridWrap, buildRecipientFooter(recipients.size()));
        panel.addClassName("mn-subpanel");
        return panel;
    }

    private HorizontalLayout renderStatus(RecipientData recipient) {
        Div dot = new Div();
        dot.addClassName("mn-status-dot");

        HorizontalLayout cell = new HorizontalLayout(dot, new Span(recipient.status().label()));
        cell.addClassName("mn-status-dot-cell");
        cell.addClassName(recipient.status().styleClass());
        cell.setAlignItems(HorizontalLayout.Alignment.CENTER);
        return cell;
    }

    private HorizontalLayout buildRecipientFooter(int shownCount) {
        Span showing = new Span();
        showing.getElement().setProperty("innerHTML",
                "Showing <strong>1–" + shownCount + "</strong> of 24,500 recipients");
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
}
