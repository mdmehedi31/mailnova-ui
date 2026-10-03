package com.mailnova.service;


import com.mailnova.DTO.EmailTemplateDto;
import com.mailnova.endpoint.TemplateCreateCaller;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailTemplateService {


    private TemplateCreateCaller emailTemplateCaller;

    public EmailTemplateService(TemplateCreateCaller emailTemplateCaller) {
        this.emailTemplateCaller = emailTemplateCaller;
    }


    public EmailTemplateDto createEmailTemplate(EmailTemplateDto emailTemplateDto) {
        EmailTemplateDto emailTemplate =emailTemplateCaller.createTemplate(emailTemplateDto);
        return emailTemplate;
    }

    public EmailTemplateDto updateEmailTemplate(EmailTemplateDto emailTemplateDto) {
        EmailTemplateDto updateEmailTemplate = emailTemplateCaller.updateEmailTemplate(emailTemplateDto);
        return updateEmailTemplate;
    }

    public List<EmailTemplateDto> getAllEmailTemplates(Integer creatorId) {
        List<EmailTemplateDto> emailTemplateDtos = emailTemplateCaller.getEmailTemplatesByCreator(creatorId);
        return emailTemplateDtos;
    }

}
