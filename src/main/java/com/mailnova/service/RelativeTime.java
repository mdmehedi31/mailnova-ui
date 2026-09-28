package com.mailnova.service;

import java.time.Duration;
import java.time.LocalDateTime;

/** "2h ago", "1d ago" formatting used on cards. */
public final class RelativeTime {
    private RelativeTime() { }

    public static String format(LocalDateTime t) {
        Duration d = Duration.between(t, LocalDateTime.now());
        long mins = Math.max(0, d.toMinutes());
        if (mins < 1) return "just now";
        if (mins < 60) return mins + "m ago";
        if (mins < 60 * 24) return (mins / 60) + "h ago";
        if (mins < 60 * 24 * 7) return (mins / (60 * 24)) + "d ago";
        return (mins / (60 * 24 * 7)) + "w ago";
    }
}
