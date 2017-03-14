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
import com.intellij.openapi.util.Pair;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.java.PsiReferenceExpressionImpl;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Contains utility methods for getting elements / information about callback in class under transformation
 */

public class CallbackUtil {

    @NotNull
    public static Optional<PsiIfStatement> getRequestCodeIfElement(PsiMethod psiMethod) {
        Collection<PsiIfStatement> psiIfStatements = PsiTreeUtil.findChildrenOfType(psiMethod, PsiIfStatement.class);
        PsiIfStatement[] targetPsiIfStatements = psiIfStatements.stream()
                .filter(s -> {
                    assert s.getCondition() != null;
                    assert s.getCondition().getType() != null;
                    assert s.getCondition().getType().equals(PsiType.BOOLEAN);
                    PsiElement[] children = s.getCondition().getChildren();
                    children = Arrays.stream(children)
                            .filter(c -> c.getClass().equals(PsiReferenceExpressionImpl.class))
                            .filter(c -> c.getText().equals("requestCode"))
                            .toArray(PsiElement[]::new);
                    return children.length > 0;
                })
                .toArray(PsiIfStatement[]::new);
        return targetPsiIfStatements.length > 0 ? Optional.of(targetPsiIfStatements[0]) : Optional.empty();
    }

    @NotNull
    public static Optional<PsiSwitchStatement> getRequestCodeSwitchElement(PsiMethod psiMethod) {
        Collection<PsiSwitchStatement> psiSwitchStatements =
                PsiTreeUtil.findChildrenOfType(psiMethod, PsiSwitchStatement.class);
        PsiSwitchStatement[] targetPsiSwitchStatements = psiSwitchStatements.stream()
                .filter(s -> {
                    assert s.getExpression() != null;
                    return s.getExpression().getText().equals("requestCode");
                })
                .toArray(PsiSwitchStatement[]::new);
        return targetPsiSwitchStatements.length > 0 ? Optional.of(targetPsiSwitchStatements[0]) : Optional.empty();
    }

    @NotNull
    public static Optional<PsiMethod> getExistingCallback(PsiClass psiClass) {
        PsiMethod[] onRequestPermissionsResults = psiClass.findMethodsByName("onRequestPermissionsResult", false);
        return onRequestPermissionsResults.length > 0 ? Optional.of(onRequestPermissionsResults[0]) : Optional.empty();
    }

    @NotNull
    static Pair<String, Integer> getRequestCodeField(PsiClass psiClass, String permission) {
        Optional<PsiField[]> constantIntegerFields = getConstantIntegerFields(psiClass);
        if (constantIntegerFields.isPresent()) {
            PsiField[] constants = constantIntegerFields.get();
            return Pair.create(uniqueRequestFieldName(constants, permission), smallestUnusedInteger(constants));
        }
        return Pair.create(permission.toUpperCase() + "_REQUEST_CODE", 1);
    }

    @NotNull
    private static Integer smallestUnusedInteger(PsiField[] constantFields) {
        List<Integer> constantIntegers = Arrays.stream(constantFields)
                .map(PsiVariable::computeConstantValue)
                .filter(obj -> obj instanceof Integer)
                .map(obj -> (Integer) obj)
                .collect(Collectors.toList());
        OptionalInt unusedInt = IntStream.range(1, 255).filter(i -> !constantIntegers.contains(i)).findFirst();
        return unusedInt.isPresent() ? unusedInt.getAsInt() : 1;
    }

    @NotNull
    private static String uniqueRequestFieldName(PsiField[] constantFields, String permission) {
        List<String> constantNames = Arrays.stream(constantFields)
                .map(NavigationItem::getName)
                .collect(Collectors.toList());
        String fieldName = permission.toUpperCase() + "_REQUEST_CODE";
        int iterator = 1;
        if (constantNames.contains(fieldName)) {
            fieldName = fieldName.concat(Integer.toString(iterator));
        }
        while (constantNames.contains(fieldName)) {
            iterator += 1;
            fieldName = fieldName.substring(0, fieldName.length() - 1) + iterator;
        }
        return fieldName;
    }

    /**
     * Examines all PsiFields within a target PsiClass and returns an array of Integer-type fields ordered by
     * their numerical values.
     *
     * @param psiClass       the class to target for analysis of all declared PsiFields.
     * @return an Optional containing constant Integer PsiFields ordered by Integer value; or Optional.empty if none.
     */
    @NotNull
    private static Optional<PsiField[]> getConstantIntegerFields(PsiClass psiClass) {
        PsiField[] fields = psiClass.getFields();
        PsiField[] integerFields = Arrays.stream(fields)
                .filter(f -> f.getType().equalsToText("int"))
                .filter(f -> f.computeConstantValue() != null)
                .sorted((f1, f2) -> Integer.compare((int) f1.computeConstantValue(), (int) f2.computeConstantValue()))
                .toArray(PsiField[]::new);
        return integerFields.length > 0 ? Optional.of(integerFields) : Optional.empty();
    }
}
