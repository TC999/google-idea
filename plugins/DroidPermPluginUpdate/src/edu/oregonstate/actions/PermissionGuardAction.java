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

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import edu.oregonstate.templates.BaseTemplate;
import edu.oregonstate.ui.PermissionSelectionDialog;
import edu.oregonstate.util.ManifestWrapper;
import edu.oregonstate.util.PermissionsUtil;
import edu.oregonstate.util.PluginUtil;

/**
 * Point of Entry for PermissionGuardRunnable execution
 * actionPerformed is called when users invoke "Convert to Android Runtime Permissions"
 * Update is called a few times a second to determine if "Converto to Android Runtime Permissions" is clickable
 */

public class PermissionGuardAction extends AnAction {


    @Override
    public void actionPerformed(AnActionEvent e) {
        new ManifestWrapper(); // generate static lists of permissions and permission groups
        final DataContext dataContext = e.getDataContext();
        final Project project = CommonDataKeys.PROJECT.getData(dataContext);
        final Editor editor = CommonDataKeys.EDITOR.getData(dataContext);

        final BaseTemplate template = PluginUtil.getTemplateFromContext(editor, project);
        PermissionSelectionDialog dialog = new PermissionSelectionDialog(project,
                PermissionsUtil.templateContainsCallback(template));
        if (dialog.show(project, PermissionsUtil.templateContainsCallback(template))) {
            SelectionService service = SelectionService.getInstance();
            PermissionGuardFactory.getInstance(project, editor).addPermission(service.permissionRequested,
                    service.insertRequest);
        }
    }

    @Override
    public void update(AnActionEvent actionEvent) {
        Editor editor = actionEvent.getData(CommonDataKeys.EDITOR);
        PsiFile psiFile = actionEvent.getData(CommonDataKeys.PSI_FILE);
        actionEvent.getPresentation().setEnabled(PluginUtil.preconditions(psiFile, editor));
    }


}
