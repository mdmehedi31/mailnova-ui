package com.mailnova.endpoint;


import com.mailnova.DTO.EmailCampaignDto;
import com.mailnova.exceptionmessage.ApiResponse;
import com.mailnova.restEndPointCaller.RestEndPointCaller;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class EmailCampaignCaller extends RestEndPointCaller {


    public EmailCampaignDto  createEmailCampaign(EmailCampaignDto emailCampaign, MultipartFile file) {
      //  EmailCampaignDto createEmailCampaign = postCallerWithRequestBody(EmailCampaignEndpointPath.CREATE_CAMPAIGN_PATH,emailCampaign,EmailCampaignDto.class);

        EmailCampaignDto createEmailCampaign= null;
        try {
            createEmailCampaign = postCallerForMultipart(EmailCampaignEndpointPath.CREATE_CAMPAIGN_PATH,"campaign", emailCampaign, "file",file, EmailCampaignDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return createEmailCampaign;
    }

    public EmailCampaignDto updateEmailCampaign(EmailCampaignDto emailCampaign) {
        EmailCampaignDto updateEmailCampaign = putCallerWithRequestBody(EmailCampaignEndpointPath.UPDATE_CAMPAIGN_PATH,emailCampaign,EmailCampaignDto.class);
        return updateEmailCampaign;
    }

    public EmailCampaignDto getEmailCampaignById(Integer campaignId) {
        EmailCampaignDto emailCampaignDto = getCallerWithPathVariable(EmailCampaignEndpointPath.GET_CAMPAIGN_PATH+"/"+campaignId,EmailCampaignDto.class);
        return emailCampaignDto;
    }


    public ApiResponse deleteEmailCampaignById(Integer campaignId) {
        ApiResponse apiResponse = putCallerWithPathVariable(EmailCampaignEndpointPath.DELETE_CAMPAIGN_PATH+"/"+campaignId,ApiResponse.class);
        return apiResponse;
    }

    public ApiResponse cancelEmailCampaignById(Integer campaignId) {
        ApiResponse response = putCallerWithPathVariable(EmailCampaignEndpointPath.CANCEL_CAMPAIGN_PATH+"/"+campaignId,ApiResponse.class);
        return response;
    }
}
