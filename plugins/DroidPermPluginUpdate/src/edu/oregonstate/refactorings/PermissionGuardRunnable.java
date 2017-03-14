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

package edu.oregonstate.refactorings;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.QuickFixFactory;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.refactoring.extractMethod.ExtractMethodHandler;
import com.intellij.refactoring.extractMethod.ExtractMethodProcessor;
import com.intellij.refactoring.extractMethod.PrepareFailedException;
import com.intellij.refactoring.util.RefactoringUtil;
import edu.oregonstate.augmentation.TestHarness;
import edu.oregonstate.settings.NotificationsManager;
import edu.oregonstate.settings.PersistentSettings;
import edu.oregonstate.templates.BaseTemplate;
import edu.oregonstate.util.CallbackUtil;
import edu.oregonstate.util.ExtractionUtil;
import edu.oregonstate.util.PermissionsUtil;
import edu.oregonstate.util.PluginUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Runnable that contains high level logic for insertion of permission guard
 * Permission guards may be either Checks or Checks + Requests
 */
public class PermissionGuardRunnable implements Runnable {

    private Project project;
    private Editor editor;
    private String permission;

    private Document document;
    private SelectionModel selectionModel;
    private PsiFile psiFile;
    private PsiClass containingClass;
    private PsiClass anonymousClass;
    private PsiMethod containingMethod;

    private static Integer METHOD_EXTRACTION_LINES_LIMIT = 2;
    private final boolean insertRequest;
    private TestHarness LOG = TestHarness.getInstance();

    private boolean wasAnonymous = false;

    public PermissionGuardRunnable(Project project, Editor editor, @NotNull String permission, boolean insertRequest) {
        this.project = project;
        this.editor = editor;
        this.permission = PluginUtil.trimPermission(permission);
        this.insertRequest = insertRequest;
        updateRefs();
    }

    private void updateRefs() {
        assert editor != null;
        this.document = editor.getDocument();
        this.selectionModel = editor.getSelectionModel();
        this.psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
        assert psiFile != null;
        this.containingClass = PluginUtil.getContainingClass(psiFile, selectionModel.getSelectionStart());
        this.containingMethod = PluginUtil.getContainingMethod(psiFile, selectionModel.getSelectionStart());
    }

    @Override
    public void run() {
        //LOG.calculateBeforeState(psiFile);

        PsiElement psiElement = PsiUtil.getElementAtOffset(psiFile, selectionModel.getSelectionStart());
        //PsiClass psiClass = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);

        ExtractionUtil.expandLineSelection(psiFile, document, selectionModel);
        //ExtractionUtil.expandMethodSelection(document, selectionModel,psiClass);

        //LOG.innerClass = isAnonymous(selectionModel);
        //LOG.returnType = hasReturnType(containingMethod);

        // extract local variables to class fields; extract selected code to new method if necessary
        extractLocalVariablesAndMethod();

        if(selectionModel.getSelectedText().equals("\n")){
            //TODO: show notification that there is no selection possibly from contracting "super" and "this" references
            return;
        }

        // generate templates
        final BaseTemplate template = PermissionsUtil.getTemplate(editor, psiFile, permission, containingClass,
                selectionModel);

        if (template != null) {
            if(insertRequest){
                insertPermissionGuard(template);
                insertRequestCode(template);
                // add new callback method, or a new branch to an existing callback method, in target class
                if (template.getCallbackMethod() != "") insertCallback(template);
            } else {
                insertPermissionGuard(template);
            }
            // format the code style of inserted code
            formatAndCleanup();
        } else {
            NotificationsManager.getInstance(project).templateNotice(null);
        }

        //LOG.calculateAfterState(psiFile);
        //LOG.showDialog(editor.getContentComponent());
    }

    private void collectStatistics() {
        if (PersistentSettings.getInstance().notifications) {
            NotificationsManager.getInstance(project)
                    .templateNotice(PermissionsUtil.getTemplateName(containingClass));
        }
        LOG.template = PermissionsUtil.getTemplateName(containingClass);
    }

    private void formatAndCleanup() {
        JavaCodeStyleManager.getInstance(project).shortenClassReferences(psiFile.getNavigationElement());
        selectionModel.removeSelection(true);
        assert containingMethod.isValid();
        editor.getCaretModel().moveToOffset(containingMethod.getTextOffset());
    }

    private boolean isAnonymous(SelectionModel selectionModel) {
        PsiElement element = PsiUtil.getElementAtOffset(psiFile, selectionModel.getSelectionStart());
        PsiMethod method = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
        assert method != null;

        return RefactoringUtil.isInsideAnonymousOrLocal(element, method);
    }

    private boolean hasReturnType(@NotNull PsiMethod method) {
        return PsiUtil.findReturnStatements(method).length > 0;
    }

    /**
     * Handles extracting local variables into class fields, encapsulating references to parameter variables
     * within the method scope to use class fields, and extracting selected code into a new method if the number
     * of lines selected is greater than {@link #METHOD_EXTRACTION_LINES_LIMIT}.
     */
    private void extractLocalVariablesAndMethod() {
        ExtractLocalVariableHandler localVariableHandler = new ExtractLocalVariableHandler(project);
        List<PsiLocalVariable> localVariables = ExtractionUtil.getLocalVariables(project, editor);
        localVariables.forEach(local -> localVariableHandler.convertLocalToField(local, editor));
        if (localVariables.size() > 0) LOG.localVariableExtract = true; // LOGGING

        ExtractParameterReferencesHandler parametersHandler =
                new ExtractParameterReferencesHandler(project, editor, LOG);
        int linesInserted = parametersHandler.convertParameterReferencesToField(containingMethod);
        ExtractionUtil.shiftLineSelectionOnPrefix(document, selectionModel, "super.");
        ExtractionUtil.shiftLineSelectionOnPrefix(document, selectionModel, "this.");
        updateRefs();

        if (PluginUtil.getSelectionLineCount(selectionModel) >= METHOD_EXTRACTION_LINES_LIMIT) {
                try {
                final String methodName = PluginUtil.getUniqueMethodName(containingClass, "guarded");
                final ExtractMethodProcessor processor =
                        ExtractionUtil.getExtractMethodProcessor(project, editor, psiFile, methodName);
                ExtractMethodHandler.run(project, editor, processor);
            } catch (PrepareFailedException e) {
                Messages.showInfoMessage(project, "Method extraction of selected lines failed.",
                        "Runtime Permission Conversion");
                e.printStackTrace();
            } finally {
                ExtractionUtil.expandLineSelection(psiFile, document, selectionModel);
                ExtractionUtil.contractSelectionOnPrefix("return ", selectionModel);
                LOG.methodExtract = true;
            }
        }
        PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document);
    }

    /**
     * Handles inserting the constant request code field for differentiating between different permission requests
     * from within the onRequestPermissionsResult() method.
     *
     * @param template  Template instance as configured in templates.properties
     * @param <T>       Template class derived from {@link edu.oregonstate.templates.BaseTemplate}
     */
    private <T extends BaseTemplate> void insertRequestCode(@NotNull T template) {
        PsiElement anchor = PluginUtil.getFieldLocation(containingClass);
        assert anchor != null;
        PsiField requestCodeField = JavaPsiFacade.getElementFactory(project)
                .createFieldFromText(template.getRequestCodeDecl(), containingClass.getContext());
        containingClass.addAfter(requestCodeField, anchor);
        PsiDocumentManager.getInstance(project).commitDocument(document);
    }

    /************************************************************************************************/

    /**
     * Handles inserting the permission check (only) code surrounding the selected block of code.
     *
     * @param template  Template instance as configured in templates.properties
     * @param <T>       Template class derived from {@link edu.oregonstate.templates.BaseTemplate}
     */
    private <T extends BaseTemplate> void insertPermissionGuard(@NotNull T template) {
        int startOffset;
        int endOffset;
        List<String> endStrings = new ArrayList<>();
        endStrings = insertEndOfCheck(template.getCheckVersionBlockAfter(), endStrings);
        if (selectionModel.hasSelection()) {
            //noinspection ConstantConditions
            endStrings = insertEndOfCheck(selectionModel.getSelectedText(), endStrings);
        }
        endStrings = insertEndOfCheck(template.getCheckVersionElse(), endStrings);

        PsiClass parentForAnonymous = PluginUtil.getParentIfAnonymous(
                PsiTreeUtil.findElementOfClassAtOffset(psiFile,selectionModel.getSelectionStart(), PsiElement.class,
                        false));
        if(parentForAnonymous != null) {
            wasAnonymous = true;
            anonymousClass = containingClass;
            containingClass = parentForAnonymous;
        }
        document.insertString(selectionModel.getSelectionStart(), template.getCheckVersionBlockBefore());

        if(insertRequest) {
            document.insertString(selectionModel.getSelectionStart(), template.getCheckRequestBlock());
            startOffset = selectionModel.getSelectionStart() -
                    (template.getCheckVersionBlockBefore().length() + template.getCheckRequestBlock().length());
            endOffset = selectionModel.getSelectionEnd();
            for(String str : endStrings){
                endOffset += str.length();
            }
        }
        else {
            document.insertString(selectionModel.getSelectionStart(), template.getCheckOnlyBlock());
            startOffset = selectionModel.getSelectionStart() -
                    (template.getCheckVersionBlockBefore().length() + template.getCheckOnlyBlock().length());
            endOffset = selectionModel.getSelectionEnd();
            for(String str : endStrings){
                endOffset += str.length();
            }
        }

        PsiDocumentManager.getInstance(project).commitDocument(document);
        PluginUtil.formatRange(project, editor, startOffset, endOffset);
        LOG.guardInserted = true;
    }

    /*************************************************************************************************/

    /**
     *
     * @param endText       The text to be inserted after the end of the selection
     * @param endStrings    The list of strings that have been inserted after the end of the selection
     * @return              The updated list of strings that have been inserted after the end of the selection
     */
    private List<String> insertEndOfCheck(String endText, List<String> endStrings){
        document.insertString(selectionModel.getSelectionEnd(), endText);
        endStrings.add(endText);
        return endStrings;
    }

    /**
     * Handles the callback method based upon the templates and evaluations that occurred when loading the templates.
     * If a callback method already exists, a new branch is added to either the existing if-block or switch-block.
     *
     * @param template  Template instance as configured in templates.properties
     * @param <T>       Template class derived from {@link edu.oregonstate.templates.BaseTemplate}
     */
    private <T extends BaseTemplate> void insertCallback(@NotNull T template) {
        if (PluginUtil.isSelectionBlank(selectionModel)) return;
        final Optional<PsiMethod> existingCallback = CallbackUtil.getExistingCallback(containingClass);

        switch (template.getModel()) {
            case NONE:
                break;
            case NEW_METHOD:
                insertNewCallbackMethod(template);
                break;
            case IF_BLOCK:
                insertIfStatement(template, existingCallback);
                break;
            case SWITCH_BLOCK:
                insertSwitch(template, existingCallback);
                break;
        }

        if (existingCallback.isPresent()) {
            extendExistingCallback(existingCallback);
        }
        PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document);
    }

    private <T extends BaseTemplate> void insertNewCallbackMethod(@NotNull T template) {
        PsiJavaParserFacade parserFacade = JavaPsiFacade.getInstance(project).getParserFacade();
        // createMethodFromText error --> text (arg 1) is not a valid method body.
        PsiMethod callbackMethod =
                parserFacade.createMethodFromText(template.getCallbackMethod(), containingClass);
        IntentionAction addCallback =
                QuickFixFactory.getInstance().createAddMethodFix(callbackMethod, containingClass);
        addCallback.invoke(project, editor, psiFile);
        LOG.callbackInserted = true;
    }

    private <T extends BaseTemplate> void insertIfStatement(@NotNull T template, Optional<PsiMethod> existingCallback) {
        int startOffset;
        int endOffset;
        assert existingCallback.isPresent();
        Optional<PsiIfStatement> requestCodeIfElement =
                CallbackUtil.getRequestCodeIfElement(existingCallback.get());
        assert requestCodeIfElement.isPresent();
        PsiStatement thenBranch = requestCodeIfElement.get().getThenBranch();
        assert thenBranch != null;
        PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document);
        startOffset = thenBranch.getTextOffset() + thenBranch.getTextLength();
        document.insertString(startOffset, template.getCallbackIfBranch());
        endOffset = startOffset + template.getCallbackIfBranch().length();
        PsiDocumentManager.getInstance(project).commitDocument(document);
        //Format modified code
        PluginUtil.formatRange(project, editor, startOffset, endOffset);
        LOG.callbackExtendIf = true;
    }

    private <T extends BaseTemplate> void insertSwitch(@NotNull T template, Optional<PsiMethod> existingCallback) {
        int startOffset;
        int endOffset;
        assert existingCallback.isPresent();
        Optional<PsiSwitchStatement> requestCodeSwitchElement =
                CallbackUtil.getRequestCodeSwitchElement(existingCallback.get());
        assert requestCodeSwitchElement.isPresent();
        PsiCodeBlock body = requestCodeSwitchElement.get().getBody();
        assert body != null;
        PsiElement firstBodyElement = body.getFirstBodyElement();
        assert firstBodyElement != null;
        PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document);
        startOffset = firstBodyElement.getTextOffset();
        document.insertString(startOffset, template.getCallbackSwitchBranch());
        endOffset = startOffset + template.getCallbackSwitchBranch().length();
        PsiDocumentManager.getInstance(project).commitDocument(document);
        //Format modified code
        PluginUtil.formatRange(project, editor, startOffset, endOffset);
        LOG.callbackExtendSwitch = true;
    }

    private void extendExistingCallback(Optional<PsiMethod> existingCallback) {
        PsiMethod callbackMethod = existingCallback.get();
        @SuppressWarnings("unchecked")
        Collection<PsiVariable> psiVariables =
                PsiTreeUtil.collectElementsOfType(callbackMethod, PsiVariable.class);
        for (PsiVariable v : psiVariables) {
            IntentionAction variableAccessFromInnerClassFix =
                    QuickFixFactory.getInstance().createVariableAccessFromInnerClassFix(v, callbackMethod);
            variableAccessFromInnerClassFix.invoke(project, editor, psiFile);
            PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(document);
        }
    }

}