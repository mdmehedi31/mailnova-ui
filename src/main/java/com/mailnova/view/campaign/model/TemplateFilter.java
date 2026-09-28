package com.mailnova.view.campaign.model;

/** Everything the Template Management view can filter on, in one immutable value. */
public record TemplateFilter(String text, String type, String subtype, Chip chip,
                             Collection collection, Visibility visibility) {

    public enum Chip { ALL, MANUAL, AI, PUBLIC, PRIVATE }
    public enum Collection { ALL, FAVORITES, RECENT }

    public boolean matches(EmailTemplate t) {
        String q = text == null ? "" : text.trim().toLowerCase();
        boolean textOk = q.isEmpty()
                || (t.getName() + " " + t.getType() + " " + t.getSubtype()).toLowerCase().contains(q);
        boolean typeOk = type == null || type.equals(t.getType());
        boolean subOk = subtype == null || subtype.equals(t.getSubtype());
        boolean chipOk = switch (chip) {
            case ALL -> true;
            case MANUAL -> t.getSource() == TemplateSource.MANUAL;
            case AI -> t.getSource() == TemplateSource.AI;
            case PUBLIC -> t.getVisibility() == Visibility.PUBLIC;
            case PRIVATE -> t.getVisibility() == Visibility.PRIVATE;
        };
        boolean visOk = visibility == null || visibility == t.getVisibility();
        boolean colOk = collection != Collection.FAVORITES || t.isFavorite();
        return textOk && typeOk && subOk && chipOk && visOk && colOk;
    }
}
