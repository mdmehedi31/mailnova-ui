package com.mailnova.view.dashboard;

/**
 * A company that owns email campaigns.
 *
 * @param initial   single letter shown in the avatar
 * @param name      display name
 * @param colorHex  background color of the avatar, e.g. "#2563EB"
 * @param active    number of active campaigns
 * @param drafts    number of draft campaigns
 */
public record CompanyData(String initial, String name, String colorHex, int active, int drafts) {
}
