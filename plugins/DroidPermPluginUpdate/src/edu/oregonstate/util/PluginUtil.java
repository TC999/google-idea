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

package edu.oregonstate.util;

import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.*;
import edu.oregonstate.templates.BaseTemplate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Contains generic utility methods for DroidPerm that don't fit in other utility classes
 */

public class PluginUtil {

    @Nullable
    public static PsiClass getTopLevelClass(PsiFile psiFile, Integer offset) {
        PsiElement psiElement = PsiUtil.getElementAtOffset(psiFile, offset);
        return PsiUtil.getTopLevelClass(psiElement);
    }


    public static PsiClass getParentIfAnonymous(final PsiElement psiElement) {
        PsiClass parentClass = null;
        PsiElement parent = psiElement.getParent();
        boolean wasAnonymousBefore = false;
        while (parent != null && parent.getContainingFile() != null) {
            if(parent instanceof PsiAnonymousClass){
                wasAnonymousBefore = true;
            }
            if (parent instanceof PsiClass && !(parent instanceof PsiAnonymousClass)) {
                parentClass = wasAnonymousBefore?(PsiClass) parent : null;
            }
            parent = parent.getParent();
        }
        return parentClass;
    }

    public static List<PsiClass> getParentTreeIfAnonymous(final PsiElement psiElement){
        PsiElement parent = psiElement.getParent();
        final List<PsiClass> classes = new ArrayList<>();
        while (parent != null && parent.getContainingFile() != null) {
            if (parent instanceof PsiAnonymousClass) {
                classes.add((PsiClass)parent);
            }
            parent = parent.getParent();
        }
        return classes;
    }


    public static PsiClass getContainingClass(@NotNull PsiFile file, int offset) {
        PsiElement element = PsiUtil.getElementAtOffset(file, offset);
        return PsiTreeUtil.getParentOfType(element, PsiClass.class);
    }

    @Nullable
    @Contract("null, _ -> null")
    public static PsiMethod getContainingMethod(@NotNull PsiFile file, int offset) {
        PsiElement psiElement = PsiUtil.getElementAtOffset(file, offset);
        return PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class);
    }


    /**
     *
     * @param project       The current project
     * @param editor        The current editor
     * @param startOffset   The location to start formatting
     * @param endOffset     The location to end formatting
     */
    public static void formatRange(@NotNull Project project, @NotNull Editor editor, int startOffset, int endOffset){
        Document document = editor.getDocument();
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
        PsiElement psiElement = PsiUtil.getElementAtOffset(psiFile, startOffset);
        CodeStyleManager.getInstance(project).reformatRange(psiElement, startOffset,
                endOffset);

    }

    @NotNull
    public static Integer getMethodLineCount(@NotNull PsiMethod psiMethod, @NotNull Document document) {
        final PsiCodeBlock body = psiMethod.getBody();
        assert body != null;
        PsiElement firstBodyElement = body.getFirstBodyElement();
        assert firstBodyElement != null;
        PsiElement lastBodyElement = body.getLastBodyElement();
        assert lastBodyElement != null;

        int startLineNumber = document.getLineNumber(firstBodyElement.getTextOffset());
        int endLineNumber = document.getLineNumber(lastBodyElement.getTextOffset());
        return endLineNumber - startLineNumber + 1;
    }

    @NotNull
    static String getMethodSignature(@NotNull PsiMethod psiMethod) {
        if (!psiMethod.isPhysical()) System.out.println("Method '" + psiMethod.getName() + "' is not physical");
        MethodSignature signature = psiMethod.getSignature(PsiSubstitutor.EMPTY);
        PsiType[] parameterTypes = signature.getParameterTypes();
        String[] parameterSignatures = Arrays.stream(parameterTypes)
                .map(PsiType::getPresentableText)
                .map(s -> Character.toLowerCase(s.charAt(0)) + s.substring(1))
                .toArray(String[]::new);
        String parameters = StringUtil.join(parameterSignatures, ",");
        return signature.getName() + "(" + parameters + ");";
    }

    @NotNull
    public static String getUniqueMethodName(@NotNull PsiClass psiClass, String candidateName) {
        List<String> methodNames = Arrays.stream(psiClass.getMethods())
                .map(NavigationItem::getName)
                .collect(Collectors.toList());
        int iterator = 1;
        if (methodNames.contains(candidateName)) {
            candidateName = candidateName.concat(Integer.toString(iterator));
        }
        while (methodNames.contains(candidateName)) {
            iterator += 1;
            candidateName = candidateName.substring(0, candidateName.length() - 1) + iterator;
        }
        return candidateName;
    }

    @NotNull
    public static String getUniqueFieldName(@NotNull PsiClass psiClass, String candidateName) {
        @SuppressWarnings("unchecked")
        Collection<PsiField> psiFields = PsiTreeUtil.collectElementsOfType(psiClass, PsiField.class);
        List<String> fieldNames = psiFields.stream()
                .map(NavigationItem::getName)
                .collect(Collectors.toList());
        int iterator = 1;
        if (fieldNames.contains(candidateName)) {
            candidateName = candidateName.concat(Integer.toString(iterator));
        }
        while (fieldNames.contains(candidateName)) {
            iterator += 1;
            candidateName = candidateName.substring(0, candidateName.length() - 1) + iterator;
        }
        return candidateName;
    }

    @Nullable
    public static PsiElement getFieldLocation(PsiClass psiClass) {
        PsiField[] fields = psiClass.getFields();
        if (fields.length > 0) {
            return fields[fields.length-1];
        } else {
            final PsiElement lBrace = psiClass.getLBrace();
            if (lBrace != null) {
                PsiElement result = lBrace.getNextSibling();
                while (result.getNextSibling() instanceof PsiWhiteSpace) {
                    result = result.getNextSibling();
                }
                return result;
            }
        }
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    public static int getSelectionLineCount(SelectionModel selectionModel) {
        if (selectionModel.hasSelection()) {
            return selectionModel.getSelectedText().split("[\n|\r]").length;
        }
        return 0;
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean isSelectionBlank(SelectionModel selectionModel) {
        return selectionModel.hasSelection() && selectionModel.getSelectedText().trim().length() == 0;
    }


    /**
     * @param psiMethod
     * @return The first PsiElement in psiMethod
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public static PsiElement getMethodFirstStatementLocation(PsiMethod psiMethod) {
        PsiCodeBlock body = psiMethod.getBody();
        if (body != null) {
            final PsiJavaToken lBrace = body.getLBrace();
            if (lBrace != null) {
                PsiElement result = lBrace.getNextSibling();
                PsiSuperExpression superExpression =
                        PsiTreeUtil.findChildOfAnyType(result, false, PsiSuperExpression.class);
                PsiClass containingClass = psiMethod.getContainingClass();
                assert containingClass != null;
                while (result instanceof PsiWhiteSpace ||
                        (superExpression != null && thisOrSuperReference(superExpression, containingClass))) {
                    result = result.getNextSibling();
                    superExpression = PsiTreeUtil.findChildOfAnyType(result, false, PsiSuperExpression.class);
                }

                return result;
            }
        }
        return null;
    }

    public static PsiField getDeveloperContextField(@NotNull PsiClass psiClass) {
        PsiField f[] = psiClass.getAllFields();
        for (int i = 0; i < f.length; i++) {
            if((f[i].getType()).equalsToText("Context")) {
                return f[i];
            }
        }
        return null;
    }

    public static PsiParameter getDeveloperContextParameter(@NotNull PsiClass psiClass, Editor editor) {
        SelectionModel selectionModel = editor.getSelectionModel();
        PsiMethod psiMethod = getContainingMethod(psiClass.getContainingFile(),selectionModel.getSelectionStart());
        assert psiMethod != null;
        PsiParameter[] parameters = psiMethod.getParameterList().getParameters();
        for (PsiParameter parameter : parameters) {
            if ((parameter.getType()).equalsToText("Context")) {
                return parameter;
            }
        }
        return null;
    }

    public static PsiLocalVariable getDeveloperContextLocal(PsiFile psiFile,
                                                            @NotNull Editor editor) {
        SelectionModel selectionModel = editor.getSelectionModel();
        PsiMethod myMethod = getContainingMethod(psiFile, selectionModel.getSelectionStart());
        assert myMethod != null;
        assert myMethod.getBody() != null;
        PsiElement element = myMethod.getBody().getFirstBodyElement();
        while(element != null && element.getTextOffset() < selectionModel.getSelectionStart()){
            if(element instanceof PsiDeclarationStatement){
                //Iterate over first child's children to find instance of PsiTypeElement
                PsiElement[] myChildren = element.getFirstChild().getChildren();
                for (PsiElement aMyChildren : myChildren) {
                    if (aMyChildren instanceof PsiTypeElement) {
                        if (aMyChildren.getFirstChild().getText().equals("Context")) {
                            //We found a context variable
                            return (PsiLocalVariable) element.getFirstChild();
                        }
                    }
                }
            }
            element = element.getNextSibling();
        }
        return null;
    }

    // Reference: com.intellij.codeInsight.daemon.impl.analysis.HighlightUtil::thisOrSuperReference()
    private static boolean thisOrSuperReference(@Nullable PsiExpression qualifierExpression, PsiClass aClass) {
        if (qualifierExpression == null) return true;
        PsiJavaCodeReferenceElement qualifier;
        if (qualifierExpression instanceof PsiThisExpression) {
            qualifier = ((PsiThisExpression)qualifierExpression).getQualifier();
        }
        else if (qualifierExpression instanceof PsiSuperExpression) {
            qualifier = ((PsiSuperExpression)qualifierExpression).getQualifier();
        }
        else {
            return false;
        }
        if (qualifier == null) return true;
        PsiElement resolved = qualifier.resolve();
        return resolved instanceof PsiClass && InheritanceUtil.isInheritorOrSelf(aClass, (PsiClass)resolved, true);
    }

    public static void openFileInEditor(Project project, PsiFile psiFile) {
        String path = psiFile.getVirtualFile().getCanonicalPath();
        assert path != null;
        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);

        VirtualFile vf = LocalFileSystem.getInstance().findFileByPath(path);
        assert vf != null;
        fileEditorManager.openFile(vf, true, true);
    }

    public static String trimPermission(String permission){
        String[] permissionTemp = permission.split("\\.");
        return permissionTemp[permissionTemp.length - 1];
    }

    public static <T extends BaseTemplate> T getTemplateFromContext(Editor editor, Project project){
        Document document = editor.getDocument();
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document);
        SelectionModel selectionModel = editor.getSelectionModel();
        assert psiFile != null;
        PsiClass containingClass = PluginUtil.getContainingClass(psiFile, selectionModel.getSelectionStart());
        return PermissionsUtil.getTemplate(editor, psiFile, "ACCESS_FINE_LOCATION",
                containingClass, selectionModel);
    }


    public static boolean preconditions(PsiFile psiFile, Editor editor) {
        if (editor != null && psiFile != null) {
            boolean isJavaFileType = psiFile.getFileType().equals(StdFileTypes.JAVA);
            PsiElement selectedElement = psiFile.findElementAt(editor.getSelectionModel().getSelectionStart());
            PsiMethod containingMethod = PsiTreeUtil.getParentOfType(selectedElement, PsiMethod.class, false);


            if (isJavaFileType && containingMethod != null) {
                // enable action only in Java file with selection inside of a PsiMethod
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /*
    Not Working, Currently Unused.
    We either have to traverse the AST or examine text to determine if there exists a method call on the line.
     */
    private static boolean methodOnLine(PsiFile psiFile, Editor editor) {
        int start;
        if(editor.getSelectionModel().getSelectedText() == null){
            Pair<Integer, Integer> selection = ExtractionUtil.getExpandedLineOffsets(psiFile, editor.getDocument(),
                    editor.getSelectionModel());
            start = selection.getFirst();
        }else{
            start = editor.getSelectionModel().getSelectionStart();
        }

        PsiElement currentElement = PsiUtil.getElementAtOffset(psiFile, start);
        while(!currentElement.getText().equals("\n")){
            if(currentElement instanceof PsiIdentifier && currentElement.getParent() instanceof PsiReferenceExpression
                    && currentElement.getParent().getParent() instanceof PsiMethodCallExpression){
                return true;
            }
            if(currentElement.getNextSibling()!= null){
                currentElement = currentElement.getNextSibling();
            } else {
                return false;
            }
        }
        return false;
    }

}
