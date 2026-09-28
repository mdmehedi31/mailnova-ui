package com.mailnova.view.components;

import com.vaadin.flow.component.Html;

/**
 * A simple inline SVG donut chart with up to three segments.
 * Values are expressed as percentages that should sum to (approximately) 100.
 */
public class DonutChart extends Html {

    private static final double RADIUS = 70;
    private static final double CIRCUMFERENCE = 2 * Math.PI * RADIUS;

    public DonutChart(double deliveredPct, double bouncedPct, double failedPct,
                       String deliveredColor, String bouncedColor, String failedColor,
                       String centerLabel, String centerCaption) {
        super(buildSvg(deliveredPct, bouncedPct, failedPct, deliveredColor, bouncedColor, failedColor,
                centerLabel, centerCaption));
    }

    private static String buildSvg(double deliveredPct, double bouncedPct, double failedPct,
                                    String deliveredColor, String bouncedColor, String failedColor,
                                    String centerLabel, String centerCaption) {
        double deliveredLen = CIRCUMFERENCE * (deliveredPct / 100.0);
        double bouncedLen = CIRCUMFERENCE * (bouncedPct / 100.0);
        double failedLen = CIRCUMFERENCE * (failedPct / 100.0);

        double bouncedOffset = -deliveredLen;
        double failedOffset = -(deliveredLen + bouncedLen);

        return "<svg viewBox='0 0 200 200' width='220' height='220' role='img' "
                + "aria-label='Donut chart showing delivery breakdown'>"
                + circle("#EEF2FF", CIRCUMFERENCE, CIRCUMFERENCE, 0)
                + circle(deliveredColor, deliveredLen, CIRCUMFERENCE, 0)
                + circle(bouncedColor, bouncedLen, CIRCUMFERENCE, bouncedOffset)
                + circle(failedColor, failedLen, CIRCUMFERENCE, failedOffset)
                + "<text x='100' y='96' text-anchor='middle' style='font-size:22px;font-weight:700;fill:#0F172A;font-family:Inter,sans-serif;'>"
                + centerLabel + "</text>"
                + "<text x='100' y='116' text-anchor='middle' style='font-size:12px;fill:#64748B;font-family:Inter,sans-serif;'>"
                + centerCaption + "</text>"
                + "</svg>";
    }

    private static String circle(String color, double dashLength, double circumference, double dashOffset) {
        return String.format(java.util.Locale.US,
                "<circle cx='100' cy='100' r='%.0f' fill='none' stroke='%s' stroke-width='30' "
                        + "stroke-dasharray='%.2f %.2f' stroke-dashoffset='%.2f' transform='rotate(-90 100 100)'/>",
                RADIUS, color, dashLength, circumference, dashOffset);
    }
}
