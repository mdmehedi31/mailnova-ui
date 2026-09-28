package com.mailnova.view.templatecreate.dialog;

import com.mailnova.view.templatecreate.Ui;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;

/** Header / scrollable body / footer frame shared by every screen of the Create Template dialog. */
public class DialogShell extends Div {

    private final Div header = Ui.div("mn-dlg-head");
    private final Div body = Ui.div("mn-dlg-body");
    private final Div footer = Ui.div("mn-dlg-foot");

    public DialogShell() {
        addClassName("mn-dlg-shell");
        add(header, body, footer);
    }

    public void set(Component headerContent, Component bodyContent, Component footerContent) {
        header.removeAll();
        body.removeAll();
        footer.removeAll();
        header.setVisible(headerContent != null);
        footer.setVisible(footerContent != null);
        if (headerContent != null) header.add(headerContent);
        body.add(bodyContent);
        if (footerContent != null) footer.add(footerContent);
    }

    public void setBodyClass(String cls) {
        body.setClassName("mn-dlg-body " + cls);
    }
}
