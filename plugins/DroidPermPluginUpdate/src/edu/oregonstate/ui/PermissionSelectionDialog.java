/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.oregonstate.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Logic for permission selection dialog box
 * Triggers Permission guard action when OK is pressed
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
