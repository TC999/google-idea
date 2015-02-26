package com.siyeh.ig.migration;

import com.intellij.codeInspection.InspectionProfileEntry;
import com.intellij.testFramework.LightProjectDescriptor;
import com.siyeh.ig.LightInspectionTestCase;
import org.jetbrains.annotations.NotNull;

public class ForCanBeForeachInspectionTest extends LightInspectionTestCase {

  public void testForCanBeForEach() {
    doTest();
  }

  @Override
  protected InspectionProfileEntry getInspection() {
    ForCanBeForeachInspection inspection = new ForCanBeForeachInspection();

    // Android Studio: Changed default to false, but this test is for indexed-checks=true.
    inspection.REPORT_INDEXED_LOOP = true;

    return inspection;
  }

  @Override
  protected String getBasePath() {
    return "/plugins/InspectionGadgets/test/com/siyeh/igtest/migration/foreach";
  }
<<<<<<< HEAD   (a3b687 Merge "Added missing uninstall.exe to the windows layout." i)
}
=======

  @NotNull
  @Override
  protected LightProjectDescriptor getProjectDescriptor() {
    return JAVA_8;
  }
}
>>>>>>> BRANCH (f3cad8 Snapshot idea/140.2683.2 from git://git.jetbrains.org/idea/c)
