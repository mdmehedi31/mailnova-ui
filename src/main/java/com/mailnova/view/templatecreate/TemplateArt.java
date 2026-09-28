package com.mailnova.view.templatecreate;


import com.mailnova.view.campaign.model.TemplateSource;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.Div;

/** Decorative art reproduced from the design images (mini email thumbnails, hero, logo). */
public final class TemplateArt {
    private TemplateArt() { }

    /** Mini "document" thumbnail on the right of a card. AI = text lines, Manual = picture + lines. */
    public static Div thumb(TemplateSource source) {
        Div t = Ui.div("mn-thumb " + (source == TemplateSource.AI ? "mn-thumb-ai" : "mn-thumb-manual"));
        if (source == TemplateSource.MANUAL) {
            Div img = Ui.div("mn-thumb-img", Ui.div("mn-thumb-sun"), Ui.div("mn-thumb-hill"));
            t.add(img);
        } else {
            t.add(Ui.div("mn-thumb-line mn-w60"), Ui.div("mn-thumb-line mn-w90"));
        }
        t.add(Ui.div("mn-thumb-line mn-w90"), Ui.div("mn-thumb-line mn-w70"), Ui.div("mn-thumb-line mn-w50"));
        return t;
    }

    /** MailNova logo mark. */
    public static Html logo() {
        return new Html("<span class=\"mn-logo\"><svg width=\"30\" height=\"30\" viewBox=\"0 0 30 30\">"
                + "<defs><linearGradient id=\"lg1\" x1=\"0\" y1=\"0\" x2=\"1\" y2=\"1\">"
                + "<stop offset=\"0\" stop-color=\"#3B82F6\"/><stop offset=\"1\" stop-color=\"#6366F1\"/></linearGradient></defs>"
                + "<path d=\"M3 8 L15 17 L27 8 V22 a2 2 0 0 1 -2 2 H5 a2 2 0 0 1 -2 -2 Z\" fill=\"url(#lg1)\"/>"
                + "<path d=\"M3 8 a2 2 0 0 1 2 -2 H25 a2 2 0 0 1 2 2 L15 17 Z\" fill=\"#EC4899\" opacity=\".9\"/>"
                + "</svg></span>");
    }

    /** Hero illustration in the page header: envelope with document, sparkles and AI badge. */
    public static Html hero() {
        return new Html("<span class=\"mn-hero-art\"><svg width=\"190\" height=\"110\" viewBox=\"0 0 190 110\">"
                + "<defs><linearGradient id=\"hg1\" x1=\"0\" y1=\"0\" x2=\"1\" y2=\"1\">"
                + "<stop offset=\"0\" stop-color=\"#22D3EE\"/><stop offset=\"1\" stop-color=\"#A78BFA\"/></linearGradient>"
                + "<linearGradient id=\"hg2\" x1=\"0\" y1=\"0\" x2=\"1\" y2=\"1\">"
                + "<stop offset=\"0\" stop-color=\"#6366F1\"/><stop offset=\"1\" stop-color=\"#8B5CF6\"/></linearGradient></defs>"
                + "<circle cx=\"118\" cy=\"56\" r=\"44\" fill=\"url(#hg1)\" opacity=\".35\"/>"
                + "<rect x=\"88\" y=\"14\" width=\"56\" height=\"50\" rx=\"6\" fill=\"#fff\" stroke=\"#E0E7FF\"/>"
                + "<rect x=\"96\" y=\"24\" width=\"26\" height=\"4\" rx=\"2\" fill=\"#C7D2FE\"/>"
                + "<rect x=\"96\" y=\"34\" width=\"40\" height=\"4\" rx=\"2\" fill=\"#E0E7FF\"/>"
                + "<rect x=\"96\" y=\"44\" width=\"34\" height=\"4\" rx=\"2\" fill=\"#E0E7FF\"/>"
                + "<path d=\"M70 58 L112 86 L154 58 V96 a6 6 0 0 1 -6 6 H76 a6 6 0 0 1 -6 -6 Z\" fill=\"url(#hg2)\"/>"
                + "<path d=\"M70 58 L112 30 L154 58 L112 86 Z\" fill=\"#818CF8\" opacity=\".85\"/>"
                + "<circle cx=\"150\" cy=\"88\" r=\"12\" fill=\"#A855F7\" stroke=\"#fff\" stroke-width=\"3\"/>"
                + "<text x=\"150\" y=\"92\" font-size=\"9\" font-weight=\"700\" fill=\"#fff\" text-anchor=\"middle\">AI</text>"
                + "<path d=\"M52 34 l3 8 8 3 -8 3 -3 8 -3 -8 -8 -3 8 -3 Z\" fill=\"#6366F1\"/>"
                + "<path d=\"M160 22 l2 5 5 2 -5 2 -2 5 -2 -5 -5 -2 5 -2 Z\" fill=\"#F472B6\"/>"
                + "</svg></span>");
    }

    /** Small envelope used in the AI review dialog header. */
    public static Html envelope() {
        return new Html("<span class=\"mn-env-art\"><svg width=\"64\" height=\"52\" viewBox=\"0 0 64 52\">"
                + "<defs><linearGradient id=\"eg1\" x1=\"0\" y1=\"0\" x2=\"1\" y2=\"1\">"
                + "<stop offset=\"0\" stop-color=\"#818CF8\"/><stop offset=\"1\" stop-color=\"#6366F1\"/></linearGradient></defs>"
                + "<rect x=\"18\" y=\"2\" width=\"32\" height=\"26\" rx=\"4\" fill=\"#fff\" stroke=\"#E0E7FF\"/>"
                + "<rect x=\"23\" y=\"9\" width=\"14\" height=\"3\" rx=\"1.5\" fill=\"#C7D2FE\"/>"
                + "<rect x=\"23\" y=\"16\" width=\"22\" height=\"3\" rx=\"1.5\" fill=\"#E0E7FF\"/>"
                + "<path d=\"M6 22 L34 40 L62 22 V44 a5 5 0 0 1 -5 5 H11 a5 5 0 0 1 -5 -5 Z\" fill=\"url(#eg1)\"/>"
                + "</svg></span>");
    }
}
