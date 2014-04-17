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
package com.intellij.codeInsight.template.impl.editorActions;

import com.intellij.codeInsight.editorActions.BaseEnterHandler;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.codeInsight.template.impl.TemplateManagerImpl;
import com.intellij.codeInsight.template.impl.TemplateSettings;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
=======
import com.intellij.openapi.editor.Caret;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;

public class EnterHandler extends BaseEnterHandler {
  private final EditorActionHandler myOriginalHandler;

  public EnterHandler(EditorActionHandler originalHandler) {
    super(true);
    myOriginalHandler = originalHandler;
  }

  @Override
  public boolean isEnabled(Editor editor, DataContext dataContext) {
    return myOriginalHandler.isEnabled(editor, dataContext);
  }

  @Override
  public void executeWriteAction(Editor editor, Caret caret, DataContext dataContext) {
    Project project = CommonDataKeys.PROJECT.getData(dataContext);
    if (project != null) {
      PsiDocumentManager.getInstance(project).commitDocument(editor.getDocument());
      TemplateManagerImpl templateManager = (TemplateManagerImpl)TemplateManager.getInstance(project);
      if (templateManager != null && templateManager.startTemplate(editor, TemplateSettings.ENTER_CHAR)) {
        return;
      }
    }

    myOriginalHandler.execute(editor, caret, dataContext);
  }
}
