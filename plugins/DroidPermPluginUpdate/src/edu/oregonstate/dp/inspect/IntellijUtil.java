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

import com.intellij.analysis.AnalysisScope;
import com.intellij.openapi.externalSystem.util.ExternalSystemConstants;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * DP-Detect side of DroidPerm - Unused
 * We may use DP-Detect in the future but for now it is not mature enough to be used with DP-Transform
 */

public class IntellijUtil {

    public static Path getModulePath(Module module) {
        //noinspection ConstantConditions
        return Paths.get(module.getOptionValue(ExternalSystemConstants.LINKED_PROJECT_PATH_KEY));
    }

    private static Path getScopePath(AnalysisScope scope) throws DroidPermException {
        if (!(scope.getScopeType() == AnalysisScope.DIRECTORY)) {
            return null;
        }
        Class<AnalysisScope> scopeClazz = AnalysisScope.class;
        try {
            Field myElementField = scopeClazz.getDeclaredField("myElement");
            myElementField.setAccessible(true);
            PsiElement scopePsiElement = (PsiElement) myElementField.get(scope);

            if (scopePsiElement != null) {
                String canonicalPath = ((PsiDirectory) scopePsiElement).getVirtualFile().getCanonicalPath();
                return canonicalPath != null ? Paths.get(canonicalPath) : null;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new DroidPermException(e);
        }
    }

    /**
     * A ton of hacks. No guarantee will work in all cases/all machines.
     */
    private static boolean scopeContainsModule(AnalysisScope scope, Module module) throws DroidPermException {
        if (scope.containsModule(module)) {
            return true;
        }
        Path modulePath = getModulePath(module);
        Path scopePath = getScopePath(scope);
        return modulePath != null && scopePath != null &&
                modulePath.toAbsolutePath().equals(scopePath.toAbsolutePath());
    }

    public static Module getModule(AnalysisScope scope, Project project) throws DroidPermException {
        ModuleManager man = ModuleManager.getInstance(project);
        for (Module module : man.getModules()) {
            if (scopeContainsModule(scope, module)) {
                return module;
            }
        }
        return null;
    }
}
