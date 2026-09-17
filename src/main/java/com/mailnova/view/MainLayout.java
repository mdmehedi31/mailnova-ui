package com.mailnova.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.avatar.AvatarVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * Application shell: the top navigation bar shown in both screens
 * (logo on the left, notification bell + user menu on the right).
 * Every route in the app is rendered as the "content" of this layout.
 */
public class MainLayout extends AppLayout {

    public MainLayout() {
        addClassName("mn-shell");
        setPrimarySection(Section.NAVBAR);
        addToNavbar(buildHeader());
    }

    private HorizontalLayout buildHeader() {
        HorizontalLayout header = new HorizontalLayout(buildLogo(), buildActions());
        header.addClassName("mn-header");
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        return header;
    }

    private HorizontalLayout buildLogo() {
        Div icon = new Div();
        icon.addClassName("mn-logo-icon");
        icon.add(new Icon(VaadinIcon.ENVELOPE_O));

        Span title = new Span("MailNova");
        title.addClassName("mn-logo-text");

        HorizontalLayout logo = new HorizontalLayout(icon, title);
        logo.addClassName("mn-logo");
        logo.setAlignItems(FlexComponent.Alignment.CENTER);
        logo.setSpacing(true);
        return logo;
    }

    private HorizontalLayout buildActions() {
        Button bell = new Button(new Icon(VaadinIcon.BELL_O));
        bell.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ICON);
        bell.addClassName("mn-bell-button");

        Div dot = new Div();
        dot.addClassName("mn-notification-dot");

        Div bellWrapper = new Div(bell, dot);
        bellWrapper.addClassName("mn-bell-wrapper");

        Avatar avatar = new Avatar("Md Mehedi Hasan");
        avatar.addThemeVariants(AvatarVariant.LUMO_XSMALL);
        avatar.addClassName("mn-user-avatar");

        Span userName = new Span("Md Mehedi Hasan");
        userName.addClassName("mn-user-name");

        Icon chevron = new Icon(VaadinIcon.CHEVRON_DOWN_SMALL);
        chevron.addClassName("mn-user-chevron");

        HorizontalLayout userMenu = new HorizontalLayout(avatar, userName, chevron);
        userMenu.addClassName("mn-user-menu");
        userMenu.setAlignItems(FlexComponent.Alignment.CENTER);
        userMenu.setSpacing(true);

        HorizontalLayout actions = new HorizontalLayout(bellWrapper, userMenu);
        actions.addClassName("mn-actions");
        actions.setAlignItems(FlexComponent.Alignment.CENTER);
        actions.setSpacing(true);
        return actions;
    }
}
