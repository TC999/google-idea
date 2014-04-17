package com.intellij.xdebugger.impl.ui;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.impl.DocumentImpl;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;
import org.jetbrains.annotations.NotNull;

public final class TextViewer extends EditorTextField {
  private final boolean myEmbeddedIntoDialogWrapper;
  private final boolean myUseSoftWraps;

  public TextViewer(@NotNull Project project, boolean embeddedIntoDialogWrapper, boolean useSoftWraps) {
    this(createDocument(""), project, embeddedIntoDialogWrapper, useSoftWraps);
  }

  public TextViewer(@NotNull String initialText, @NotNull Project project) {
    this(createDocument(initialText), project, false, false);
  }

  public TextViewer(@NotNull Document document, @NotNull Project project, boolean embeddedIntoDialogWrapper, boolean useSoftWraps) {
    super(document, project, FileTypes.PLAIN_TEXT, true, false);

    myEmbeddedIntoDialogWrapper = embeddedIntoDialogWrapper;
    myUseSoftWraps = useSoftWraps;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  }

  private static Document createDocument(@NotNull String initialText) {
    final Document document = EditorFactory.getInstance().createDocument(initialText);
    if (document instanceof DocumentImpl) {
      ((DocumentImpl)document).setAcceptSlashR(true);
    }
    return document;
  }

  @Override
  protected EditorEx createEditor() {
    final EditorEx editor = super.createEditor();
    editor.setHorizontalScrollbarVisible(true);
=======
    setFontInheritedFromLAF(false);
  }

  private static Document createDocument(@NotNull String initialText) {
    final Document document = EditorFactory.getInstance().createDocument(initialText);
    if (document instanceof DocumentImpl) {
      ((DocumentImpl)document).setAcceptSlashR(true);
    }
    return document;
  }

  @Override
  protected EditorEx createEditor() {
    final EditorEx editor = super.createEditor();
    editor.setHorizontalScrollbarVisible(true);
    editor.setCaretEnabled(true);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    editor.setVerticalScrollbarVisible(true);
    editor.setEmbeddedIntoDialogWrapper(myEmbeddedIntoDialogWrapper);
    editor.getComponent().setPreferredSize(null);
    editor.getSettings().setUseSoftWraps(myUseSoftWraps);
    return editor;
  }
}
