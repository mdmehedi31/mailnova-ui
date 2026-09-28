package com.mailnova.view.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * Horizontal 4-step wizard indicator: circle + label per step, joined by a
 * connecting line. Matches the reference design: a single indigo accent is
 * used for both the current and every completed step (checkmark), future
 * steps stay muted gray. Lives above the two-column content area, never as
 * a sidebar.
 */
public class WizardStepper extends HorizontalLayout {

    public static final String[] STEP_LABELS = {"Details", "Audience", "Content", "Schedule & Review"};

    public WizardStepper(int currentStep) {
        addClassName("mn-wizard");
        setWidthFull();
        setSpacing(false);
        setPadding(false);
        setAlignItems(Alignment.CENTER);

        for (int i = 1; i <= STEP_LABELS.length; i++) {
            add(buildStep(i, currentStep));
            if (i < STEP_LABELS.length) {
                add(buildConnector(i < currentStep));
            }
        }
    }

    private Component buildStep(int stepNumber, int currentStep) {
        boolean completed = stepNumber < currentStep;
        boolean active = stepNumber == currentStep;

        Div circle = new Div();
        circle.addClassName("mn-wizard-circle");
        if (completed) {
            circle.addClassName("mn-wizard-circle-completed");
            Icon check = VaadinIcon.CHECK.create();
            check.addClassName("mn-wizard-check");
            circle.add(check);
        } else if (active) {
            circle.addClassName("mn-wizard-circle-active");
            circle.add(new Span(String.valueOf(stepNumber)));
        } else {
            circle.addClassName("mn-wizard-circle-future");
            circle.add(new Span(String.valueOf(stepNumber)));
        }

        Span label = new Span(STEP_LABELS[stepNumber - 1]);
        label.addClassName("mn-wizard-label");
        if (active) {
            label.addClassName("mn-wizard-label-active");
        } else if (completed) {
            label.addClassName("mn-wizard-label-completed");
        } else {
            label.addClassName("mn-wizard-label-future");
        }

        HorizontalLayout step = new HorizontalLayout(circle, label);
        step.addClassName("mn-wizard-step");
        step.setSpacing(true);
        step.setAlignItems(Alignment.CENTER);
        return step;
    }

    private Component buildConnector(boolean filled) {
        Div line = new Div();
        line.addClassName("mn-wizard-connector");
        if (filled) {
            line.addClassName("mn-wizard-connector-filled");
        }
        expand(line);
        return line;
    }
}
