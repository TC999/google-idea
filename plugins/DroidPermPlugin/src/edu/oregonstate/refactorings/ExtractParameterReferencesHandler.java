package edu.oregonstate.refactorings;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.util.PsiUtil;
import com.intellij.refactoring.psi.SearchUtils;
import com.sun.org.apache.bcel.internal.classfile.Code;
import edu.oregonstate.augmentation.TestHarness;
import edu.oregonstate.util.ExtractionUtil;
import edu.oregonstate.util.PluginUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Nicholas Nelson <nelsonni@oregonstate.edu> Created on on 8/8/16.
 */
class ExtractParameterReferencesHandler {

    private final Project project;
    private final Editor editor;

    //private final Map<PsiElement, PsiElement> referenceToFieldMap = new HashMap<>();
    private final Map<PsiElement, PsiElement> referenceToFieldMap = new LinkedHashMap();

    private final TestHarness LOG;

    ExtractParameterReferencesHandler(Project project, Editor editor, TestHarness LOG) {
        this.project = project;
        this.editor = editor;
        this.LOG = LOG;
    }

    int convertParameterReferencesToField(PsiMethod method) {
        List<PsiElement> methodParameters = Arrays.stream(method.getParameterList().getParameters())
                .map(parameter -> (PsiElement) parameter)
                .collect(Collectors.toList());

        List<PsiElement> matchedExpressions = ExtractionUtil.getLocalExpressions(project, editor).stream()
                .filter(expression -> expression instanceof PsiReferenceExpression)
                .map(expression -> {
                    PsiReferenceExpression refExpression = (PsiReferenceExpression) expression;
                    return refExpression.resolve();
                })
                .filter(element -> element != null)
                .filter(methodParameters::contains)
                .distinct()
                .collect(Collectors.toList());

        matchedExpressions.forEach(expression -> {
            PsiElement element = convertToField(expression);
            // grab according to what being passed
            System.out.print(element + "\n");

            assert element != null;
            referenceToFieldMap.put(expression, element);
        });
        updateLocalReferences(method);
        if (matchedExpressions.size() > 0) LOG.parameterExtract = true;
        return insertReferenceLinkage(method);
    }

    private void updateLocalReferences(@NotNull PsiMethod method) {
        LocalSearchScope scope = new LocalSearchScope(method);

        referenceToFieldMap.entrySet().forEach(entry -> {
            Iterable<PsiReference> references = SearchUtils.findAllReferences(entry.getKey(), scope);
            references.forEach(reference -> reference.handleElementRename(PsiUtil.getName(entry.getValue())));
        });
        PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(editor.getDocument());
    }

    private PsiElement convertToField(@NotNull PsiElement element) {
        String name = PsiUtil.getName(element);
        PsiType type = PsiUtil.getTypeByPsiElement(element);
        assert name != null && type != null;
        PsiClass psiClass = PluginUtil.getTopLevelClass(element.getContainingFile(), element.getTextOffset());
        assert psiClass != null;
        PsiElement anchor = PluginUtil.getFieldLocation(psiClass);
        assert anchor != null && anchor.isPhysical();
        String fieldName = PluginUtil.getUniqueFieldName(psiClass, "f_" + name);

        PsiField field = JavaPsiFacade.getElementFactory(project).createField(fieldName, type);
        return psiClass.addAfter(field, anchor);
    }

    private int insertReferenceLinkage(@NotNull PsiMethod method) {
        PsiElement anchor = PluginUtil.getMethodFirstStatementLocation(method);
        assert anchor != null;
        referenceToFieldMap.entrySet().forEach(entry -> {
            String original = PsiUtil.getName(entry.getKey());
            String updated = PsiUtil.getName(entry.getValue());
            assert original != null;
            assert updated != null;
            String compositeDecl = "this." + updated + " = " + original + ";\n";

            // print random this.f_l = l; this.f_a = a; this.f_k = k;
            // Fixed: This will print backward. If input "a,k,l", then output "l,k,a"
            System.out.print(compositeDecl);

            editor.getDocument().insertString(anchor.getTextOffset(), compositeDecl);
        });

        //Have to commit document before formatting
        PsiDocumentManager.getInstance(project).commitDocument(editor.getDocument());

        formatInsertedStatements(anchor);

        return referenceToFieldMap.entrySet().size();
    }

    private void formatInsertedStatements(PsiElement anchor) {
        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
        assert psiFile != null;
        PsiElement psiElement = PsiUtil.getElementAtOffset(psiFile, anchor.getTextOffset());
        //Have to go up 4 nodes to get to expression statement
        psiElement = psiElement.getParent().getParent().getParent().getParent();
        for(int i = 0; i < referenceToFieldMap.size(); i++){
            CodeStyleManager.getInstance(project).reformat(psiElement);
            //Skip LineBreak and go to next statement
            psiElement = psiElement.getNextSibling().getNextSibling();
        }
    }
}
