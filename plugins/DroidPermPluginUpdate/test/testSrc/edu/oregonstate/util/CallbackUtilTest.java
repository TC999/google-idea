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

import com.intellij.openapi.util.Pair;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Unit tests for CallbackUtil methods
 */

public class CallbackUtilTest extends LightCodeInsightFixtureTestCase {

    private static final String BASE_PATH = "/util/CallbackUtil/";
    private static final String ACTIVITY_CLASS = "package android.app; public class Activity{}";
    private static final String PERMISSION = "android.permission.ACCESS_CHECKIN_PROPERTIES";
    private static final String EXPECTED_PERMISSION = PERMISSION.toUpperCase() + "_REQUEST_CODE";

    @NotNull
    @Override
    protected String getTestDataPath() {
        return "test/testData/";
    }

    public void testGetExistingCallback() {
        doGetExistingCallbackTest(true);
    }

    public void testGetExistingCallbackNonExistent(){
        doGetExistingCallbackTest(false);
    }

    public void testGetRequestCodeSwitchElement() {
        Optional<PsiSwitchStatement> statement = doGetRequestCodeSwitchElementTest();
        String bodyText = "{\n" +
                "            case ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE1:\n" +
                "                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {\n" +
                "                    sensitive1();\n" +
                "                } else {\n" +
                "                    Toast.makeText(this, \"ACCESS_CHECKIN_PROPERTIES Permission Denied\", Toast.LENGTH_LONG).show();\n" +
                "                }\n" +
                "            case ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE:\n" +
                "                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {\n" +
                "                    sensitive();\n" +
                "                } else {\n" +
                "                    Toast.makeText(this, \"ACCESS_CHECKIN_PROPERTIES Permission Denied\", Toast.LENGTH_LONG).show();\n" +
                "                }\n" +
                "                break;\n" +
                "            default:\n" +
                "                super.onRequestPermissionsResult(requestCode, permissions, grantResults);\n" +
                "        }";
        assertEquals(statement.get().getBody().getText(), bodyText);
    }

    public void testRequestCodeRecognition() {
        Pair<String, Integer> requestPair = doRequestCodeTest();
        assertEquals(EXPECTED_PERMISSION, requestPair.first);
        assertEquals(new Integer(2), requestPair.getSecond());
    }

    public void testRequestCodeRecognitionDifficult() {
        Pair<String, Integer> requestPair = doRequestCodeTest();
        assertEquals(EXPECTED_PERMISSION, requestPair.first);
        assertEquals(new Integer(2), requestPair.getSecond());
    }

    public void testRequestCodeRecognitionMaxInt() {
        Pair<String, Integer> requestPair = doRequestCodeTest();
        assertEquals(EXPECTED_PERMISSION, requestPair.first);
        assertEquals(new Integer(1), requestPair.getSecond());
    }

    public void testGetIfElement() {
        Optional<PsiIfStatement> statement = doGetIfElementTest();
        String bodyText = "if (requestCode == ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE) {\n" +
                "            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {\n" +
                "                sensitive();\n" +
                "            } else {\n" +
                "                Toast.makeText(this, \"ACCESS_CHECKIN_PROPERTIES Permission Denied\", Toast.LENGTH_LONG).show();\n" +
                "            }\n" +
                "        } else {\n" +
                "            super.onRequestPermissionsResult(requestCode, permissions, grantResults);\n" +
                "        }";
        assertEquals(statement.get().getText(), bodyText);
    }

    private Optional<PsiIfStatement> doGetIfElementTest() {
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        PsiElement psiElement = PsiUtil.getElementAtOffset(myFixture.getFile(),
                myFixture.getEditor().getSelectionModel().getSelectionStart());
        PsiClass psiClass = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
        PsiMethod psiMethod = psiClass.findMethodsByName("onRequestPermissionsResult", false)[0];
        return CallbackUtil.getRequestCodeIfElement(psiMethod);
    }

    private Pair<String, Integer> doRequestCodeTest() {
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        PsiElement psiElement = PsiUtil.getElementAtOffset(myFixture.getFile(),
                myFixture.getEditor().getSelectionModel().getSelectionStart());
        PsiClass psiclass = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
        return CallbackUtil.getRequestCodeField(psiclass, PERMISSION);
    }

    private Optional<PsiSwitchStatement> doGetRequestCodeSwitchElementTest() {
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        PsiElement psiElement = PsiUtil.getElementAtOffset(myFixture.getFile(),
                myFixture.getEditor().getSelectionModel().getSelectionStart());
        PsiClass psiClass = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
        PsiMethod psiMethod = psiClass.findMethodsByName("onRequestPermissionsResult", false)[0];
        return CallbackUtil.getRequestCodeSwitchElement(psiMethod);
    }

    private void doGetExistingCallbackTest(boolean callbackExists) {
        myFixture.addClass(ACTIVITY_CLASS);
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        PsiElement psiElement = PsiUtil.getElementAtOffset(myFixture.getFile(),
                myFixture.getEditor().getSelectionModel().getSelectionStart());
        PsiClass psiClass = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
        Optional<PsiMethod> psiMethod = CallbackUtil.getExistingCallback(psiClass);
        if(callbackExists) {
            assertTrue(psiMethod.isPresent());
            assertEquals("onRequestPermissionsResult", psiMethod.get().getName());
            String bodyText = "{\n" +
                    "        if (requestCode == ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE) {\n" +
                    "            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {\n" +
                    "                sensitive1();\n" +
                    "            } else {\n" +
                    "                Toast.makeText(this, \"ACCESS_CHECKIN_PROPERTIES Permission Denied\", Toast.LENGTH_LONG).show();\n" +
                    "            }\n" +
                    "        } else {\n" +
                    "            super.onRequestPermissionsResult(requestCode, permissions, grantResults);\n" +
                    "        }\n" +
                    "    }";
            assertEquals(bodyText, psiMethod.get().getBody().getText());
        } else{
            assertFalse(psiMethod.isPresent());
        }
    }

}
