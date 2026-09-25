package com.mailnova.view.utils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;

/**
 * A slim, branded replacement for Vaadin's default {@link Notification} card.
 * <p>
 * Shows a tinted icon badge, title/message text, an accessible close button,
 * and a bottom progress bar that visually counts down to auto-close.
 */
public class MailNovaNotification extends Notification {

    private static final int DEFAULT_DURATION_MS = 4000;

    public MailNovaNotification(NotificationType type, String title, String message) {
        this(type, title, message, DEFAULT_DURATION_MS);
    }

    public MailNovaNotification(NotificationType type, String title, String message, int durationMillis) {
        setPosition(Position.TOP_END);
        setDuration(durationMillis);

        // Clear default Vaadin notification chrome so our stylesheet controls everything.
        setClassName("mailnova-notification-container");

        String typeClass = type.name().toLowerCase();

        // Main card wrapper
        Div card = new Div();
        card.addClassName("mailnova-notification-card");
        card.addClassName(typeClass);

        // Icon badge (tinted circle + icon), rather than a bare glyph
        Div iconWrapper = new Div();
        iconWrapper.addClassName("notification-icon-wrapper");
        iconWrapper.addClassName(typeClass);

        Icon icon = getIcon(type);
        icon.addClassName("notification-icon");
        icon.setSize("18px"); // set explicitly; don't rely solely on CSS to beat Vaadin's internal default
        applyIconColor(icon, type);
        iconWrapper.add(icon);

        // Text content
        Div textContainer = new Div();
        textContainer.addClassName("notification-text-container");

        Span titleLabel = new Span(title);
        titleLabel.addClassName("notification-title");

        Span messageLabel = new Span(message);
        messageLabel.addClassName("notification-message");

        textContainer.add(titleLabel, messageLabel);

        // Close button
        Button closeButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        closeButton.addClassName("notification-close-btn");
        closeButton.getElement().setAttribute("aria-label", "Close notification");
        closeButton.addClickListener(e -> close());

        // Bottom progress bar — animation length matches the notification's own duration
        Div progressBar = new Div();
        progressBar.addClassName("notification-progress");
        progressBar.addClassName(typeClass);
        if (durationMillis > 0) {
            progressBar.getStyle().set("animation-duration", durationMillis + "ms");
        } else {
            // Duration 0 means "stays open until dismissed" — hide the bar rather than show a stalled one.
            progressBar.getStyle().set("display", "none");
        }

        // Assemble card. Progress bar is absolutely positioned in CSS, so appending it
        // last doesn't affect the flex layout of the icon/text/close row.
        card.add(iconWrapper, textContainer, closeButton, progressBar);
        add(card);
    }

    private Icon getIcon(NotificationType type) {
        return switch (type) {
            case SUCCESS -> VaadinIcon.CHECK_CIRCLE.create();
            case ERROR -> VaadinIcon.CLOSE_CIRCLE.create();
            case WARNING -> VaadinIcon.WARNING.create();
            case INFO -> VaadinIcon.INFO_CIRCLE.create();
        };
    }

    private void applyIconColor(Icon icon, NotificationType type) {
        String color = switch (type) {
            case SUCCESS -> "#22C55E";
            case ERROR -> "#EF4444";
            case WARNING -> "#EAB308";
            case INFO -> "#3B82F6";
        };
        icon.getStyle().set("color", color);
    }
}
