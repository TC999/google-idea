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

package com.intellij.codeInsight.lookup.impl;

import com.intellij.codeInsight.completion.CompletionProgressIndicator;
import com.intellij.codeInsight.completion.impl.CompletionServiceImpl;
import com.intellij.codeInsight.lookup.LookupManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;

public class BackspaceHandler extends EditorActionHandler {
  private final EditorActionHandler myOriginalHandler;

  public BackspaceHandler(EditorActionHandler originalHandler){
    myOriginalHandler = originalHandler;
  }

  @Override
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  public void execute(final Editor editor, Caret caret, final DataContext dataContext){
=======
  public void doExecute(final Editor editor, Caret caret, final DataContext dataContext){
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    LookupImpl lookup = (LookupImpl)LookupManager.getActiveLookup(editor);
    if (lookup == null){
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
      myOriginalHandler.executeInCaretContext(editor, caret, dataContext);
=======
      myOriginalHandler.execute(editor, caret, dataContext);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
      return;
    }

    int hideOffset = lookup.getLookupStart();
    int originalStart = lookup.getLookupOriginalStart();
    if (originalStart >= 0 && originalStart <= hideOffset) {
      hideOffset = originalStart - 1;
    }
    
    truncatePrefix(dataContext, lookup, myOriginalHandler, hideOffset, caret);
  }

  static void truncatePrefix(final DataContext dataContext,
                             LookupImpl lookup,
                             final EditorActionHandler handler,
                             final int hideOffset,
                             final Caret caret) {
    final Editor editor = lookup.getEditor();
    if (!lookup.performGuardedChange(new Runnable() {
      @Override
      public void run() {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
        handler.executeInCaretContext(editor, caret, dataContext);
=======
        handler.execute(editor, caret, dataContext);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
      }
    })) {
      return;
    }

    final CompletionProgressIndicator process = CompletionServiceImpl.getCompletionService().getCurrentCompletion();
    if (lookup.truncatePrefix(process == null || !process.isAutopopupCompletion())) {
      return;
    }

    if (process != null) {
      if (hideOffset < editor.getCaretModel().getOffset()) {
        process.scheduleRestart();
        return;
      }
      process.prefixUpdated();
    }

    lookup.hide();
  }
}
