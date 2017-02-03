package edu.oregonstate.util;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Jacob on 11/6/2016.
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
        assertEquals("ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE2", uniqueName);
        uniqueName = doGetUniqueFieldNameTest("ACCESS_FINE_LOCATION_REQUEST_CODE");
        assertEquals("ACCESS_FINE_LOCATION_REQUEST_CODE", uniqueName);
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
