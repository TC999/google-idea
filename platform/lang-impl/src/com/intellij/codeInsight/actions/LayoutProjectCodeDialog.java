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

package com.intellij.codeInsight.actions;

import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.help.HelpManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * @author max
 */
public class LayoutProjectCodeDialog extends DialogWrapper implements ReformatFilesOptions {
  private static @NonNls final String HELP_ID = "editing.codeReformatting";

  private final String  myText;
  private final boolean myEnableOnlyVCSChangedTextCb;
  private final boolean mySuggestOptimizeImports;
  private final Project myProject;

  
  private JCheckBox myCbOptimizeImports;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  private JCheckBox myCbOnlyVcsChangedRegions;
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  private JCheckBox myCbRearrangeEntries;

  public LayoutProjectCodeDialog(@NotNull Project project,
                                 String title,
                                 String text,
                                 boolean suggestOptimizeImports,
                                 boolean enableOnlyVCSChangedTextCb)
  {
    super(project, false);
    myText = text;
    mySuggestOptimizeImports = suggestOptimizeImports;
    myProject = project;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    myEnableOnlyVCSChangedTextCb = enableOnlyVCSChangedTextCb;
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

    setOKButtonText(CodeInsightBundle.message("reformat.code.accept.button.text"));
    setTitle(title);
    init();
  }

  @Override
  protected JComponent createCenterPanel() {
    if (!mySuggestOptimizeImports) {
      return new JLabel(myText);
    }

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    JPanel panel = new JPanel(new GridLayout(4, 1));
    myCbOptimizeImports = new JCheckBox(CodeInsightBundle.message("reformat.option.optimize.imports"));
    myCbRearrangeEntries = new JCheckBox(CodeInsightBundle.message("reformat.option.rearrange.entries"));
    myCbOnlyVcsChangedRegions = new JCheckBox(CodeInsightBundle.message("reformat.option.vcs.changed.region"));

    panel.add(new JLabel(myText));
    panel.add(myCbOptimizeImports);
    panel.add(myCbRearrangeEntries);
    panel.add(myCbOnlyVcsChangedRegions);

    myCbOptimizeImports.setSelected(PropertiesComponent.getInstance().getBoolean(LayoutCodeConstants.OPTIMIZE_IMPORTS_KEY, false));
    myCbRearrangeEntries.setSelected(LayoutCodeSettingsStorage.getLastSavedRearrangeEntriesCbStateFor(myProject));
    myCbOnlyVcsChangedRegions.setEnabled(myEnableOnlyVCSChangedTextCb);
    myCbOnlyVcsChangedRegions.setSelected(
      myEnableOnlyVCSChangedTextCb && PropertiesComponent.getInstance().getBoolean(LayoutCodeConstants.PROCESS_CHANGED_TEXT_KEY, false)
    );
=======
    JPanel panel = new JPanel(new GridLayout(3, 1));
    myCbOptimizeImports = new JCheckBox(CodeInsightBundle.message("reformat.option.optimize.imports"));
    myCbRearrangeEntries = new JCheckBox(CodeInsightBundle.message("reformat.option.rearrange.entries"));

    panel.add(new JLabel(myText));
    panel.add(myCbOptimizeImports);
    panel.add(myCbRearrangeEntries);

    myCbOptimizeImports.setSelected(PropertiesComponent.getInstance().getBoolean(LayoutCodeConstants.OPTIMIZE_IMPORTS_KEY, false));
    myCbRearrangeEntries.setSelected(LayoutCodeSettingsStorage.getLastSavedRearrangeEntriesCbStateFor(myProject));
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

    return panel;
  }
  
  @NotNull
  @Override
  protected Action[] createActions() {
    return new Action[]{getOKAction(), getCancelAction(), getHelpAction()};
  }

  @Override
  public boolean isRearrangeEntries() {
    return myCbRearrangeEntries.isSelected();
  }

  @Override
  protected void doHelpAction() {
    HelpManager.getInstance().invokeHelp(HELP_ID);
  }

  @Override
  protected void doOKAction() {
    super.doOKAction();
    if (mySuggestOptimizeImports) {
      PropertiesComponent.getInstance().setValue(LayoutCodeConstants.OPTIMIZE_IMPORTS_KEY, Boolean.toString(isOptimizeImports()));
      LayoutCodeSettingsStorage.saveRearrangeEntriesOptionFor(myProject, isRearrangeEntries());
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
      if (myEnableOnlyVCSChangedTextCb) {
        PropertiesComponent.getInstance().setValue(LayoutCodeConstants.PROCESS_CHANGED_TEXT_KEY, Boolean.toString(myCbOnlyVcsChangedRegions.isSelected()));
      }
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    }
  }

  public boolean isOptimizeImports() {
    return myCbOptimizeImports.isSelected();
  }

  public boolean isProcessOnlyChangedText() {
    return false;
  }

}
