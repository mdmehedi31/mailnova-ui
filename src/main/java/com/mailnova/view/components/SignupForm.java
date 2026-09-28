package com.mailnova.view.components;

import com.mailnova.service.SignupService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.function.Predicate;

/** Registration card with live password-rule feedback. Delegates persistence to {@link SignupService}. */
public class SignupForm extends Div {
    private static final int MIN_PASSWORD_LENGTH = 8;
    private final TextField name = new TextField("Full Name");
    private final EmailField email = new EmailField("Email Address");
    private final PasswordField password = new PasswordField("Password");
    private final PasswordField confirm = new PasswordField("Confirm Password");
    private final Checkbox terms = new Checkbox();
    private final Button submit = new Button("Create Account", Ui.icon(VaadinIcon.ARROW_RIGHT, "mn-icon"));
    private final Div rules = Ui.div("mn-rules");
    private final Rule[] ruleSet = {
        new Rule("At least 8 characters", p -> p.length() >= MIN_PASSWORD_LENGTH),
        new Rule("One number", p -> p.chars().anyMatch(Character::isDigit)),
        new Rule("One special character", p -> p.chars().anyMatch(c -> !Character.isLetterOrDigit(c)))};

    private class Rule {
        final Div view; final Predicate<String> test;
        Rule(String label, Predicate<String> test) {
            this.test = test;
            view = Ui.div("mn-rule", Ui.icon(VaadinIcon.CIRCLE_THIN, "mn-icon"), Ui.span("", label));
            rules.add(view);
        }
        boolean update(String value) {
            boolean ok = test.test(value);
            view.getClassNames().set("mn-rule--ok", ok);
            view.getComponentAt(0).getElement().setAttribute("icon", ok ? "vaadin:check-circle" : "vaadin:circle-thin");
            return ok;
        }
    }

    public SignupForm(SignupService service) {
        addClassName("mn-signup-card");
        name.setPrefixComponent(Ui.icon(VaadinIcon.USER, "mn-icon")); name.setPlaceholder("John Doe");
        email.setPrefixComponent(Ui.icon(VaadinIcon.ENVELOPE_O, "mn-icon")); email.setPlaceholder("you@company.com");
        password.setPrefixComponent(Ui.icon(VaadinIcon.LOCK, "mn-icon")); password.setPlaceholder("Create a strong password");
        confirm.setPrefixComponent(Ui.icon(VaadinIcon.LOCK, "mn-icon")); confirm.setPlaceholder("Confirm your password");
        name.setWidthFull(); email.setWidthFull(); password.setWidthFull(); confirm.setWidthFull();
        name.setValueChangeMode(ValueChangeMode.EAGER); email.setValueChangeMode(ValueChangeMode.EAGER);
        password.setValueChangeMode(ValueChangeMode.EAGER); confirm.setValueChangeMode(ValueChangeMode.EAGER);
        name.addValueChangeListener(e -> refresh()); email.addValueChangeListener(e -> refresh());
        password.addValueChangeListener(e -> refresh()); confirm.addValueChangeListener(e -> refresh());
        terms.setLabelComponent(Ui.div("", Ui.span("", "I agree to the "), Ui.link("Terms of Service", "#", "mn-link"), Ui.span("", " and "), Ui.link("Privacy Policy", "#", "mn-link")));
        terms.addValueChangeListener(e -> refresh());
        submit.setIconAfterText(true);
        submit.addClassNames("mn-btn", "mn-btn--primary", "mn-btn--block");
        submit.setEnabled(false);
        submit.addClickListener(e -> {
            var result = service.register(name.getValue(), email.getValue(), password.getValue());
            Notification.show(result.message());
        });
        Button google = new Button("Continue with Google", Ui.icon(VaadinIcon.GOOGLE_PLUS, "mn-icon"));
        google.addClassNames("mn-btn", "mn-btn--secondary", "mn-btn--block");
        google.addClickListener(e -> Notification.show("Google sign-in is not connected yet."));
        add(new MailNovaLogo(false), Ui.h(2, "mn-h3", "Create Your Account"),
            Ui.span("mn-sub", "Get started with MailNova and take your email marketing to the next level."),
            name, email, password, rules, confirm, terms, submit,
            Ui.div("mn-or", Ui.span("", "OR")), google,
            Ui.div("mn-login-card__foot", Ui.span("", "Already have an account? "), Ui.link("Sign In", "/#login", "mn-link")));
        refresh();
    }

    private void refresh() {
        boolean rulesOk = true;
        for (Rule r : ruleSet) rulesOk &= r.update(password.getValue());
        boolean match = !confirm.isEmpty() && confirm.getValue().equals(password.getValue());
        confirm.setInvalid(!confirm.isEmpty() && !match);
        confirm.setErrorMessage("Passwords do not match");
        submit.setEnabled(rulesOk && match && terms.getValue() && !name.isEmpty() && !email.isEmpty() && !email.isInvalid());
    }
}
