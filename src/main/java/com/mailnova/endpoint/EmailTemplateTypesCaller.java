package com.mailnova.endpoint;


import com.mailnova.DTO.EmailTemplateTypeDTO;
import com.mailnova.restEndPointCaller.RestEndPointCaller;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailTemplateTypesCaller extends RestEndPointCaller {

    public List<EmailTemplateTypeDTO> getEmailTemplates(){

        EmailTemplateTypeDTO[] emailTemplateTypes= getCaller(EmailTemplateEndpointPath.GET_EMAIL_TEMPLATES, EmailTemplateTypeDTO[].class);

        if (emailTemplateTypes == null || emailTemplateTypes.length == 0)
            return new ArrayList<>();

        return List.of(emailTemplateTypes);
    }
}
