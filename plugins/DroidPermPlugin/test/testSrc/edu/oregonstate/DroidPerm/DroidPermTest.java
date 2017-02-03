package edu.oregonstate.DroidPerm;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import edu.oregonstate.refactorings.PermissionGuardRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Jacob on 10/16/2016.
 */
public class DroidPermTest extends LightCodeInsightFixtureTestCase {

    private static final String BASE_PATH = "/DroidPerm/";
    private static final String PERMISSION = "android.permission.ACCESS_CHECKIN_PROPERTIES";
    private static final String ACTIVITY_CLASS = "package android.app; public class Activity{}";
    private static final String FRAGMENT_CLASS = "package android.app; public class Fragment{}";
    private static final String SERVICE_CLASS = "package android.app; public class Service{}";
    private static final String VIEW_CLASS = "package android.view; public class View{}";


    @NotNull
    @Override
    protected String getTestDataPath() {
        return "test/testData/";
    }

    public void testBasicActivity() {
        doTest(ACTIVITY_CLASS, true);
    }

    public void testVariableExtractionMultiple() {
        doTest(ACTIVITY_CLASS, true);
    }

    public void testVariableExtraction() {
        doTest(ACTIVITY_CLASS, true);
    }

    public void testMethodExtraction() {
        doTest(ACTIVITY_CLASS, true);
    }

    public void testBasicFragment() {
        doTest(FRAGMENT_CLASS, true);
    }

    public void testBasicService() {
        doTest(SERVICE_CLASS, false);
    }

    public void testBasicView() {
        doTest(VIEW_CLASS, false);
    }

    public void testExpandLineSelection() {
        doTest(ACTIVITY_CLASS, true);
    }

    public void testExistingCallback_IfStatement() {
        doTest(ACTIVITY_CLASS, true);
    }

    public void testExistingCallback_SwitchStatement() {
        doTest(ACTIVITY_CLASS, true);
    }

    public void testThisAndSuperReferences() {
        doTest(ACTIVITY_CLASS, true);
    }

    public void testEmptySelectionAfterThisSuperShift(){
        doTest(ACTIVITY_CLASS, true);
    }

    public void testCheckOnly() {
        doTest(ACTIVITY_CLASS, false);
    }

    public void testParameterExtraction() {
        doTest(ACTIVITY_CLASS, true);
    }

    public void testParameterExtractionMultiple() {
        doTest(ACTIVITY_CLASS, true);
    }

    public void testSelectionWithComment() {
        doTest(ACTIVITY_CLASS, true);
    }

    public void testSelectionWithWhitespace() { doTest(ACTIVITY_CLASS, true); }

    public void testAnonymousClass() {
        doTest(ACTIVITY_CLASS, true);
    }

    public void testAnonymousMultiLine() {
        doTest(ACTIVITY_CLASS, true);
    }

    public void testCodeCleanupOnlyModified(){
        doTest(ACTIVITY_CLASS, true);
    }

    private void doTest(String Class, boolean insertRequest) {
        myFixture.addClass(Class);
        myFixture.addClass("package android; public class Manifest{}");
        myFixture.addClass("package android.content.pm; public class PackageManager{}");
        myFixture.addClass("package android.os; public class Build{}");
        myFixture.addClass("package android.support.v4.app; public class ActivityCompat{}");
        if(Class.equals(ACTIVITY_CLASS) || Class.equals(FRAGMENT_CLASS)){
            myFixture.addClass("package android.support.annotation; public class NonNull{}");
            myFixture.addClass("package android.widget; public class Toast{}");
        }
        myFixture.configureByFile(BASE_PATH + getTestName(false) + ".java");
        performRefactor(myFixture.getProject(), myFixture.getEditor(), insertRequest, PERMISSION);
        myFixture.checkResultByFile(BASE_PATH + getTestName(false) + "_after.java");
    }

    void performRefactor(Project project, Editor editor, boolean insertRequest, String permission){

        String title = "Runtime Permission Conversion";
        PermissionGuardRunnable job = new PermissionGuardRunnable(project,
                editor, permission, insertRequest);

        CommandProcessor.getInstance().executeCommand(project, () ->
                        ApplicationManager.getApplication().runWriteAction(job), title, null);

    }
}