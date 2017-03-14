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

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;

/**
 * Unit tests for PermissionsUtil methods
 */

public class PermissionsUtilTest extends LightCodeInsightFixtureTestCase {

    private static final String BASE_PATH = "/util/PermissionsUtil/";
    private static final String PERMISSION = "android.permission.ACCESS_CHECKIN_PROPERTIES";
    private static final String[] BASIC_CLASSES = {
            BASE_PATH + "BasicActivity.java",
            BASE_PATH + "BasicFragment.java",
            BASE_PATH + "BasicService.java",
            BASE_PATH + "BasicView.java"};

    private static final String ACTIVITY_CLASS = "package android.app; public class Activity{}";
    private static final String FRAGMENT_CLASS = "package android.app; public class Fragment{}";
    private static final String SERVICE_CLASS = "package android.app; public class Service{}";
    private static final String VIEW_CLASS = "package android.view; public class View{}";

    @NotNull
    @Override
    protected String getTestDataPath() {
        return "test/testData/";
    }

    public void testGetTemplate() {
        myFixture.addClass(ACTIVITY_CLASS);
        myFixture.addClass(FRAGMENT_CLASS);
        myFixture.addClass(SERVICE_CLASS);
        myFixture.addClass(VIEW_CLASS);
        for (String file : BASIC_CLASSES) {
            myFixture.configureByFile(file);
            PsiElement psiElement = PsiUtil.getElementAtOffset(myFixture.getFile(),
                    myFixture.getEditor().getSelectionModel().getSelectionStart());
            PsiClass psiclass = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
            String returnedTemplateName = PermissionsUtil.getTemplate(myFixture.getEditor(), myFixture.getFile(),
                    PERMISSION, psiclass,
                    myFixture.getEditor().getSelectionModel()).getClass().getName();
            String trueTemplateName = "edu.oregonstate.templates." + file.split("Basic|\\.")[1] + "Template";
            assertEquals(returnedTemplateName, trueTemplateName);
        }
    }

    public void testGetTemplateName() {
        myFixture.addClass(ACTIVITY_CLASS);
        myFixture.addClass(FRAGMENT_CLASS);
        myFixture.addClass(SERVICE_CLASS);
        myFixture.addClass(VIEW_CLASS);
        for (String file : BASIC_CLASSES) {
            myFixture.configureByFile(file);
            PsiElement element = PsiUtil.getElementAtOffset(myFixture.getFile(),
                    myFixture.getEditor().getSelectionModel().getSelectionStart());
            PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
            String templateName = PermissionsUtil.getTemplateName(psiClass);
            assertEquals(file.split("Basic|\\.")[1] + "Template", templateName);
        }
    }

}