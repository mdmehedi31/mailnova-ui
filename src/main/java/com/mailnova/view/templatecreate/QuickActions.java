package com.mailnova.view.templatecreate;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;

/** "Quick Actions" strip under the stats. */
public class QuickActions extends Div {

    public QuickActions(Runnable manual, Runnable ai, Runnable importTemplate, Runnable browse) {
        addClassName("mn-quick");
        add(Ui.div("mn-quick-intro",
                Ui.span("Quick Actions", "mn-quick-title"),
                Ui.span("Get started with templates in seconds.", "mn-quick-sub")),
            Ui.div("mn-quick-tiles",
                tile(VaadinIcon.PENCIL, "blue", "Create Manually", "Build from scratch", manual),
                tile(VaadinIcon.MAGIC, "purple", "Generate with AI", "Let AI create for you", ai),
                tile(VaadinIcon.UPLOAD, "green", "Import Template", "Upload your own", importTemplate),
                tile(VaadinIcon.GLOBE, "blue", "Browse Public Templates", "Explore community templates", browse)));
    }

    private Div tile(VaadinIcon icon, String color, String title, String sub, Runnable action) {
        Div t = Ui.div("mn-quick-tile",
                Ui.div("mn-quick-icon mn-quick-" + color, Ui.icon(icon, "18px")),
                Ui.div("mn-quick-copy", Ui.span(title, "mn-quick-tile-title"), Ui.span(sub, "mn-quick-tile-sub")));
        t.getElement().setAttribute("role", "button");
        t.getElement().setAttribute("tabindex", "0");
        t.addClickListener(e -> action.run());
        return t;
    }
}
