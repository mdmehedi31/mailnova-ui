package com.mailnova.service;

import com.mailnova.DTO.EmailTemplateDto;
import com.mailnova.DTO.EmailTemplateTypeDTO;
import com.mailnova.endpoint.EmailTemplateTypesCaller;
import com.mailnova.view.campaign.model.EmailTemplate;
import com.mailnova.view.campaign.model.TemplateFilter;
import com.mailnova.view.campaign.model.TemplateSource;
import com.mailnova.view.campaign.model.Visibility;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

/**
 * In-memory store + stubbed AI generator. Replace with a repository / real AI client later;
 * no view or component depends on how data is stored.
 */
@Service
public class TemplateService {


    private final EmailTemplateService emailTemplateService;
    private EmailTemplateTypesCaller emailTemplateTypesCaller;

    public TemplateService(EmailTemplateTypesCaller emailTemplateTypesCaller, EmailTemplateService emailTemplateService) {
        this.emailTemplateTypesCaller = emailTemplateTypesCaller;
        this.emailTemplateService = emailTemplateService;
    }


    public List<EmailTemplateTypeDTO> getEmailTemplateTypes() {
        return emailTemplateTypesCaller.getEmailTemplates();
    }

    private final List<EmailTemplate> templates = new CopyOnWriteArrayList<>(seed());

    public List<EmailTemplate> findAll() { return List.copyOf(templates); }

    public List<EmailTemplate> search(TemplateFilter f) {
        var stream = templates.stream().filter(f::matches);
        if (f.collection() == TemplateFilter.Collection.RECENT) {
            stream = stream.sorted(Comparator.comparing(EmailTemplate::getLastUsedAt).reversed()).limit(12);
        }
        return stream.toList();
    }

    public List<EmailTemplateDto> recentlyUsed(int n) {
        /*return templates.stream().sorted(Comparator.comparing(EmailTemplate::getLastUsedAt).reversed())
                .limit(n).toList();*/
        return  new ArrayList<>();
    }

    public long count(Predicate<EmailTemplate> p) { return templates.stream().filter(p).count(); }

    public Optional<EmailTemplate> mostUsed() {
        return templates.stream().max(Comparator.comparingInt(EmailTemplate::getUses));
    }

    public EmailTemplateDto createEmailTemplate(EmailTemplateDto emailTemplateDto) {
        EmailTemplateDto saveResponse = emailTemplateService.createEmailTemplate(emailTemplateDto);
        return saveResponse;
    }

    public EmailTemplateDto duplicate(EmailTemplateDto t) {
        //EmailTemplateDto c = t.copy();
        //templates.add(0, c);
        return t;
    }

    public void delete(EmailTemplate t) { templates.remove(t); }

    public void toggleFavorite(EmailTemplate t) {
        t.setFavorite(!t.isFavorite()); }

    public void markUsed(EmailTemplate t) {
        t.setUses(t.getUses() + 1);
        t.setLastUsedAt(LocalDateTime.now());
    }

    /** Stand-in for a real AI call. Returns a subject + HTML body for the shared editor. */
    public AiDraft generate(String type, String subtype, String name, String purpose, String instructions) {
        String subject = subtype + " — {{companyName}}";
        String extra = instructions == null || instructions.isBlank() ? ""
                : "<p>" + instructions.replace("<", "&lt;").replace(">", "&gt;") + "</p>";
        String body = "<p>Dear {{recipientName}},</p>"
                + "<p>Thank you for your interest in {{companyName}}. "
                + (purpose == null || purpose.isBlank() ? "" : purpose.replace("<", "&lt;") + ". ")
                + "We would like to share the details below.</p>"
                + "<ul><li>Date: {{date}}</li><li>Time: {{time}}</li><li>Location: {{location}} (or Online)</li></ul>"
                + extra
                + "<p>Looking forward to hearing from you!</p><p>Best regards,<br>{{companyName}} Team</p>";
        return new AiDraft(subject, body);
    }

    public record AiDraft(String subject, String body) { }

    // ---------------------------------------------------------------- seed data

    private static EmailTemplate t(String name, String type, String sub, TemplateSource src, Visibility vis,
                                   int uses, int ageHours, boolean fav, String subject, String body) {
        EmailTemplate e = new EmailTemplate(name, type, sub, subject, body, src, vis);
        e.setUses(uses);
        e.setFavorite(fav);
        e.setLastUsedAt(LocalDateTime.now().minusHours(ageHours));
        e.setCreatedOn(LocalDate.of(2025, 1, 12));
        return e;
    }

    private static String simple(String greeting, String line) {
        return "<p>" + greeting + "</p><p>" + line + "</p><p>Best regards,<br>{{companyName}} Team</p>";
    }

    private static List<EmailTemplate> seed() {
        String interview = "<p>Dear Candidate,</p>"
                + "<p>Thank you for your application for the {{jobTitle}} position at {{companyName}}. "
                + "We were impressed with your background and experience and would like to invite you for an interview.</p>"
                + "<p>Please find the details below:</p>"
                + "<ul><li>Date: {{interviewDate}}</li><li>Time: {{interviewTime}}</li>"
                + "<li>Location: {{interviewLocation}} (or Online)</li></ul>"
                + "<p>If you have any questions or need to reschedule, feel free to contact us at {{contactEmail}}.</p>"
                + "<p>We look forward to speaking with you!</p><p>Best regards,<br>{{companyName}} Team</p>";
        return new CopyOnWriteArrayList<>(List.of(
            t("Interview Invitation", "HR", "Interview Scheduling", TemplateSource.AI, Visibility.PUBLIC, 142, 2, true,
                "Interview for {{jobTitle}} - {{companyName}}", interview),
            t("Welcome Email", "Marketing", "New Subscribers", TemplateSource.MANUAL, Visibility.PRIVATE, 98, 24, false,
                "Welcome to {{companyName}}!", simple("Welcome to our community!", "We're excited to have you with us.")),
            t("Job Offer Letter", "HR", "Job Offers", TemplateSource.AI, Visibility.PUBLIC, 76, 48, true,
                "Your offer from {{companyName}}", simple("Dear Candidate,", "We are pleased to offer you the {{jobTitle}} position.")),
            t("Product Update", "Marketing", "Newsletter", TemplateSource.MANUAL, Visibility.PUBLIC, 64, 72, false,
                "What's new at {{companyName}}", simple("Hi there,", "Here's what's new in our product this month.")),
            t("Rejection Email", "HR", "Candidate Rejection", TemplateSource.MANUAL, Visibility.PRIVATE, 54, 72, false,
                "Update on your application", simple("Dear Candidate,", "Thank you for your interest in our company. Although we were impressed, we have decided to move forward with other candidates.")),
            t("Sales Follow-up", "Sales", "Follow-up", TemplateSource.AI, Visibility.PUBLIC, 47, 96, false,
                "Following up on our conversation", simple("Hi {{firstName}},", "I wanted to follow up on our recent conversation.")),
            t("Proposal Cover Note", "Sales", "Proposal", TemplateSource.MANUAL, Visibility.PRIVATE, 31, 120, false,
                "Proposal for {{companyName}}", simple("Hello {{firstName}},", "Please find our proposal attached.")),
            t("Ticket Resolved", "Customer Support", "Ticket Update", TemplateSource.MANUAL, Visibility.PUBLIC, 88, 30, false,
                "Your ticket {{ticketId}} is resolved", simple("Hi {{firstName}},", "Your support ticket has been resolved.")),
            t("Feedback Request", "Customer Support", "Feedback Request", TemplateSource.AI, Visibility.PUBLIC, 39, 150, true,
                "How did we do?", simple("Hi {{firstName}},", "We'd love to hear your feedback on your recent experience.")),
            t("Password Reset", "Others", "Account Security", TemplateSource.MANUAL, Visibility.PUBLIC, 311, 200, false,
                "Reset your password", simple("Hi {{firstName}},", "We received a request to reset your password.")),
            t("Monthly Newsletter", "Marketing", "Newsletter", TemplateSource.MANUAL, Visibility.PUBLIC, 212, 240, true,
                "{{month}} highlights", simple("Hello,", "Here's what happened this month."))
        ));
    }
}
