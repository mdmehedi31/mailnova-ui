package com.mailnova.view.components;

import com.mailnova.view.dashboard.CompanyData;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

import java.util.List;

/**
 * "Your Companies" section: heading, actions, and a horizontally
 * scrollable row of selectable company cards.
 */
public class CompanySelector extends Div {

    private CompanyData selected;
    private final List<Div> cardsInOrder = new java.util.ArrayList<>();
    private final List<CompanyData> companiesInOrder = new java.util.ArrayList<>();

    public CompanySelector(List<CompanyData> companies) {
        addClassNames("mn-companies");
        getStyle().set("margin-top", "32px");

        add(buildHeader());
        add(buildCompanyRow(companies));
        add(buildScrollerFoot());

        if (!companies.isEmpty()) {
            selected = companies.get(0);
        }
    }

    private FlexLayout buildHeader() {
        H1 title = new H1("Your Companies");
        Paragraph subtitle = new Paragraph("Select a company to view its campaigns and performance");
        Div titleBlock = new Div(title, subtitle);
        titleBlock.addClassName("mn-companies-title");

        TextField search = new TextField();
        search.setPlaceholder("Find company...");
        search.setPrefixComponent(VaadinIcon.SEARCH.create());
        search.setWidth("220px");

        Button newCompany = new Button("New Company", VaadinIcon.PLUS.create());
        newCompany.addClassName("mn-btn-primary");

        HorizontalLayout actions = new HorizontalLayout(search, newCompany);
        actions.addClassName("mn-companies-actions");
        actions.setAlignItems(HorizontalLayout.Alignment.CENTER);

        FlexLayout header = new FlexLayout(titleBlock, actions);
        header.addClassName("mn-companies-head");
        header.setWidthFull();
        return header;
    }

    private Div buildCompanyRow(List<CompanyData> companies) {
        Div row = new Div();
        row.setId("companies-row");
        row.addClassName("mn-companies-row");

        for (CompanyData company : companies) {
            Div card = buildCard(company);
            companiesInOrder.add(company);
            cardsInOrder.add(card);
            row.add(card);
        }

        Div wrapper = new Div(row);
        wrapper.getStyle().set("margin-top", "24px");
        return wrapper;
    }

    private Div buildCard(CompanyData company) {
        Div check = new Div();
        check.addClassName("mn-company-check");
        Icon checkIcon = VaadinIcon.CHECK.create();
        checkIcon.setSize("12px");
        checkIcon.getStyle().set("color", "white");
        check.add(checkIcon);

        Div avatar = new Div(new Span(company.initial()));
        avatar.addClassName("mn-company-avatar");
        avatar.getStyle().set("display", "flex");
        avatar.getStyle().set("background", company.colorHex());

        Span name = new Span(company.name());
        name.addClassName("mn-company-name");

        Span activeChip = new Span(company.active() + (company.active() == 1 ? " ACTIVE" : " ACTIVE"));
        activeChip.addClassName("mn-chip");
        activeChip.addClassName("active-chip");

        Span draftChip = new Span(company.drafts() + (company.drafts() == 1 ? " DRAFT" : " DRAFTS"));
        draftChip.addClassName("mn-chip");
        draftChip.addClassName("draft-chip");

        Div chips = new Div(activeChip, draftChip);
        chips.addClassName("mn-company-chips");
        chips.getStyle().set("display", "flex");

        Div meta = new Div(name, chips);

        Div card = new Div(check, avatar, meta);
        card.addClassName("mn-company-card");

        card.addClickListener(e -> selectCompany(company, card));

        return card;
    }

    private void selectCompany(CompanyData company, Div card) {
        for (Div c : cardsInOrder) {
            c.removeClassName("selected");
        }
        card.addClassName("selected");
        selected = company;
        fireEvent(new CompanySelectedEvent(this, company));
    }

    private HorizontalLayout buildScrollerFoot() {
        Span hint = new Span("SCROLL TO BROWSE ALL COMPANIES");
        hint.addClassName("mn-scroll-hint");

        Button left = new Button(VaadinIcon.ANGLE_LEFT.create());
        left.getElement().getThemeList().add("tertiary");
        left.getElement().setAttribute("aria-label", "Scroll left");

        Button right = new Button(VaadinIcon.ANGLE_RIGHT.create());
        right.getElement().getThemeList().add("tertiary");
        right.getElement().setAttribute("aria-label", "Scroll right");

        left.addClickListener(e -> getElement().executeJs(
                "this.querySelector('.mn-companies-row').scrollBy({left: -260, behavior: 'smooth'})"));
        right.addClickListener(e -> getElement().executeJs(
                "this.querySelector('.mn-companies-row').scrollBy({left: 260, behavior: 'smooth'})"));

        HorizontalLayout arrows = new HorizontalLayout(left, right);

        HorizontalLayout foot = new HorizontalLayout(hint, arrows);
        foot.addClassName("mn-scroller-foot");
        foot.setWidthFull();
        foot.setAlignItems(HorizontalLayout.Alignment.CENTER);
        foot.setJustifyContentMode(HorizontalLayout.JustifyContentMode.BETWEEN);
        return foot;
    }

    /** Marks the first company card as selected by default. Call once after construction. */
    public void selectFirstByDefault() {
        if (!cardsInOrder.isEmpty()) {
            cardsInOrder.get(0).addClassName("selected");
            selected = companiesInOrder.get(0);
        }
    }

    public CompanyData getSelectedCompany() {
        return selected;
    }

    public Registration addCompanySelectedListener(ComponentEventListener<CompanySelectedEvent> listener) {
        return addListener(CompanySelectedEvent.class, listener);
    }

    /** Fired whenever the user picks a different company card. */
    public static class CompanySelectedEvent extends ComponentEvent<CompanySelector> {
        private final CompanyData company;

        public CompanySelectedEvent(CompanySelector source, CompanyData company) {
            super(source, false);
            this.company = company;
        }

        public CompanyData getCompany() {
            return company;
        }
    }
}
