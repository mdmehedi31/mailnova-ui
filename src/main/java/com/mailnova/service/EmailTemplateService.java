package com.mailnova.service;


import com.mailnova.DTO.EmailTemplateDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailTemplateService {


    public List<EmailTemplateDto> getEmailTemplatesByUser(Integer userId) {
        List<EmailTemplateDto> emailTemplateDtos = new ArrayList<>();
        return emailTemplateDtos;
    }
}
