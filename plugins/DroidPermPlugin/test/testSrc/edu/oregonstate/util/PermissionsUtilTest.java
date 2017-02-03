package edu.oregonstate.util;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;


/**
 * Created by jonathan on 11/6/2016.
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
            String returnedTemplateName = PermissionsUtil.getTemplate(PERMISSION, psiclass,
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