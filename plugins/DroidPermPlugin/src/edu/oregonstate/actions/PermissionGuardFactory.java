package edu.oregonstate.actions;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import edu.oregonstate.refactorings.PermissionGuardRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * @author Nicholas Nelson <nelsonni@oregonstate.edu> Created on on 3/22/16.
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
