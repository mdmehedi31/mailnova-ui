package com.mailnova.view.campaign.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Single source of truth for Type -> Subtype, shared by filters, Manual form and AI wizard. */
public final class TemplateTaxonomy {

    private static final Map<String, List<String>> TYPES = new LinkedHashMap<>();
    static {
        TYPES.put("HR", List.of("Interview Scheduling", "Job Offers", "Candidate Rejection", "Onboarding"));
        TYPES.put("Marketing", List.of("New Subscribers", "Newsletter", "Promotion"));
        TYPES.put("Sales", List.of("Follow-up", "Proposal"));
        TYPES.put("Customer Support", List.of("Ticket Update", "Feedback Request"));
        TYPES.put("Others", List.of("General", "Account Security"));
    }

    private TemplateTaxonomy() { }

    public static List<String> types() { return List.copyOf(TYPES.keySet()); }

    public static List<String> subtypesOf(String type) { return TYPES.getOrDefault(type, List.of()); }

    public static List<String> allSubtypes() {
        List<String> all = new ArrayList<>();
        TYPES.values().forEach(all::addAll);
        return all;
    }
}
