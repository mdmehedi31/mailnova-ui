package com.mailnova.view.dashboard;

/**
 * A single email campaign row shown in the campaign table.
 */
public record CampaignData(
        String name,
        CampaignType type,
        CampaignStatus status,
        String sent,
        String delivered,
        String openRate,
        int openRatePercent,
        String clickRate,
        String failed,
        boolean failedHigh
) {

    public enum CampaignType {
        PROMO, NEWSLETTER, DRAFT
    }

    public enum CampaignStatus {
        RUNNING("Running", "running"),
        DRAFT("Draft", "draft"),
        COMPLETED("Completed", "completed");

        private final String label;
        private final String styleClass;

        CampaignStatus(String label, String styleClass) {
            this.label = label;
            this.styleClass = styleClass;
        }

        public String label() {
            return label;
        }

        public String styleClass() {
            return styleClass;
        }
    }
}
