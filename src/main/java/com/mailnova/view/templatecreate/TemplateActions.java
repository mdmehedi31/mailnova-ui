package com.mailnova.view.templatecreate;


import com.mailnova.view.campaign.model.EmailTemplate;

/** Callbacks a card/panel raises; the parent view owns data + navigation. */
public interface TemplateActions {
    void use(EmailTemplate t);
    void preview(EmailTemplate t);
    void edit(EmailTemplate t);
    void duplicate(EmailTemplate t);
    void delete(EmailTemplate t);
    void toggleFavorite(EmailTemplate t);
}
