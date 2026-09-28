package com.mailnova.view.campaign.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/** User-facing template record. Only fields that belong in the UI are exposed. */
public class EmailTemplate {

    private String id = UUID.randomUUID().toString();
    private String name;
    private String type;
    private String subtype;
    private String subject;
    /** HTML produced by the shared Rich Text Editor. */
    private String body;
    private TemplateSource source = TemplateSource.MANUAL;
    private Visibility visibility = Visibility.PUBLIC;
    private boolean favorite;
    private boolean draft;
    private int uses;
    private String createdBy = "Md Mehedi Hasan";
    private LocalDate createdOn = LocalDate.now();
    private LocalDateTime lastUsedAt = LocalDateTime.now();

    public EmailTemplate() { }

    public EmailTemplate(String name, String type, String subtype, String subject, String body,
                          TemplateSource source, Visibility visibility) {
        this.name = name; this.type = type; this.subtype = subtype; this.subject = subject;
        this.body = body; this.source = source; this.visibility = visibility;
    }

    /** First two text blocks, used as the 2-line preview on cards. */
    public List<String> previewLines() {
        if (body == null) return List.of();
        return Jsoup.parse(body).select("p, li").stream()
                .map(Element::text).filter(s -> !s.isBlank()).limit(2).toList();
    }

    public String plainText() { return body == null ? "" : Jsoup.parse(body).text(); }

    public EmailTemplate copy() {
        EmailTemplate c = new EmailTemplate(name + " (Copy)", type, subtype, subject, body, source, visibility);
        c.setDraft(draft);
        return c;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String v) { name = v; }
    public String getType() { return type; }
    public void setType(String v) { type = v; }
    public String getSubtype() { return subtype; }
    public void setSubtype(String v) { subtype = v; }
    public String getSubject() { return subject; }
    public void setSubject(String v) { subject = v; }
    public String getBody() { return body; }
    public void setBody(String v) { body = v; }
    public TemplateSource getSource() { return source; }
    public void setSource(TemplateSource v) { source = v; }
    public Visibility getVisibility() { return visibility; }
    public void setVisibility(Visibility v) { visibility = v; }
    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean v) { favorite = v; }
    public boolean isDraft() { return draft; }
    public void setDraft(boolean v) { draft = v; }
    public int getUses() { return uses; }
    public void setUses(int v) { uses = v; }
    public String getCreatedBy() { return createdBy; }
    public LocalDate getCreatedOn() { return createdOn; }
    public void setCreatedOn(LocalDate v) { createdOn = v; }
    public LocalDateTime getLastUsedAt() { return lastUsedAt; }
    public void setLastUsedAt(LocalDateTime v) { lastUsedAt = v; }
}
