package com.mailnova.view.templatecreate;

import com.mailnova.DTO.EmailCampaignDto;
import com.mailnova.DTO.EmailTemplateDto;
import com.mailnova.DTO.EmailTemplateTypeDTO;
import com.mailnova.service.TemplateService;
import com.mailnova.view.MainLayout;
import com.mailnova.view.campaign.model.*;
import com.mailnova.view.templatecreate.dialog.CreateTemplateDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Template Management module. Layout follows the design: header, KPI row, quick actions,
 * [collections | search + chips + recent + all templates], and the right-hand preview panel.
 * Create Template opens a dialog on top of this page — the page is never left.
 */
@Route(value = "template/management", layout = MainLayout.class)
@PageTitle("Email Templates · MailNova")
public class TemplateManagementView extends Div implements TemplateActions {

    private static final String ALL_TYPES = "All Types";
    private static final String ALL_SUBTYPES = "All Subtypes";

    private final TemplateService service;

    // filter state
    private String text = "";
    private String type;
    private String subtype;
    private TemplateFilter.Chip chip = TemplateFilter.Chip.ALL;
    private TemplateFilter.Collection collection = TemplateFilter.Collection.ALL;
    private Visibility visibility;
    private boolean listMode;
    private EmailTemplateDto selected;
    private boolean syncing;

    // widgets
    private final Div stats = Ui.div("mn-stats");
    private final Div recent = Ui.div("mn-cards-row");
    private final Div grid = Ui.div("mn-grid");
    private final com.vaadin.flow.component.html.Span gridCount = Ui.span("", "mn-grid-count");
    private final Select<String> typeSelect = new Select<>();
    private final Select<String> subtypeSelect = new Select<>();
    private final Map<TemplateFilter.Chip, Button> chips = new EnumMap<>(TemplateFilter.Chip.class);
    private final Button gridBtn = Ui.plainIconButton(VaadinIcon.GRID_SMALL, "Grid view", "mn-view-btn");
    private final Button listBtn = Ui.plainIconButton(VaadinIcon.LIST, "List view", "mn-view-btn");
    private final CollectionsPanel collections;
    private final TemplatePreviewPanel preview;
    private final Div body = Ui.div("mn-body");
    private List<EmailTemplateTypeDTO> templateTypes;
    public TemplateManagementView(TemplateService service) {
        this.service = service;
        addClassName("mn-view");
        collections = new CollectionsPanel(service, this::onCollection);
        preview = new TemplatePreviewPanel(this, this::closePreview);

        templateTypes= service.getEmailTemplateTypes();
        Div center = Ui.div("mn-center", stats, new QuickActions(this::openManual, this::openAi,
                () -> Notification.show("Import is coming soon"), () -> Notification.show("Public library is coming soon")),
                Ui.div("mn-browse", collections, Ui.div("mn-main", toolbar(), chipRow(), recentSection(), allSection())));
        body.add(center, preview);
        add(header(), body);

        selected =null; //service.findAll().stream().findFirst().orElse(null);
        refresh();
    }

    // ------------------------------------------------------------------ layout pieces

    private Div header() {
        Button create = Ui.primary("Create Template");
        create.setPrefixComponent(Ui.icon(VaadinIcon.PLUS, "14px"));
        create.setSuffixComponent(Ui.icon(VaadinIcon.ANGLE_DOWN, "12px"));
        create.addClassName("mn-create-btn");
        create.addClickListener(e -> new CreateTemplateDialog(service, this::afterSave).openChooser());
        return Ui.div("mn-header",
                Ui.div("mn-header-copy", Ui.span("Email Templates", "mn-page-title"),
                        Ui.span("Create and manage reusable templates for HR, Marketing, Sales and customer communication.", "mn-page-sub")),
                TemplateArt.hero(), create);
    }

    private Div toolbar() {
        TextField search = new TextField();
        search.setPlaceholder("Search templates...");
        search.setPrefixComponent(Ui.icon(VaadinIcon.SEARCH, "15px"));
        search.setValueChangeMode(ValueChangeMode.LAZY);
        search.addClassName("mn-search");
        search.addValueChangeListener(e -> { text = e.getValue(); refresh(); });

        List<String> types = new ArrayList<>(List.of(ALL_TYPES));
        types.addAll(TemplateTaxonomy.types());
        typeSelect.setItems(types);
        typeSelect.setValue(ALL_TYPES);
        typeSelect.addClassNames("mn-select");
        typeSelect.addValueChangeListener(e -> {
            if (syncing) return;
            type = ALL_TYPES.equals(e.getValue()) ? null : e.getValue();
            subtype = null;
            collections.select(type == null ? new CollectionsPanel.Selection(CollectionsPanel.Kind.ALL, null)
                    : new CollectionsPanel.Selection(CollectionsPanel.Kind.TYPE, type), false);
            visibility = null;
            collection = TemplateFilter.Collection.ALL;
            refresh();
        });

        subtypeSelect.addClassName("mn-select");
        subtypeSelect.addValueChangeListener(e -> {
            if (syncing) return;
            subtype = ALL_SUBTYPES.equals(e.getValue()) ? null : e.getValue();
            refresh();
        });
        syncSubtypes();
        return Ui.div("mn-toolbar", search, typeSelect, subtypeSelect);
    }

    private void syncSubtypes() {
        syncing = true;
        List<String> subs = new ArrayList<>(List.of(ALL_SUBTYPES));
        subs.addAll(type == null ? TemplateTaxonomy.allSubtypes() : TemplateTaxonomy.subtypesOf(type));
        subtypeSelect.setItems(subs);
        subtypeSelect.setValue(subtype == null ? ALL_SUBTYPES : subtype);
        typeSelect.setValue(type == null ? ALL_TYPES : type);
        syncing = false;
    }

    private Div chipRow() {
        Div row = Ui.div("mn-chips");
        for (TemplateFilter.Chip c : TemplateFilter.Chip.values()) {
            String label = switch (c) { case ALL -> "All"; case MANUAL -> "Manual"; case AI -> "AI Generated";
                case PUBLIC -> "Public"; case PRIVATE -> "Private"; };
            Button b = new Button(label);
            b.addClassName("mn-chip");
            b.addClickListener(e -> { chip = c; refresh(); });
            chips.put(c, b);
            row.add(b);
        }
        return row;
    }

    private Div recentSection() {
        Button viewAll = new Button("View all", Ui.icon(VaadinIcon.ARROW_RIGHT, "12px"));
        viewAll.setIconAfterText(true);
        viewAll.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        viewAll.addClassName("mn-link-btn");
        viewAll.addClickListener(e -> {
            collection = TemplateFilter.Collection.RECENT;
            collections.select(new CollectionsPanel.Selection(CollectionsPanel.Kind.RECENT, null), false);
            refresh();
        });
        return Ui.div("mn-section",
                Ui.div("mn-section-head", Ui.div("mn-section-title", Ui.icon(VaadinIcon.CLOCK, "16px"),
                        Ui.span("Recently Used Templates", "")), viewAll),
                recent);
    }

    private Div allSection() {
        gridBtn.addClickListener(e -> { listMode = false; refresh(); });
        listBtn.addClickListener(e -> { listMode = true; refresh(); });
        return Ui.div("mn-section",
                Ui.div("mn-section-head",
                        Ui.div("mn-all-titles", Ui.span("All Templates", "mn-section-title-text"), gridCount),
                        Ui.div("mn-view-toggle", gridBtn, listBtn)),
                grid);
    }

    // ------------------------------------------------------------------ state -> UI

    private void onCollection(CollectionsPanel.Selection sel) {
        type = null; subtype = null; visibility = null; collection = TemplateFilter.Collection.ALL;
        switch (sel.kind()) {
            case FAVORITES -> collection = TemplateFilter.Collection.FAVORITES;
            case RECENT -> collection = TemplateFilter.Collection.RECENT;
            case TYPE -> type = sel.value();
            case VISIBILITY -> visibility = Visibility.valueOf(sel.value());
            default -> { }
        }
        syncSubtypes();
        refresh();
    }

    private void refresh() {
        TemplateFilter filter = new TemplateFilter(text, type, subtype, chip, collection, visibility);
        List<EmailTemplateDto> results = new ArrayList<>(); //service.search(filter);

        stats.removeAll();
        stats.add(new StatCard(VaadinIcon.FILE_TEXT, "green", "Total Templates", String.valueOf(service.findAll().size()), "↑ 12%"),
                new StatCard(VaadinIcon.PENCIL, "blue", "Manual Templates",
                        String.valueOf(service.count(t -> t.getSource() == TemplateSource.MANUAL)), "↑ 8%"),
                new StatCard(VaadinIcon.MAGIC, "purple", "AI Generated",
                        String.valueOf(service.count(t -> t.getSource() == TemplateSource.AI)), "↑ 20%"));
        service.mostUsed().ifPresent(m -> stats.add(StatCard.mostUsed(m.getName(), m.getUses())));
        stats.add(new StatCard(VaadinIcon.PAPERPLANE, "teal", "Sent Using Templates", "12,430", "↑ 18%"));

        chips.forEach((c, b) -> b.setClassName("mn-chip-active", c == chip));
        collections.refresh();

        recent.removeAll();/*
        service.recentlyUsed(4).forEach(t ->
                recent.add(new TemplateCard(t, TemplateCard.Mode.COMPACT, t.equals(selected), this)));*/

        grid.removeAll();
        grid.setClassName("mn-grid-list", listMode);
        results.forEach(t -> grid.add(new TemplateCard(t,TemplateCard.Mode.FULL, t.equals(selected), this)));
        if (results.isEmpty()) {
            grid.add(Ui.div("mn-empty", Ui.span("No templates match your filters", "mn-empty-title"),
                    Ui.span("Try clearing the search or filters, or create a new template.", "mn-empty-sub")));
        }
        gridCount.setText(results.size() + " templates");
        gridBtn.setClassName("mn-view-active", !listMode);
        listBtn.setClassName("mn-view-active", listMode);

        EmailTemplateTypeDTO emailTemplateTypeDTO= null;
        if (selected != null && selected.getTypeId() != null) {
            emailTemplateTypeDTO= templateTypes.stream().filter(e->e.getId().equals(selected.getTypeId())).findAny().orElse(null);

        }

        preview.show(selected != null && service.findAll().contains(selected) ? selected : null,emailTemplateTypeDTO);
    }

    private void closePreview() {
        selected = null;
        body.addClassName("mn-preview-closed");
        refresh();
    }

    // ------------------------------------------------------------------ dialog launchers

    private void openManual() { new CreateTemplateDialog(service, this::afterSave).openManual(); }

    private void openAi() { new CreateTemplateDialog(service, this::afterSave).openAi(); }

    private void afterSave(EmailTemplateDto t) {
        selected = t;
        body.removeClassName("mn-preview-closed");
        refresh();
        Notification.show("\"" + t.getName() + "\" saved");
    }

    // ------------------------------------------------------------------ TemplateActions

    @Override public void use(EmailTemplateDto t) {
      //  service.markUsed(t);
        refresh();
        Notification.show("\"" + t.getName() + "\" is ready to use in a campaign");
    }

    @Override public void preview(EmailTemplateDto t) {
        selected = t;
        body.removeClassName("mn-preview-closed");
        refresh();
    }

    @Override
    public void edit(EmailTemplateDto t) {
        new CreateTemplateDialog(service, this::afterSave).openEdit(t);
    }

    /*   new CreateTemplateDialog(service, this::afterSave).openEdit(t);
   }
*/
    @Override public void duplicate(EmailTemplateDto t) {
        afterSave(service.duplicate(t));
    }

    @Override public void delete(EmailTemplateDto t) {
        ConfirmDialog d = new ConfirmDialog();
        d.setHeader("Delete template?");
        d.setText("\"" + t.getName() + "\" will be permanently removed.");
        d.setCancelable(true);
        d.setConfirmText("Delete");
        d.setConfirmButtonTheme("error primary");
       /* d.addConfirmListener(e -> {
            service.delete(t);
            if (t.equals(selected)) selected = null;
            refresh();
            Notification.show("Template deleted");
        });*/
        d.open();
    }

    @Override
    public void toggleFavorite(EmailTemplateDto t) {
       // service.toggleFavorite(t);
        refresh();
    }
}
