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

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.introduceField.BaseExpressionToFieldHandler;
import com.intellij.refactoring.introduceField.LocalToFieldHandler;
import edu.oregonstate.util.PluginUtil;

/**
 * LocalToFieldHandler child class
 * Calls parent with appropriate parameters to perform local variable to field extraction
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
