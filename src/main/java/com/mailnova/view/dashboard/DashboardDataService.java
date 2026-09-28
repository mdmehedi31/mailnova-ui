package com.mailnova.view.dashboard;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Provides the sample data used to populate the dashboard.
 * Swap this out for a real repository/service when wiring up a backend.
 */
@Service
public class DashboardDataService {

    public List<CompanyData> getCompanies() {
        return List.of(
                new CompanyData("N", "Nova Retail Co.", "#2563EB", 6, 2),
                new CompanyData("B", "BrightPath Labs", "#8B5CF6", 4, 1),
                new CompanyData("H", "Horizon Foods", "#10B981", 3, 3),
                new CompanyData("S", "Skyline Realty", "#F59E0B", 2, 0),
                new CompanyData("P", "Pulse Fitness", "#F43F5E", 5, 1),
                new CompanyData("A", "Aurora Media", "#334155", 3, 2)
        );
    }

    public List<CampaignData> getCampaigns() {
        return List.of(
                new CampaignData("Summer Sale Blast", CampaignData.CampaignType.PROMO, CampaignData.CampaignStatus.RUNNING,
                        "24,500", "23,980", "42.3%", 42, "11.2%", "520", false),
                new CampaignData("New Arrivals Teaser", CampaignData.CampaignType.DRAFT, CampaignData.CampaignStatus.DRAFT,
                        "—", "—", "—", 0, "—", "—", false),
                new CampaignData("Loyalty Rewards Update", CampaignData.CampaignType.NEWSLETTER, CampaignData.CampaignStatus.COMPLETED,
                        "18,200", "17,860", "55.8%", 56, "19.4%", "340", false),
                new CampaignData("Weekend Flash Deals", CampaignData.CampaignType.PROMO, CampaignData.CampaignStatus.RUNNING,
                        "31,050", "29,760", "38.6%", 39, "9.8%", "1,290", true),
                new CampaignData("Q2 Product Newsletter", CampaignData.CampaignType.NEWSLETTER, CampaignData.CampaignStatus.COMPLETED,
                        "42,300", "41,510", "47.1%", 47, "14.7%", "790", false),
                new CampaignData("Holiday Preview Drop", CampaignData.CampaignType.DRAFT, CampaignData.CampaignStatus.DRAFT,
                        "—", "—", "—", 0, "—", "—", false),
                new CampaignData("VIP Members Exclusive", CampaignData.CampaignType.PROMO, CampaignData.CampaignStatus.RUNNING,
                        "9,840", "9,690", "61.4%", 61, "22.9%", "150", false)
        );
    }

    public List<RecipientData> getRecipients() {
        return List.of(
                new RecipientData("alicia.morgan@gmail.com", RecipientData.RecipientStatus.CLICKED, "Jun 13, 9:42 AM"),
                new RecipientData("daniel.reyes@outlook.com", RecipientData.RecipientStatus.OPENED, "Jun 13, 8:15 AM"),
                new RecipientData("priya.shah@yahoo.com", RecipientData.RecipientStatus.DELIVERED, "Jun 12, 6:03 PM"),
                new RecipientData("marcus.lee@hotmail.com", RecipientData.RecipientStatus.BOUNCED, "Jun 12, 5:58 PM"),
                new RecipientData("sophie.turner@gmail.com", RecipientData.RecipientStatus.CLICKED, "Jun 12, 4:21 PM"),
                new RecipientData("omar.haddad@icloud.com", RecipientData.RecipientStatus.OPENED, "Jun 12, 2:47 PM"),
                new RecipientData("grace.kim@gmail.com", RecipientData.RecipientStatus.DELIVERED, "Jun 12, 1:09 PM"),
                new RecipientData("liam.oconnor@outlook.com", RecipientData.RecipientStatus.UNSUBSCRIBED, "Jun 11, 11:30 AM")
        );
    }
}
