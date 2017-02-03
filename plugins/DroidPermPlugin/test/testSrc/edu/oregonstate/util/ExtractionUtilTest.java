package edu.oregonstate.util;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.refactoring.extractMethod.ExtractMethodProcessor;
import com.intellij.refactoring.extractMethod.PrepareFailedException;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by Jacob on 10/22/2016.
 */
public class ExtractionUtilTest extends LightCodeInsightFixtureTestCase {

    private static final String BASE_PATH = "/util/ExtractionUtil/";
    private static final String ACTIVITY_CLASS = "package android.app; public class Activity{}";

    @NotNull
    @Override
    protected String getTestDataPath() {
        return "test/testData/";
    }


    public void testContractSelectionOnPrefixEmpty() {
        doContractSelectionOnPrefixTest(" ");
    }

    public void testGetLocalVariable() {
        doGetLocalVariableTest();
    }

    public void testContractSelectionOnPrefixCorrect() {
        doContractSelectionOnPrefixTest("this");
    }

    public void testExpandLineSelectionSingleLine() {
        doExpandLineSelectionTest();
    }

    public void testExpandLineSelectionMultiLine() {
        doExpandLineSelectionTest();
    }

    public void testExpandLineSelectionHeaderSameLine() {
        doExpandLineSelectionTest();
    }

    public void testExpandMethodSelection() {doExpandMethodSelectionTest();}

    public void testShiftLineSelectionDown(){
        doShiftLineSelectionTest(2);
    }


    public void testShiftLineSelectionNoShift(){
        doShiftLineSelectionTest(0);
    }

    public void testShiftLineSelectionOnPrefixOnePrefix(){
        doShiftLineSelectionOnPrefix("this");
    }

    public void testShiftLineSelectionOnPrefixThreePrefix(){
        doShiftLineSelectionOnPrefix("this");
    }

    public void testShiftLineSelectionOnPrefixDown(){
        doShiftLineSelectionOnPrefix("this");
    }

    public void testGetExtractMethodProcessor() {
        doGetExtractMethodProcessorTest("guarded");
    }

    public void testGetLocalExpressions() {
        doGetLocalExpressionsTest();
    }

    private void doGetLocalExpressionsTest() {
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        List<PsiExpression> psiExpressions = ExtractionUtil.getLocalExpressions(myFixture.getProject(),
                myFixture.getEditor());
        assertEquals(2, psiExpressions.size());
        assertEquals("[PsiReferenceExpression:a, PsiReferenceExpression:a]", psiExpressions.toString());
    }

    private void doGetExtractMethodProcessorTest(String methodName){
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        try {
            final ExtractMethodProcessor processor = ExtractionUtil.getExtractMethodProcessor(myFixture.getProject(),
                    myFixture.getEditor(),
                    myFixture.getFile(), methodName);
            processor.testRun();
        } catch (PrepareFailedException e) {
            System.out.println(e);
            fail();
        }
        myFixture.checkResultByFile(BASE_PATH + getTestName(false) + "_after.java");
    }

    private void doGetLocalVariableTest() {
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        List<PsiLocalVariable> localVariables = ExtractionUtil.getLocalVariables(myFixture.getProject(),
                myFixture.getEditor());
        assertEquals("String myString = \"My String\";", localVariables.get(0).getText());
        assertEquals("int myInt = 1;", localVariables.get(1).getText());
        assertEquals("boolean myBool = true;", localVariables.get(2).getText());
    }

    private void doContractSelectionOnPrefixTest(String prefix) {
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        ExtractionUtil.contractSelectionOnPrefix(prefix, myFixture.getEditor().getSelectionModel());
        myFixture.checkResultByFile(BASE_PATH + getTestName(false) + "_after.java");
    }

    private void doExpandLineSelectionTest(){
        myFixture.addClass(ACTIVITY_CLASS);
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        ExtractionUtil.expandLineSelection(myFixture.getFile(), myFixture.getEditor().getDocument(),
                myFixture.getEditor().getSelectionModel());
        myFixture.checkResultByFile(BASE_PATH + getTestName(false) + "_after.java");
    }

    private Integer doExpandMethodSelectionTest() {
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        PsiElement psiElement = PsiUtil.getElementAtOffset(myFixture.getFile(),
                myFixture.getEditor().getSelectionModel().getSelectionStart());
        PsiClass psiclass = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class);
        Integer result = ExtractionUtil.expandMethodSelection(myFixture.getEditor().getDocument(),
                myFixture.getEditor().getSelectionModel(), psiclass);
        myFixture.checkResultByFile(BASE_PATH + getTestName(false) + "_after.java");
        return result;
    }

    private void doShiftLineSelectionTest(int numberLineShift) {
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        ExtractionUtil.shiftLineSelection(myFixture.getEditor().getDocument(),
                myFixture.getEditor().getSelectionModel(), numberLineShift);

        myFixture.checkResultByFile(BASE_PATH + getTestName(false) + "_after.java");
    }

    private void doShiftLineSelectionOnPrefix(String prefix) {
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        ExtractionUtil.shiftLineSelectionOnPrefix(myFixture.getEditor().getDocument(),
                myFixture.getEditor().getSelectionModel(), prefix);

        myFixture.checkResultByFile(BASE_PATH + getTestName(false) + "_after.java");
    }

}
