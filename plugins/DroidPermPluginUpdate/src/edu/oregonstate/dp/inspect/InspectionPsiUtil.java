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

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;

/**
 * DP-Detect side of DroidPerm - Unused
 * We may use DP-Detect in the future but for now it is not mature enough to be used with DP-Transform
 */

class InspectionPsiUtil {
    static PsiClass createPsiClass(String qualifiedName, Project project) {
        final JavaPsiFacade psiFacade = JavaPsiFacade.getInstance(project);
        final GlobalSearchScope searchScope = GlobalSearchScope.allScope(project);
        return psiFacade.findClass(qualifiedName, searchScope);
    }

    static boolean isAbstractClass(PsiClass aClass) {
        return aClass != null && aClass.getModifierList() != null &&
                aClass.getModifierList().hasModifierProperty("abstract");
    }

    static boolean isStaticMethod(PsiMethod method) {
        return hasMethodModifier(method, "static");
    }

    static boolean isPublicMethod(PsiMethod method) {
        return hasMethodModifier(method, "public");
    }

    private static boolean hasMethodModifier(PsiMethod method, String aStatic) {
        return method != null &&
                method.getModifierList().hasModifierProperty(aStatic);
    }

}
