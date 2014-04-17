package com.intellij.openapi.keymap.impl;

import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.util.SystemInfo;

public class GenericKeymapManager extends DefaultKeymap {
  @Override
  public String getDefaultKeymapName() {
    if (SystemInfo.isMac) {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
      return "Mac OS X 10.5+";
    }
    else if (SystemInfo.isXWindow) {
      return KeymapManager.X_WINDOW_KEYMAP;
    }
    else {
      return KeymapManager.DEFAULT_IDEA_KEYMAP;
    }
  }

  @Override
  public String getKeymapPresentableName(KeymapImpl keymap) {
    final String name = keymap.getName();

    if (getDefaultKeymapName().equals(name)) {
      return "Default";
    }

    if (KeymapManager.DEFAULT_IDEA_KEYMAP.equals(name)) {
      return "IntelliJ IDEA Classic";
=======
      return KeymapManager.MAC_OS_X_10_5_PLUS_KEYMAP;
    }
    else if (SystemInfo.isXWindow) {
      return KeymapManager.X_WINDOW_KEYMAP;
    }
    else {
      return KeymapManager.DEFAULT_IDEA_KEYMAP;
    }
  }

  @Override
  public String getKeymapPresentableName(KeymapImpl keymap) {
    final String name = keymap.getName();

    if (getDefaultKeymapName().equals(name)) {
      return "Default";
    }

    if (KeymapManager.DEFAULT_IDEA_KEYMAP.equals(name)) {
      return "IntelliJ IDEA Classic" + (SystemInfo.isMac ? " (Windows)" : "");
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    }

    if ("Mac OS X".equals(name)) {
      return "IntelliJ IDEA Classic (OS X)";
    }

    return name;
  }
}
