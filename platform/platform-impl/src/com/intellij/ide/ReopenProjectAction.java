/*
 * Copyright 2000-2011 JetBrains s.r.o.
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
package com.intellij.ide;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.io.FileUtil;

import java.awt.event.InputEvent;
import java.io.File;

/**
* @author yole
*/
public class ReopenProjectAction extends AnAction implements DumbAware {

  public final static DataKey<RemovedProjectListener> REMOVED_LISTENER_KEY = DataKey.create("ReopenProject.RemovedListener");

  private final String myProjectPath;
  private final String myProjectName;

  public ReopenProjectAction(final String projectPath, final String projectName, final String displayName) {
    myProjectPath = projectPath;
    myProjectName = projectName;

    final Presentation presentation = getTemplatePresentation();
    String text = projectPath.equals(displayName) ? FileUtil.getLocationRelativeToUserHome(projectPath) : displayName;
    presentation.setText(text, false);
    presentation.setDescription(projectPath);
  }


  @Override
  public void actionPerformed(AnActionEvent e) {
    final int modifiers = e.getModifiers();
    final boolean forceOpenInNewFrame = (modifiers & InputEvent.CTRL_MASK) != 0 || (modifiers & InputEvent.SHIFT_MASK) != 0;
    Project project = CommonDataKeys.PROJECT.getData(e.getDataContext());
    if (!new File(myProjectPath).exists()) {
      if (Messages.showDialog(project, "The path " + FileUtil.toSystemDependentName(myProjectPath) + " does not exist.\n" +
                                       "If it is on a removable or network drive, please make sure that the drive is connected.",
                                       "Reopen Project", new String[]{"OK", "&Remove From List"}, 0, Messages.getErrorIcon()) == 1) {
        RecentProjectsManager.getInstance().removePath(myProjectPath);
        RemovedProjectListener listener = e.getData(REMOVED_LISTENER_KEY);
        if (listener != null) {
          listener.projectRemoved(myProjectPath);
        }
      }
      return;
    }
    RecentProjectsManagerBase.getInstanceEx().doOpenProject(myProjectPath, project, forceOpenInNewFrame);
  }

  public String getProjectPath() {
    return myProjectPath;
  }

  public String getProjectName() {
    return myProjectName;
  }

  /**
   * A listener which is fired anytime this action removes a project, which a caller can register for by adding itself as a
   * {@link DataProvider} with the {@link #REMOVED_LISTENER_KEY}.
   */
  public interface RemovedProjectListener {
    void projectRemoved(String myProjectPath);
  }
}
