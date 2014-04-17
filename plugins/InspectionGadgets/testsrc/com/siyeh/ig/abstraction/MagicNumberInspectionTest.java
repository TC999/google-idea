package com.siyeh.ig.abstraction;

import com.intellij.codeInspection.InspectionProfileEntry;
import com.siyeh.ig.LightInspectionTestCase;

public class MagicNumberInspectionTest extends LightInspectionTestCase {

  public void testMagicNumber() {
    doTest();
  }

  @Override
  protected InspectionProfileEntry getInspection() {
    final MagicNumberInspection tool = new MagicNumberInspection();
    tool.ignoreInHashCode = true;
    tool.ignoreInAnnotations = true;
    tool.ignoreInitialCapacity = true;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    doTest("com/siyeh/igtest/abstraction/magic_number", tool);
=======
    return tool;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }
}
