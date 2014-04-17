package com.intellij.execution.console;

import com.intellij.openapi.editor.Editor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public abstract class GutterContentProvider {
  protected static final int MAX_LINE_END_GUTTER_WIDTH_IN_CHAR = 2;

  public void beforeUiComponentUpdate(@NotNull Editor editor) {
  }

  public void documentCleared(@NotNull Editor editor) {
  }

  public void beforeEvaluate(@NotNull Editor editor) {
  }

  public abstract boolean hasText();

  @Nullable
  public abstract String getText(int line, @NotNull Editor editor);

  @Nullable
  public abstract String getToolTip(int line, @NotNull Editor editor);

  public abstract void doAction(int line, @NotNull Editor editor);

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  public abstract void drawIcon(int line, @NotNull Graphics g, int y, @NotNull Editor editor);
=======
  public abstract boolean drawIcon(int line, @NotNull Graphics g, int y, @NotNull Editor editor);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

  public boolean isShowSeparatorLine(int line, @NotNull Editor editor) {
    return true;
  }

  public int getLineStartGutterOverlap(@NotNull Editor editor) {
    return 0;
  }
}
