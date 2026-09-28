package com.mailnova.view.templatecreate;

import com.mailnova.service.TemplateService;
import com.mailnova.view.campaign.model.EmailTemplate;
import com.mailnova.view.campaign.model.TemplateTaxonomy;
import com.mailnova.view.campaign.model.Visibility;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/** Left column: Collections, By Type, By Visibility — each row shows a live count. */
public class CollectionsPanel extends Div {

    public enum Kind { ALL, FAVORITES, RECENT, TYPE, VISIBILITY }

    public record Selection(Kind kind, String value) { }

    private record Row(Selection selection, Div el, Span count, Predicate<EmailTemplate> counter) { }

    private static final Map<String, VaadinIcon> TYPE_ICONS = Map.of(
            "HR", VaadinIcon.USER_CARD, "Marketing", VaadinIcon.BELL, "Sales", VaadinIcon.BRIEFCASE,
            "Customer Support", VaadinIcon.HEADSET, "Others", VaadinIcon.ELLIPSIS_CIRCLE_O);

    private final TemplateService service;
    private final Consumer<Selection> onSelect;
    private final List<Row> rows = new ArrayList<>();
    private Selection current = new Selection(Kind.ALL, null);

    public CollectionsPanel(TemplateService service, Consumer<Selection> onSelect) {
        this.service = service;
        this.onSelect = onSelect;
        addClassName("mn-collections");

        add(Ui.span("Collections", "mn-col-title"));
        addRow(VaadinIcon.FILE_TEXT_O, "All Templates", new Selection(Kind.ALL, null), t -> true);
        addRow(VaadinIcon.STAR_O, "Favorites", new Selection(Kind.FAVORITES, null), EmailTemplate::isFavorite);
        addRow(VaadinIcon.CLOCK, "Recently Used", new Selection(Kind.RECENT, null),
                t -> t.getLastUsedAt().isAfter(LocalDateTime.now().minusDays(7)));

        add(Ui.div("mn-col-sep"), Ui.span("By Type", "mn-col-title"));
        for (String type : TemplateTaxonomy.types()) {
            addRow(TYPE_ICONS.getOrDefault(type, VaadinIcon.FOLDER_O), type,
                    new Selection(Kind.TYPE, type), t -> type.equals(t.getType()));
        }

        add(Ui.div("mn-col-sep"), Ui.span("By Visibility", "mn-col-title"));
        addRow(VaadinIcon.CHECK_CIRCLE_O, "Public", new Selection(Kind.VISIBILITY, "PUBLIC"),
                t -> t.getVisibility() == Visibility.PUBLIC);
        addRow(VaadinIcon.LOCK, "Private", new Selection(Kind.VISIBILITY, "PRIVATE"),
                t -> t.getVisibility() == Visibility.PRIVATE);
        refresh();
    }

    private void addRow(VaadinIcon icon, String label, Selection sel, Predicate<EmailTemplate> counter) {
        Span count = Ui.span("0", "mn-col-count");
        Div el = Ui.div("mn-col-row", Ui.icon(icon, "16px"), Ui.span(label, "mn-col-label"), count);
        el.getElement().setAttribute("role", "button");
        el.getElement().setAttribute("tabindex", "0");
        el.addClickListener(e -> select(sel, true));
        rows.add(new Row(sel, el, count, counter));
        add(el);
    }

    public void select(Selection sel, boolean notify) {
        current = sel;
        refresh();
        if (notify) onSelect.accept(sel);
    }

    /** Recomputes counts and highlights the active row. */
    public void refresh() {
        for (Row r : rows) {
            r.count().setText(String.valueOf(service.count(r.counter())));
            r.el().setClassName("mn-col-row-active", r.selection().equals(current));
        }
    }
}
