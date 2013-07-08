package com.intellij.codeInspection;

import com.intellij.codeInspection.emptyMethod.EmptyMethodInspection;
import com.intellij.codeInspection.ex.*;
import com.intellij.codeInspection.i18n.I18nInspection;
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.InspectionTestCase;
import org.jetbrains.annotations.NotNull;

public class RedundantSuppressTest extends InspectionTestCase {
  private GlobalInspectionToolWrapper myWrapper;
  private InspectionToolWrapper[] myInspectionToolWrappers;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    InspectionToolRegistrar.getInstance().ensureInitialized();
    myInspectionToolWrappers = new InspectionToolWrapper[]{new LocalInspectionToolWrapper(new I18nInspection()),
      new GlobalInspectionToolWrapper(new EmptyMethodInspection())};

    myWrapper = new GlobalInspectionToolWrapper(new RedundantSuppressInspection() {
      @Override
<<<<<<< HEAD   (39f68d Merge "Revert "Snapshot d8891a7de15cebb78b6ce5711e50e531b42c)
      protected InspectionToolWrapper[] getInspectionTools(PsiElement psiElement, InspectionManager manager) {
=======
      protected InspectionToolWrapper[] getInspectionTools(PsiElement psiElement, @NotNull InspectionManager manager) {
>>>>>>> BRANCH (c1ace1 Snapshot aea001abfc1b38fec3a821bcd5174cc77dc75787 from maste)
        return myInspectionToolWrappers;
      }
    });
  }

  public void testDefaultFile() throws Exception {
    doTest();
  }

  public void testSuppressAll() throws Exception {
    try {
      ((RedundantSuppressInspection)myWrapper.getTool()).IGNORE_ALL = true;
      doTest();
    }
    finally {
      ((RedundantSuppressInspection)myWrapper.getTool()).IGNORE_ALL = false;
    }
  }

  private void doTest() throws Exception {
    doTest("redundantSuppress/" + getTestName(true), myWrapper,"java 1.5",true);
  }
}
