package com.mailnova.endpoint;

import com.mailnova.DTO.EmailTemplateDto;
import com.mailnova.exceptionmessage.ApiResponse;
import com.mailnova.restEndPointCaller.RestEndPointCaller;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateCreateCaller extends RestEndPointCaller {


    public EmailTemplateDto createTemplate(EmailTemplateDto template) {
        EmailTemplateDto response = postCallerWithRequestBody(EmailTemplateCreateEndpointPath.CREATE_PATH, template, EmailTemplateDto.class);
        return response;
    }


    public EmailTemplateDto updateEmailTemplate(EmailTemplateDto template) {
        EmailTemplateDto emailTemplateDto= putCallerWithRequestBody(EmailTemplateCreateEndpointPath.UPDATE_PATH, template, EmailTemplateDto.class);
        return emailTemplateDto;
    }


    public ApiResponse deleteEmailTemplate(Integer templateId) {
        ApiResponse response = putCallerWithPathVariable(EmailTemplateCreateEndpointPath.DELETE_PATH+"/"+templateId, ApiResponse.class);
        return response;
    }


    public EmailTemplateDto getEmailTemplate(Integer templateId) {
        EmailTemplateDto emailTemplateDto = getCallerWithPathVariable(EmailTemplateCreateEndpointPath.GET_TEMPLATE+"/"+templateId, EmailTemplateDto.class);
        return emailTemplateDto;
    }


    public List<EmailTemplateDto> getEmailTemplatesByCreator(Integer creatorId) {
        EmailTemplateDto[] emailTemplateDtos = getCallerWithPathVariable(EmailTemplateCreateEndpointPath.GET_TEMPLATES+"/"+creatorId, EmailTemplateDto[].class);

        if (emailTemplateDtos == null || emailTemplateDtos.length == 0) {
            return new ArrayList<>();
        }

        return List.of(emailTemplateDtos);
    }


}
