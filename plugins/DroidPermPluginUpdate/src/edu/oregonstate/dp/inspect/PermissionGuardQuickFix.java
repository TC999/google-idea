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

package edu.oregonstate.dp.inspect;

import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import edu.oregonstate.actions.PermissionGuardFactory;
import edu.oregonstate.dp.inspect.jaxb.JaxbStmt;
import edu.oregonstate.util.PluginUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * DP-Detect side of DroidPerm - Unused
 * We may use DP-Detect in the future but for now it is not mature enough to be used with DP-Transform
 */

class PermissionGuardQuickFix extends LocalQuickFixAndIntentionActionOnPsiElement {

    private JaxbStmt jaxbStmt;
    private String permission;

    PermissionGuardQuickFix(@Nullable PsiElement element, @NotNull JaxbStmt jaxbStmt) {
        super(element);
        this.jaxbStmt = jaxbStmt;
        this.permission = jaxbStmt.getUncheckedPermissions().iterator().next();
    }

    @Override
    public void invoke(@NotNull Project project, @NotNull PsiFile file,
                       @Nullable("is null when called from inspection") Editor editor,
                       @NotNull PsiElement startElement,
                       @NotNull PsiElement endElement) {

        PluginUtil.openFileInEditor(project, file);
        OpenFileDescriptor fileDesc =
                new OpenFileDescriptor(project, file.getVirtualFile(), startElement.getTextOffset());
        Editor openEditor = FileEditorManager.getInstance(project).openTextEditor(fileDesc, true);
        assert openEditor != null;

        int startOffset = startElement.getTextOffset();
        int endOffset = endElement.getTextOffset() + endElement.getTextLength();
        openEditor.getSelectionModel().setSelection(startOffset, endOffset);

        //DataContext dataContext = DataManager.getInstance().getDataContext(openEditor.getComponent());
        PermissionGuardFactory.getInstance(project, editor).addPermission(permission, true);
    }

    @NotNull
    @Override
    public String getText() {
        return "Insert guards for " + permission;
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return "Insert permission guard getFamilyName()";
    }
}
