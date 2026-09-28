package com.mailnova.view.templatecreate;

import com.mailnova.DTO.EmailTemplateDto;

/** Callbacks a card/panel raises; the parent view owns data + navigation. */
public interface TemplateActions {
    void use(EmailTemplateDto t);
    void preview(EmailTemplateDto t);
    void edit(EmailTemplateDto t);
    void duplicate(EmailTemplateDto t);
    void delete(EmailTemplateDto t);
    void toggleFavorite(EmailTemplateDto t);
}
