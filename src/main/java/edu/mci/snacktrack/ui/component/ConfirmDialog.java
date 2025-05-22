package edu.mci.snacktrack.ui.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

public class ConfirmDialog extends Dialog {

    private final Button yesButton = new Button("Yes");
    private final Button noButton = new Button("No");

    public ConfirmDialog(String message, Runnable onConfirm, Runnable onCancel) {
        setCloseOnOutsideClick(false);

        Span msg = new Span(message);
        Div buttonLayout = new Div(yesButton, noButton);
        buttonLayout.getStyle().set("display", "flex").set("gap", "1rem").set("justify-content", "center");

        yesButton.addClickListener(e -> {
            close();
            if (onConfirm != null) onConfirm.run();
        });

        noButton.addClickListener(e -> {
            close();
            if (onCancel != null) onCancel.run();
        });

        add(msg, buttonLayout);
    }
}
