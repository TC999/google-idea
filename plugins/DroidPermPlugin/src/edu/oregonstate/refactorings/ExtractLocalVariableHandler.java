package edu.oregonstate.refactorings;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.introduceField.BaseExpressionToFieldHandler;
import com.intellij.refactoring.introduceField.LocalToFieldHandler;
import edu.oregonstate.util.PluginUtil;

/**
 * @author Nicholas Nelson <nelsonni@oregonstate.edu> Created on on 7/27/16.
 */
public class ExtractLocalVariableHandler extends LocalToFieldHandler {

    ExtractLocalVariableHandler(Project project) {
        super(project, false);
    }

    @Override
    protected BaseExpressionToFieldHandler.Settings showRefactoringDialog(PsiClass aClass, PsiLocalVariable local,
                                                                          PsiExpression[] occurences,
                                                                          boolean isStatic) {
        return new BaseExpressionToFieldHandler.Settings(PluginUtil.getUniqueFieldName(aClass, local.getName()),
                null, occurences, true, isStaticNeeded(local), false,
                BaseExpressionToFieldHandler.InitializationPlace.IN_CURRENT_METHOD,
                PsiModifier.PRIVATE, local, local.getType(), false, aClass, false, false);
    }

    private boolean isStaticNeeded(PsiLocalVariable local) {
        if (local.hasModifierProperty(PsiModifier.STATIC)) {
            return true;
        }

        PsiMethod parentMethod = PsiTreeUtil.getParentOfType(local, PsiMethod.class);
        return parentMethod != null && parentMethod.hasModifierProperty(PsiModifier.STATIC);
    }
}
