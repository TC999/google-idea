/*
 * Copyright 2000-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Created by IntelliJ IDEA.
 * User: max
 * Date: May 14, 2002
 * Time: 6:29:03 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.intellij.openapi.editor.actions;

import com.intellij.openapi.editor.actionSystem.EditorAction;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.util.text.CharArrayUtil;
import org.jetbrains.annotations.NotNull;
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

public class CutLineEndAction extends EditorAction {
  public CutLineEndAction() {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    super(new Handler(true));
  }

  static class Handler extends EditorWriteActionHandler {
    private final boolean myCopyToClipboard;

    Handler(boolean copyToClipboard) {
      myCopyToClipboard = copyToClipboard;
    }

    @Override
    public void executeWriteAction(Editor editor, DataContext dataContext) {
      final Document doc = editor.getDocument();
      int caretOffset = editor.getCaretModel().getOffset();
      if (caretOffset >= doc.getTextLength()) {
        return;
      }
      final int lineNumber = doc.getLineNumber(caretOffset);
      int lineEndOffset = doc.getLineEndOffset(lineNumber);

      if (editor.isColumnMode() && editor.getCaretModel().supportsMultipleCarets() && caretOffset == lineEndOffset) {
        return;
      }

      int start;
      int end;
      if (caretOffset >= lineEndOffset) {
        start = lineEndOffset;
        end = lineEndOffset + 1;
      }
      else {
        start = caretOffset;
        end = lineEndOffset;
        if (lineEndOffset < doc.getTextLength() && CharArrayUtil.isEmptyOrSpaces(doc.getCharsSequence(), caretOffset, lineEndOffset)) {
          end++;
        }
      }

      delete(editor, start, end);
    }

    private void delete(@NotNull Editor editor, int start, int end) {
      if (myCopyToClipboard) {
        KillRingUtil.copyToKillRing(editor, start, end, true);
      }
      else {
        CopyPasteManager.getInstance().stopKillRings();
      }
      editor.getDocument().deleteString(start, end);
    }
=======
    super(new CutLineActionHandler(false, true, true));
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }
}
