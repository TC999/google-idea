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

package edu.oregonstate.actions;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import edu.oregonstate.refactorings.PermissionGuardRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * Factory class for PermissionGuardRunnable
 * Called by PermissionGuardAction
 * Performs actual write action
 */

public class PermissionGuardFactory {

    private final Project project;
    private final Editor editor;

    private PermissionGuardFactory(Project project, Editor editor) {
        this.editor = editor;
        this.project = project;
    }

    public static PermissionGuardFactory getInstance(Project project, Editor editor) {
        return new PermissionGuardFactory(project, editor);
    }

    public void addPermission(String permission, boolean insertRequest) {
        PermissionGuardRunnable job = new PermissionGuardRunnable(this.project,
                this.editor, permission, insertRequest);
        executeRunnable(job, "Runtime Permission Conversion");
    }

    private void executeRunnable(@NotNull Runnable job, String title) {
        ApplicationManager.getApplication().invokeLater(() ->
                CommandProcessor.getInstance().executeCommand(project, () ->
                        ApplicationManager.getApplication().runWriteAction(job), title, null));
    }

}
