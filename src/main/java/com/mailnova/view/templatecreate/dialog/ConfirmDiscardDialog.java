package com.mailnova.view.templatecreate.dialog;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

/** Reusable "Discard changes?" confirmation. */
public class ConfirmDiscardDialog extends ConfirmDialog {

    public ConfirmDiscardDialog(Runnable onDiscard) {
        setHeader("Discard changes?");
        setText("You have unsaved changes. If you close now, they'll be lost.");
        setCancelable(true);
        setCancelText("Keep editing");
        setConfirmText("Discard");
        setConfirmButtonTheme("error primary");
        addClassName("mn-confirm");
        addConfirmListener(e -> onDiscard.run());
    }
}
