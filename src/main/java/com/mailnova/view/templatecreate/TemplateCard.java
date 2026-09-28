package com.mailnova.view.templatecreate;


import com.mailnova.service.RelativeTime;
import com.mailnova.view.campaign.model.EmailTemplate;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;

/**
 * Template card. FULL matches the provided card images (thumbnail, visibility, action row);
 * COMPACT is the small variant used in "Recently Used Templates".
 */
public class TemplateCard extends Div {

    public enum Mode { FULL, COMPACT }

    public TemplateCard(EmailTemplate t, Mode mode, boolean selected, TemplateActions actions) {
        addClassNames("mn-tcard", mode == Mode.FULL ? "mn-tcard-full" : "mn-tcard-compact");
        if (selected) addClassName("mn-tcard-selected");

        // Whole card selects the template for the preview panel, but not when a button is clicked.
        getElement().addEventListener("click", e -> actions.preview(t))
                .setFilter("!event.target.closest('vaadin-button, vaadin-context-menu-item, vaadin-context-menu-overlay')");

        Button star = Ui.plainIconButton(t.isFavorite() ? VaadinIcon.STAR : VaadinIcon.STAR_O, "Favorite",
                t.isFavorite() ? "mn-star mn-star-on" : "mn-star");
        star.addClickListener(e -> actions.toggleFavorite(t));

        Div top = Ui.div("mn-tcard-top", new SourceBadge(t.getSource()));
        Div preview = Ui.div("mn-tcard-preview");
        t.previewLines().forEach(line -> preview.add(new Div(line)));

        if (mode == Mode.FULL) {
            top.add(star);
            add(top, TemplateArt.thumb(t.getSource()),
                Ui.span(t.getName(), "mn-tcard-title"),
                Ui.span(t.getType() + "  •  " + t.getSubtype(), "mn-tcard-meta"),
                preview, stats(t, true), actionRow(t, actions));
        } else {
            top.add(moreMenu(t, actions));
            Div foot = Ui.div("mn-tcard-foot", statsInline(t), star);
            add(top, Ui.span(t.getName(), "mn-tcard-title"),
                Ui.span(t.getType() + "  •  " + t.getSubtype(), "mn-tcard-meta"), preview, foot);
        }
    }

    private Div stats(EmailTemplate t, boolean withVisibility) {
        Div row = Ui.div("mn-tcard-stats");
        if (withVisibility) row.add(new VisibilityBadge(t.getVisibility()));
        row.add(statsInline(t));
        if (t.isDraft()) row.add(Ui.span("Draft", "mn-draft"));
        return row;
    }

    private Div statsInline(EmailTemplate t) {
        return Ui.div("mn-stat-inline",
                Ui.div("mn-chip-stat", Ui.icon(VaadinIcon.EYE, "13px"), new Span(t.getUses() + " uses")),
                Ui.div("mn-chip-stat", Ui.icon(VaadinIcon.CLOCK, "13px"), new Span(RelativeTime.format(t.getLastUsedAt()))));
    }

    private Div actionRow(EmailTemplate t, TemplateActions a) {
        Button use = Ui.primary("Use Template");
        use.addClassName("mn-use-btn");
        use.addClickListener(e -> a.use(t));
        Button eye = Ui.iconButton(VaadinIcon.EYE, "Preview");
        eye.addClickListener(e -> a.preview(t));
        Button edit = Ui.iconButton(VaadinIcon.PENCIL, "Edit");
        edit.addClickListener(e -> a.edit(t));
        Button copy = Ui.iconButton(VaadinIcon.COPY_O, "Duplicate");
        copy.addClickListener(e -> a.duplicate(t));
        return Ui.div("mn-tcard-actions", use, eye, edit, copy, moreMenu(t, a));
    }

    private Button moreMenu(EmailTemplate t, TemplateActions a) {
        Button more = Ui.plainIconButton(VaadinIcon.ELLIPSIS_DOTS_V, "More", "mn-more");
        ContextMenu menu = new ContextMenu(more);
        menu.setOpenOnClick(true);
        menu.addItem("Edit", e -> a.edit(t));
        menu.addItem("Duplicate", e -> a.duplicate(t));
        menu.addItem("Delete", e -> a.delete(t));
        return more;
    }
}
