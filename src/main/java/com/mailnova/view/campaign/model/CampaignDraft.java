package com.mailnova.view.campaign.model;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory representation of the campaign currently being created in the wizard.
 * A single instance is shared (UI-scoped) across all four steps so that the
 * summary panels can reflect live edits.
 */
public class CampaignDraft {

    // Step 1 - Details
    private String campaignName = "";
    private String description = "";
    private String companyProfile = "";
    private String senderName = "";
    private String senderEmail = "";
    private String replyToEmail = "";
    private String tags = "";
    private String campaignType = "";

    // Step 2 - Audience
    private int totalRows = 0;
    private int validEmails = 0;
    private int invalidEmails = 0;
    private List<String> importedContacts = new ArrayList<>();
    private String lastImportFileName = "";

    // Step 3 - Content
    private boolean useExistingTemplate = true;
    private String templateName = "";
    private String templateType = "";
    private String templateCreatedOn = "";
    private String templateLastUpdated = "";
    private String templateStatus = "";
    private String emailSubject = "";
    private String previewText = "";

    // Step 4 - Schedule
    private boolean sendNow = true;
    private LocalDate scheduleDate;
    private LocalTime scheduleTime;
    private String timezone = "America/New_York";
    private boolean sendTestEmail = false;
    private boolean saveAsDraft = false;

    public int totalRecipients() {
        return validEmails + importedContacts.size() > 0 ? Math.max(validEmails, importedContacts.size()) : 0;
    }

    // ----- getters / setters -----
    public String getCampaignName() { return campaignName; }
    public void setCampaignName(String v) { this.campaignName = v; }

    public String getDescription() { return description; }
    public void setDescription(String v) { this.description = v; }

    public String getCompanyProfile() { return companyProfile; }
    public void setCompanyProfile(String v) { this.companyProfile = v; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String v) { this.senderName = v; }

    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String v) { this.senderEmail = v; }

    public String getReplyToEmail() { return replyToEmail; }
    public void setReplyToEmail(String v) { this.replyToEmail = v; }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCampaignType() { return campaignType; }
    public void setCampaignType(String v) { this.campaignType = v; }

    public int getTotalRows() { return totalRows; }
    public void setTotalRows(int v) { this.totalRows = v; }

    public int getValidEmails() { return validEmails; }
    public void setValidEmails(int v) { this.validEmails = v; }

    public int getInvalidEmails() { return invalidEmails; }
    public void setInvalidEmails(int v) { this.invalidEmails = v; }

    public List<String> getImportedContacts() { return importedContacts; }
    public void setImportedContacts(List<String> v) { this.importedContacts = v; }

    public String getLastImportFileName() { return lastImportFileName; }
    public void setLastImportFileName(String v) { this.lastImportFileName = v; }

    public boolean isUseExistingTemplate() { return useExistingTemplate; }
    public void setUseExistingTemplate(boolean v) { this.useExistingTemplate = v; }

    public String getTemplateName() { return templateName; }
    public void setTemplateName(String v) { this.templateName = v; }

    public String getTemplateType() { return templateType; }
    public void setTemplateType(String v) { this.templateType = v; }

    public String getTemplateCreatedOn() { return templateCreatedOn; }
    public void setTemplateCreatedOn(String v) { this.templateCreatedOn = v; }

    public String getTemplateLastUpdated() { return templateLastUpdated; }
    public void setTemplateLastUpdated(String v) { this.templateLastUpdated = v; }

    public String getTemplateStatus() { return templateStatus; }
    public void setTemplateStatus(String v) { this.templateStatus = v; }

    public String getEmailSubject() { return emailSubject; }
    public void setEmailSubject(String v) { this.emailSubject = v; }

    public String getPreviewText() { return previewText; }
    public void setPreviewText(String v) { this.previewText = v; }

    public boolean isSendNow() { return sendNow; }
    public void setSendNow(boolean v) { this.sendNow = v; }

    public LocalDate getScheduleDate() { return scheduleDate; }
    public void setScheduleDate(LocalDate v) { this.scheduleDate = v; }

    public LocalTime getScheduleTime() { return scheduleTime; }
    public void setScheduleTime(LocalTime v) { this.scheduleTime = v; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String v) { this.timezone = v; }

    public boolean isSendTestEmail() { return sendTestEmail; }
    public void setSendTestEmail(boolean v) { this.sendTestEmail = v; }

    public boolean isSaveAsDraft() { return saveAsDraft; }
    public void setSaveAsDraft(boolean v) { this.saveAsDraft = v; }
}
