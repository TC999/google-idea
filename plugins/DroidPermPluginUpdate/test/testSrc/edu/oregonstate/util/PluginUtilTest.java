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

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Unit tests for PluginUtil methods
 */

public class PluginUtilTest extends LightCodeInsightFixtureTestCase {

    private static final String BASE_PATH = "/util/PluginUtil/";
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

    public void testGetTemplateFromContext() {
        myFixture.addClass(ACTIVITY_CLASS);
        myFixture.addClass(FRAGMENT_CLASS);
        myFixture.addClass(SERVICE_CLASS);
        myFixture.addClass(VIEW_CLASS);
        for (String file : BASIC_CLASSES) {
            myFixture.configureByFile(file);
            String returnedTemplate = PluginUtil.getTemplateFromContext(myFixture.getEditor(), myFixture.getProject())
                    .getClass().getName();
            String trueTemplate = "edu.oregonstate.templates." + file.split("Basic|\\.")[1] + "Template";
            assertEquals(returnedTemplate, trueTemplate);
        }
    }

    public void testGoodPreconditions() {
        doPreconditionTest(true);
    }

    public void testGoodPreconditions2() {
        doPreconditionTest(true);
    }

    public void testGetUniqueMethodName(){
        String newMethodName = doGetUniqueMethodNameTest("guarded");
        assertEquals(newMethodName, "guarded1");
    }

    public void testGetFieldLocation(){
        assertEquals(doGetFieldLocationTest().getText(), "private String lastField = \"I'm the last field\";");
    }

    public void testGetMethodFirstStatementLocation() {
        PsiElement psiElement = doGetMethodFirstStatementLocationTest();
        assertEquals("this.a = true;", psiElement.getText());
    }

    public void testGetMethodFirstStatementLocationWithComment() {
        PsiElement psiElement = doGetMethodFirstStatementLocationTest();
        assertEquals("//Comment Before First Statement", psiElement.getText());
    }

    public void testGetContainingMethod() {
        PsiMethod psiMethod = doGetContainingMethodTest();
        assert psiMethod != null;
        assertEquals("foo", psiMethod.getName());
        assertEquals("void foo(){\n" +
                "\t\tsensitive();\n" +
                "    }", psiMethod.getText());
    }

    public void testGetContainingMethodWhiteSpaceInSelection() {
        PsiMethod psiMethod = doGetContainingMethodTest();
        assert psiMethod != null;
        assertEquals("foo", psiMethod.getName());
        assertEquals("void foo(){\n" +
                "\t\t\n" +
                "        sensitive();\n" +
                "    }", psiMethod.getText());
    }

    public void testGetContainingMethodCommentInSelection() {
        PsiMethod psiMethod = doGetContainingMethodTest();
        assert psiMethod != null;
        assertEquals("foo", psiMethod.getName());
        assertEquals("void foo(){\n" +
                "\t\t//Comment before code\n" +
                "        sensitive();\n" +
                "    }", psiMethod.getText());
    }

    public void testGetUniqueFieldName() {
        myFixture.addClass(ACTIVITY_CLASS);
        String uniqueName = doGetUniqueFieldNameTest("ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE");
        assertEquals("ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE1", uniqueName);
    }

    // Ha
    public void testGetFieldName() {
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        PsiElement psiElement = PsiUtil.getElementAtOffset(myFixture.getFile(),
                myFixture.getEditor().getSelectionModel().getSelectionStart());
        PsiClass psiClass = PsiUtil.getTopLevelClass(psiElement);
        PsiField f[] = psiClass.getAllFields();
        for (int i = 0; i < f.length; i++) {
            if((f[i].getType()).equalsToText("Context")) {
                System.out.println(f[i]);
                System.out.println(f[i].getType());
            }
        }
        return;
    }

    // Let's try this out.
    public void testGetDeveloperContextField() {
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        PsiElement psiElement = PsiUtil.getElementAtOffset(myFixture.getFile(),
                myFixture.getEditor().getSelectionModel().getSelectionStart());
        PsiClass psiClass = PsiUtil.getTopLevelClass(psiElement);
        String devContext = PluginUtil.getDeveloperContextField(psiClass).toString();
        String devContextResult = devContext.substring(9,devContext.length());
        assertEquals(devContextResult, "aContext");
    }

    // Ha
    public void testGetParameterName() {
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        PsiElement psiElement = PsiUtil.getElementAtOffset(myFixture.getFile(),
                myFixture.getEditor().getSelectionModel().getSelectionStart());
        PsiMethod psiMethod = PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class);
        PsiParameter[] parameters = psiMethod.getParameterList().getParameters();
        for (int i = 0; i < parameters.length; i++) {
            if((parameters[i].getType()).equalsToText("Context")) {
                System.out.println(parameters[i]);
            }
        }
    }

    // Let's try this out.
    public void testGetDeveloperContextParameter() {
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        PsiElement psiElement = PsiUtil.getElementAtOffset(myFixture.getFile(),
                myFixture.getEditor().getSelectionModel().getSelectionStart());
        PsiClass psiClass = PsiUtil.getTopLevelClass(psiElement);
        String devContextParameter = PluginUtil.getDeveloperContextParameter(psiClass, myFixture.getEditor()).toString();
        String devContextParameterResult = devContextParameter.substring(13,devContextParameter.length());
        assertEquals(devContextParameterResult, "aContext");
    }

    // Ha
    public void testGetVariableName() {
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        PsiElement psiElement = PsiUtil.getElementAtOffset(myFixture.getFile(),
                myFixture.getEditor().getSelectionModel().getSelectionStart());
        PsiClass psiclass = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
        Integer result = ExtractionUtil.expandMethodSelection(myFixture.getEditor().getDocument(),
                myFixture.getEditor().getSelectionModel(), psiclass);
        List<PsiLocalVariable> localVariables = ExtractionUtil.getLocalVariables(myFixture.getProject(),
                myFixture.getEditor());
        for (int i = 0; i < localVariables.size(); i++) {
            if(localVariables.get(i).getType().equalsToText("Context")) {
                System.out.println(localVariables.get(i));
            }
        }
    }

    // Let's try this out.
    public void testGetDeveloperContextVariable() {
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        PsiElement psiElement = PsiUtil.getElementAtOffset(myFixture.getFile(),
                myFixture.getEditor().getSelectionModel().getSelectionStart());
        PsiClass psiClass = PsiUtil.getTopLevelClass(psiElement);
        String devContextVariable = PluginUtil.getDeveloperContextLocal(myFixture.getFile(),
                myFixture.getEditor()).toString();
        String devContextVariableResult = devContextVariable.substring(17,devContextVariable.length());
        assertEquals(devContextVariableResult, "context");
    }

    private String doGetUniqueMethodNameTest(String value){
        myFixture.addClass(ACTIVITY_CLASS);
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        PsiElement element = PsiUtil.getElementAtOffset(myFixture.getFile(),
                myFixture.getEditor().getSelectionModel().getSelectionStart());
        PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        return PluginUtil.getUniqueMethodName(psiClass, value);
    }

    private PsiElement doGetFieldLocationTest() {
        myFixture.addClass(ACTIVITY_CLASS);
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        PsiElement element = PsiUtil.getElementAtOffset(myFixture.getFile(),
                myFixture.getEditor().getSelectionModel().getSelectionStart());
        PsiClass psiClass = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        return PluginUtil.getFieldLocation(psiClass);
    }

    private String doGetUniqueFieldNameTest(String candidateName) {
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        PsiElement psiElement = PsiUtil.getElementAtOffset(myFixture.getFile(),
                myFixture.getEditor().getSelectionModel().getSelectionStart());
        PsiClass psiClass = PsiUtil.getTopLevelClass(psiElement);
        return PluginUtil.getUniqueFieldName(psiClass, candidateName);
    }

    private PsiElement doGetMethodFirstStatementLocationTest() {
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        PsiElement psiElement = PsiUtil.getElementAtOffset(myFixture.getFile(),
                myFixture.getEditor().getSelectionModel().getSelectionStart());
        PsiMethod psiMethod = PsiTreeUtil.getParentOfType(psiElement, PsiMethod.class);
        return PluginUtil.getMethodFirstStatementLocation(psiMethod);
    }

    private void doPreconditionTest(boolean goodConditions) {
        myFixture.addClass(ACTIVITY_CLASS);
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        boolean conditionsMet = PluginUtil.preconditions(myFixture.getFile(), myFixture.getEditor());
        assertEquals(goodConditions, conditionsMet);
    }

    private PsiMethod doGetContainingMethodTest() {
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        return PluginUtil.getContainingMethod(myFixture.getFile(),
                myFixture.getEditor().getSelectionModel().getSelectionStart());
    }

}
