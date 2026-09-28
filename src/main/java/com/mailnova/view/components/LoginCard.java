package com.mailnova.view.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;

/** Hero login card. Authentication is intentionally not faked: hook {@link #onSignIn} to Spring Security. */
public class LoginCard extends Div {
    private final EmailField email = new EmailField("Email Address");
    private final PasswordField password = new PasswordField("Password");

    public LoginCard() {
        addClassName("mn-login-card");
        setId("login");
        email.setPrefixComponent(Ui.icon(VaadinIcon.ENVELOPE_O, "mn-icon"));
        email.setPlaceholder("Email Address");
        email.setWidthFull();
        password.setPrefixComponent(Ui.icon(VaadinIcon.LOCK, "mn-icon"));
        password.setPlaceholder("Password");
        password.setWidthFull();
        Button signIn = new Button("Sign In", Ui.icon(VaadinIcon.ARROW_RIGHT, "mn-icon"));
        signIn.setIconAfterText(true);
        signIn.addClassNames("mn-btn", "mn-btn--primary", "mn-btn--block");
        signIn.addClickListener(e -> onSignIn(email.getValue(), password.getValue()));
        add(new MailNovaLogo(false),
            Ui.span("mn-sub", "Welcome back! Sign in to your account."),
            email, password,
            Ui.div("mn-login-card__row", new Checkbox("Remember Me"), Ui.link("Forgot Password?", "#", "mn-link")),
            signIn,
            Ui.div("mn-login-card__foot", Ui.span("", "Don't have an account? "), Ui.link("Create Account", "/signup", "mn-link")));
    }

    /** Integration point for the authentication system. */
    protected void onSignIn(String emailValue, String passwordValue) {
        Notification.show("Sign-in is not connected yet.");
    }
}
