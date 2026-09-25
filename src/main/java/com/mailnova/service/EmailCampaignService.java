package com.mailnova.service;


import com.mailnova.DTO.EmailCampaignDto;
import com.mailnova.endpoint.EmailCampaignCaller;
import com.mailnova.exceptionmessage.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public class EmailCampaignService {

    private EmailCampaignCaller emailCampaignCaller;

    public EmailCampaignService(EmailCampaignCaller emailCampaignCaller) {
        this.emailCampaignCaller = emailCampaignCaller;
    }

    public EmailCampaignDto createEmailCampaign(EmailCampaignDto emailCampaignDto) {
        return emailCampaignCaller.createEmailCampaign(emailCampaignDto);
    }

    public EmailCampaignDto updateEmailCampaign(EmailCampaignDto emailCampaignDto) {
        return emailCampaignCaller.updateEmailCampaign(emailCampaignDto);
    }

    public EmailCampaignDto getEmailCampaignById(Integer emailCampaignId) {
        return emailCampaignCaller.getEmailCampaignById(emailCampaignId);
    }

    public ApiResponse deleteEmilCampaignById(Integer emailCampaignId) {
        return emailCampaignCaller.deleteEmailCampaignById(emailCampaignId);
    }

    public ApiResponse cancelEmailCampaignById(Integer emailCampaignId) {
        return emailCampaignCaller.cancelEmailCampaignById(emailCampaignId);
    }


}
