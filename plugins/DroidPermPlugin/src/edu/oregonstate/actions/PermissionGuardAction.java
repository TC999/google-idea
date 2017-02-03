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
 * @author Nicholas Nelson <nelsonni@oregonstate.edu> Created on on 3/22/16.
 *
 * TODO: Handle user-defined permissions which extend beyond android.Manifest.*
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
