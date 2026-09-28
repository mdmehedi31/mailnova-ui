package com.mailnova.view.dashboard;

/**
 * A single recipient row shown in the recipient activity table.
 */
public record RecipientData(String email, RecipientStatus status, String lastActivity) {

    public enum RecipientStatus {
        CLICKED("Clicked", "mn-status-clicked"),
        OPENED("Opened", "mn-status-opened"),
        DELIVERED("Delivered", "mn-status-delivered"),
        BOUNCED("Bounced", "mn-status-bounced"),
        UNSUBSCRIBED("Unsubscribed", "mn-status-unsubscribed");

        private final String label;
        private final String styleClass;

        RecipientStatus(String label, String styleClass) {
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
