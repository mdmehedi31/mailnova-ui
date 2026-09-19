package com.mailnova.view.companies;


import com.mailnova.DTO.CompanyProfileDto;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.time.LocalDateTime;

/**
 * Builds the "how your company info will appear in an email" preview
 * card shown on both the profile preview screen and the edit screen.
 * Exposes an {@link Handle} so the edit view can update the preview
 * live as the user types, without rebuilding the whole card.
 */
final class CompanyPreviewBuilder {

    private CompanyPreviewBuilder() {
    }

    static Div build(CompanyProfileDto company) {
        return new Handle(company).card;
    }

    /** Mutable handle over a rendered preview card, for live editing. */
    static final class Handle {

        final Div card;
        private final Span logoName = new Span();
        private final Div logoIcon = new Div();
        private final Span footerName = new Span();
        private final Span nameLine = new Span();
        private final Span emailLine = new Span();
        private final Span phoneLine = new Span();
        private final Span addressLine = new Span();
        private final Anchor websiteLine = new Anchor();

        Handle(CompanyProfileDto company) {
            H3 title = new H3("Preview");
            title.addClassName("mn-card-title");
            Paragraph subtitle = new Paragraph("This is how your company information will appear in your emails.");
            subtitle.addClassName("mn-card-subtitle");

            Div emailCard = buildEmailCard(company, company.getColorCode());

            card = new Div(title, subtitle, emailCard);
            card.addClassName("mn-card");
            card.addClassName("mn-preview-card");
        }

        private Div buildEmailCard(CompanyProfileDto company,String colorCode) {
            logoIcon.addClassName("mn-preview-logo-icon");
            logoIcon.add(new Icon(VaadinIcon.ENVELOPE_O));
            logoIcon.getStyle().set("background", colorCode);

            logoName.setText(company.getName()!=null ? company.getName().replace(" Ltd.", ""):"");
            logoName.addClassName("mn-preview-logo-name");

            HorizontalLayout brandRow = new HorizontalLayout(
                    new HorizontalLayout(logoIcon, logoName));
            brandRow.addClassName("mn-preview-brand-row");
            brandRow.setWidthFull();
            brandRow.setAlignItems(FlexComponent.Alignment.CENTER);
            brandRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

            H4 hello = new H4("Hello,");
            hello.addClassName("mn-preview-hello");

            Paragraph body = new Paragraph(
                    "Thank you for being a valued customer. We're excited to have you on board!");
            body.addClassName("mn-preview-body");

            Hr divider = new Hr();
            divider.addClassName("mn-preview-divider");

            nameLine.setText(company.getName() !=null ? company.getName() : "");
            emailLine.setText(company.getFromEmailSendAddress() != null ? company.getFromEmailSendAddress() : "");
            phoneLine.setText(company.getPhoneNo() !=null ? company.getPhoneNo() : "");
            addressLine.setText(company.getAddress() !=null ? company.getAddress().replace("\n", ", "):"");
            websiteLine.setText(company.getWebsite()!=null ? company.getWebsite() : "");
            websiteLine.setHref(company.getWebsite()!=null ? company.getWebsite() : "");
            websiteLine.addClassName("mn-preview-link");

            VerticalLayout metaList = new VerticalLayout(
                    metaRow(VaadinIcon.BUILDING, nameLine),
                    metaRow(VaadinIcon.ENVELOPE_O, emailLine),
                    metaRow(VaadinIcon.PHONE, phoneLine),
                    metaRow(VaadinIcon.MAP_MARKER, addressLine),
                    metaRow(VaadinIcon.GLOBE, websiteLine));
            metaList.setPadding(false);
            metaList.setSpacing(false);
            metaList.addClassName("mn-preview-meta-list");

            footerName.setText("© 2025 " + company.getName() + " All rights reserved.");
            footerName.addClassName("mn-preview-footer");

            Div body_ = new Div(brandRow, hello, body, divider, metaList, footerName);
            body_.addClassName("mn-preview-email-card");
            return body_;
        }

        private HorizontalLayout metaRow(VaadinIcon icon, com.vaadin.flow.component.Component value) {
            Icon i = new Icon(icon);
            i.addClassName("mn-preview-meta-icon");
            HorizontalLayout row = new HorizontalLayout(i, value);
            row.addClassName("mn-preview-meta-row");
            row.setAlignItems(FlexComponent.Alignment.START);
            return row;
        }

        void update(CompanyProfileDto company) {
           // logoIcon.getStyle().set("background", company.getAvatarColor());
            logoName.setText(company.getName().replace(" Ltd.", ""));
            emailLine.setText(company.getFromEmailSendAddress()!=null ? company.getFromEmailSendAddress() : "");
            phoneLine.setText(company.getPhoneNo()!=null ? company.getPhoneNo() : "");
            addressLine.setText(company.getAddress() !=null? company.getAddress().replace("\n", ", "):null);
            websiteLine.setText(company.getWebsite()!=null? company.getWebsite():null);
            websiteLine.setHref(company.getWebsite()!=null && company.getWebsite().isBlank() ? "#" : company.getWebsite());
            footerName.setText(LocalDateTime.now().getYear() + company.getName() + " All rights reserved.");
        }
    }
}
