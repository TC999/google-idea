package edu.oregonstate.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Nicholas Nelson <nelsonni@oregonstate.edu> Created on on 8/18/16.
 */

public class PermissionSelectionDialog extends DialogWrapper {
    private final PermissionSelectionComponent dialog;

    public PermissionSelectionDialog(@Nullable Project project, boolean callbackAvailable) {
        super(project);
        setTitle("Select Permission to Convert");
        setModal(true);
        dialog = new PermissionSelectionComponent(callbackAvailable);
        Disposer.register(myDisposable, dialog::dispose);
        setOKButtonText("OK");
        setCancelButtonText("Cancel");
        init();
    }

    @NotNull
    @Override
    protected Action[] createActions() {
        return new Action[]{getOKAction(), getCancelAction()};
    }

    @Override
    protected void doOKAction() {
        processDoNotAskOnOk(OK_EXIT_CODE);

        if (getOKAction().isEnabled()) {
            dialog.saveResults();
            close(OK_EXIT_CODE);
        }
    }

    @Override
    public void doCancelAction() {
        if (getCancelAction().isEnabled()) {
            close(CANCEL_EXIT_CODE);
        }
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return dialog.getRootPane();
    }

    @Override
    protected String getDimensionServiceKey() {
        return getClass().getName();
    }

    public boolean show(@Nullable Project project, boolean callbackAvailable) {
        PermissionSelectionDialog permissionSelectionDialog = new PermissionSelectionDialog(project, callbackAvailable);
        return permissionSelectionDialog.showAndGet();
    }

}
