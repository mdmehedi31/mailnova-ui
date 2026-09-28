package com.mailnova.view.components;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;

/** Static marketing mockup of the dashboard (not functional). */
public class DashboardPreview extends Div {
    private record Stat(String label, String value, String delta, VaadinIcon icon, String tone) {}
    private static final Stat[] STATS = {
        new Stat("Active Campaigns", "12", "+12% vs last week", VaadinIcon.PAPERPLANE, "green"), new Stat("Delivery Rate", "98.5%", "+0.6%", VaadinIcon.INBOX, "teal"),
        new Stat("Open Rate", "42.3%", "+12%", VaadinIcon.ENVELOPE_OPEN_O, "purple"), new Stat("Click Rate", "12.6%", "+2.4%", VaadinIcon.RSS, "orange"),
        new Stat("Audience Count", "24,582", "+3.2%", VaadinIcon.GROUP, "pink")};
    private static final VaadinIcon[] MENU_ICONS = {VaadinIcon.DASHBOARD, VaadinIcon.PAPERPLANE_O, VaadinIcon.GROUP, VaadinIcon.FILE_TEXT_O, VaadinIcon.BAR_CHART, VaadinIcon.COG, VaadinIcon.CONNECT, VaadinIcon.COGS};
    private static final String[] MENU = {"Dashboard", "Campaigns", "Audience", "Templates", "Analytics", "Automation", "Integrations", "Settings"};
    private static final String[][] ACTIVITY = {{"Spring Newsletter", "Delivered · 2 hours ago", "green"}, {"Product Update", "Opened · 4 hours ago", "blue"},
            {"Welcome Series", "Clicked · 6 hours ago", "purple"}, {"Holiday Campaign", "Completed · 1 day ago", "green"}};
    private static final String CHART = "<svg viewBox='0 0 300 110' preserveAspectRatio='none' class='mn-dash__svg'><defs>"
        + "<linearGradient id='mnArea' x1='0' y1='0' x2='0' y2='1'><stop offset='0' stop-color='#6366F1' stop-opacity='.35'/><stop offset='1' stop-color='#6366F1' stop-opacity='0'/></linearGradient></defs>"
        + "<g stroke='#E2E8F0' stroke-width='1' vector-effect='non-scaling-stroke'><line x1='0' y1='20' x2='300' y2='20'/><line x1='0' y1='50' x2='300' y2='50'/><line x1='0' y1='80' x2='300' y2='80'/></g>"
        + "<polygon fill='url(#mnArea)' points='0,80 45,72 90,50 135,68 180,35 225,42 300,15 300,110 0,110'/>"
        + "<polyline fill='none' stroke='#6366F1' stroke-width='2.5' vector-effect='non-scaling-stroke' points='0,80 45,72 90,50 135,68 180,35 225,42 300,15'/>"
        + "<polyline fill='none' stroke='#06B6D4' stroke-width='2.5' vector-effect='non-scaling-stroke' points='0,95 45,90 90,80 135,86 180,70 225,76 300,64'/></svg>";

    public DashboardPreview(boolean compact) {
        addClassNames("mn-dash-wrap", compact ? "mn-dash-wrap--stack" : "mn-dash-wrap--plain");
        getElement().setAttribute("aria-hidden", "true");
        Div side = Ui.div("mn-dash__side", new MailNovaLogo(true));
        for (int i = 0; i < MENU.length; i++) {
            side.add(Ui.div("mn-dash__item" + (i == 0 ? " is-active" : ""), Ui.icon(MENU_ICONS[i], "mn-dash__ico"), Ui.span("", MENU[i])));
        }
        Div stats = Ui.div("mn-dash__stats");
        for (int i = 0; i < (compact ? 4 : 5); i++) {
            Stat s = STATS[i];
            stats.add(Ui.div("mn-dash__stat", Ui.div("mn-dash__stat-top", Ui.span("mn-dash__label", s.label()), Ui.div("mn-tile mn-tile--xs mn-tone--" + s.tone(), Ui.icon(s.icon(), "mn-icon"))),
                    Ui.span("mn-dash__value", s.value()), Ui.span("mn-dash__delta", s.delta())));
        }
        Div legend = Ui.div("mn-dash__legend", Ui.span("mn-dash__key mn-dash__key--a", "Open Rate"), Ui.span("mn-dash__key mn-dash__key--b", "Click Rate"));
        Div activity = Ui.div("mn-dash__card", Ui.span("mn-dash__title", "Recent Activity"));
        for (String[] a : ACTIVITY) {
            activity.add(Ui.div("mn-dash__act", Ui.span("mn-dash__dot mn-tone--" + a[2], ""), Ui.div("", Ui.span("mn-dash__actname", a[0]), Ui.span("mn-dash__label", a[1]))));
        }
        Div head = Ui.div("mn-dash__head", Ui.span("mn-dash__title", "Dashboard"), Ui.span("mn-dash__search", "Search anything..."),
                Ui.div("mn-dash__user", Ui.span("mn-dash__avatar", "JD"), Ui.span("mn-dash__label", "John Doe")));
        Div dash = Ui.div("mn-dash", side, Ui.div("mn-dash__main", head, stats,
                Ui.div("mn-dash__grid", Ui.div("mn-dash__card", Ui.div("mn-dash__cardhead", Ui.span("mn-dash__title", "Campaign Performance"), legend), new Html(CHART)), activity)));
        add(dash);
        if (compact) {
            add(Ui.div("mn-floatcard", Ui.span("mn-dash__label", "Audience"), Ui.span("mn-dash__value", "24,582"), Ui.span("mn-dash__delta", "▲ +3.2% this month")));
        }
    }
}
