/*
 * Copyright 2000-2012 JetBrains s.r.o.
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

import com.intellij.CommonBundle;
import com.intellij.codeInsight.CodeInsightBundle;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.lang.LanguageImportStatements;
import com.intellij.openapi.editor.ex.EditorSettingsExternalizable;
import com.intellij.openapi.help.HelpManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.arrangement.Rearranger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LayoutCodeDialog extends DialogWrapper implements LayoutCodeOptions {
  @NotNull  private final Project myProject;
  @Nullable private final PsiFile myFile;
  @Nullable private final PsiDirectory myDirectory;
  private final Boolean myTextSelected;

  private JRadioButton myRbFile;
  private JRadioButton myRbSelectedText;
  private JRadioButton myRbDirectory;
  private JCheckBox    myCbIncludeSubdirs;
  private JCheckBox    myCbOptimizeImports;
  private JCheckBox    myCbArrangeEntries;
  private JCheckBox    myCbOnlyVcsChangedRegions;
  private JCheckBox    myDoNotAskMeCheckBox;

  private final String myHelpId;
  @Nullable private CommonCodeStyleSettings myCommonSettings;
  private boolean myRearrangeAlwaysEnabled;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
=======

  private final boolean myOptimizeImportProcessorsForFileLanguageExists;
  private final boolean myRearrangerProcessorsForFileLanguageExists;
  private final boolean myFileHasChanges;

  private boolean myOptimizeImportsSelected;
  private boolean myFormatOnlyVCSChangedRegionsSelected;
  private boolean myDoNotShowDialogSelected;
  private boolean myRearrangeEntriesSelected;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)


  public  LayoutCodeDialog(@NotNull Project project,
                          @NotNull String title,
                          @Nullable PsiFile file,
                          @Nullable PsiDirectory directory,
                          Boolean isTextSelected,
                          final String helpId) {
    super(project, true);
    myFile = file;
    myProject = project;
    myDirectory = directory;
    myTextSelected = isTextSelected;

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    if (myFile != null) myCommonSettings = CodeStyleSettingsManager.getSettings(myProject).getCommonSettings(myFile.getLanguage());
    myRearrangeAlwaysEnabled = myCommonSettings != null
                               && myCommonSettings.isForceArrangeMenuAvailable()
                               && myCommonSettings.FORCE_REARRANGE_MODE == CommonCodeStyleSettings.REARRANGE_ALWAYS;
=======
    myOptimizeImportProcessorsForFileLanguageExists = myFile != null && !LanguageImportStatements.INSTANCE.forFile(myFile).isEmpty();
    myRearrangerProcessorsForFileLanguageExists = myFile != null && Rearranger.EXTENSION.forLanguage(myFile.getLanguage()) != null;
    myFileHasChanges = myFile != null && FormatChangedTextUtil.hasChanges(myFile);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
=======
    if (myFile != null) myCommonSettings = CodeStyleSettingsManager.getSettings(myProject).getCommonSettings(myFile.getLanguage());
    myRearrangeAlwaysEnabled = myCommonSettings != null
                               && myCommonSettings.isForceArrangeMenuAvailable()
                               && myCommonSettings.FORCE_REARRANGE_MODE == CommonCodeStyleSettings.REARRANGE_ALWAYS;

>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    setOKButtonText(CodeInsightBundle.message("reformat.code.accept.button.text"));
    setTitle(title);
    init();
    myHelpId = helpId;
  }

  @Override
  protected void init() {
    super.init();

    loadCbsStates();
    setUpInitialSelection();

    myRbFile.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        saveEnabledCbsSelectedState();
        setUpCbsStateForFileFormatting();
      }
    });

    myRbDirectory.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        saveEnabledCbsSelectedState();
        setUpCbsStatesForDirectoryFormatting();
      }
    });

    myRbSelectedText.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        saveEnabledCbsSelectedState();
        setUpCbsStatesForSelectedTextFormatting();
      }
    });
  }

  private void setUpInitialSelection() {
    if (myTextSelected == Boolean.TRUE) {
      myRbSelectedText.setSelected(true);
      setUpCbsStatesForSelectedTextFormatting();
    }
    else {
      if (myFile != null) {
        myRbFile.setSelected(true);
        setUpCbsStateForFileFormatting();
      }
      else {
        myRbDirectory.setSelected(true);
        setUpCbsStatesForDirectoryFormatting();
      }
    }
    myCbIncludeSubdirs.setSelected(true);
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    //Loading previous state
    myCbOptimizeImports.setSelected(PropertiesComponent.getInstance().getBoolean(LayoutCodeConstants.OPTIMIZE_IMPORTS_KEY, false));
    myCbArrangeEntries.setSelected(myRearrangeAlwaysEnabled || ReformatCodeAction.getLastSavedRearrangeCbState(myProject, myFile));
    myCbOnlyVcsChangedRegions.setSelected(PropertiesComponent.getInstance().getBoolean(LayoutCodeConstants.PROCESS_CHANGED_TEXT_KEY, false));

    ItemListener listener = new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        updateState();
      }
    };
    myRbFile.addItemListener(listener);
    myRbSelectedText.addItemListener(listener);
    myRbDirectory.addItemListener(listener);
    myCbIncludeSubdirs.addItemListener(listener);

    updateState();
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  private void updateState() {
    myCbIncludeSubdirs.setEnabled(myRbDirectory.isSelected());
    myCbOptimizeImports.setEnabled(
      !myRbSelectedText.isSelected()
      && !(myFile != null && LanguageImportStatements.INSTANCE.forFile(myFile).isEmpty() && myRbFile.isSelected())
    );
    myCbArrangeEntries.setEnabled(isProcessDirectory()
                                  || myFile != null
                                     && Rearranger.EXTENSION.forLanguage(myFile.getLanguage()) != null
                                     && !myRearrangeAlwaysEnabled
    );
    if (myRearrangeAlwaysEnabled && !myCbArrangeEntries.isEnabled()) {
      myCbArrangeEntries.setSelected(true);
    }
    myCbOnlyVcsChangedRegions.setEnabled(canTargetVcsRegions());
    myDoNotAskMeCheckBox.setEnabled(!myRbDirectory.isSelected());
    myRbDirectory.setEnabled(!myDoNotAskMeCheckBox.isSelected());
=======
  private void loadCbsStates() {
    myOptimizeImportsSelected = PropertiesComponent.getInstance().getBoolean(LayoutCodeConstants.OPTIMIZE_IMPORTS_KEY, false);
    myRearrangeEntriesSelected = myRearrangeAlwaysEnabled || ReformatCodeAction.getLastSavedRearrangeCbState(myProject, myFile);
    myFormatOnlyVCSChangedRegionsSelected = PropertiesComponent.getInstance().getBoolean(LayoutCodeConstants.PROCESS_CHANGED_TEXT_KEY, false);
  }

  private void saveEnabledCbsSelectedState() {
    if (myCbArrangeEntries.isEnabled()) {
      myRearrangeEntriesSelected = myCbArrangeEntries.isSelected();
    }
    if (myCbOptimizeImports.isEnabled()) {
      myOptimizeImportsSelected = myCbOptimizeImports.isSelected();
    }
    if (myCbOnlyVcsChangedRegions.isEnabled()) {
      myFormatOnlyVCSChangedRegionsSelected = myCbOnlyVcsChangedRegions.isSelected();
    }
    if (myDoNotAskMeCheckBox.isEnabled()) {
      myDoNotShowDialogSelected = myDoNotAskMeCheckBox.isSelected();
    }
  }

  private void setUpCbsStateForFileFormatting() {
    myCbOptimizeImports.setEnabled(myOptimizeImportProcessorsForFileLanguageExists);
    myCbOptimizeImports.setSelected(myOptimizeImportProcessorsForFileLanguageExists && myOptimizeImportsSelected);

    myCbArrangeEntries.setEnabled(myRearrangerProcessorsForFileLanguageExists);
    myCbArrangeEntries.setSelected(myRearrangerProcessorsForFileLanguageExists && myRearrangeEntriesSelected);

    myCbOnlyVcsChangedRegions.setEnabled(myFileHasChanges);
    myCbOnlyVcsChangedRegions.setSelected(myFileHasChanges && myFormatOnlyVCSChangedRegionsSelected);

    myDoNotAskMeCheckBox.setEnabled(true);
    myDoNotAskMeCheckBox.setSelected(myDoNotShowDialogSelected);

    myCbIncludeSubdirs.setEnabled(false);
  }

  private void setUpCbsStatesForDirectoryFormatting() {
    myCbOptimizeImports.setEnabled(true);
    myCbOptimizeImports.setSelected(myOptimizeImportsSelected);

    myCbArrangeEntries.setEnabled(true);
    myCbArrangeEntries.setSelected(myRearrangeEntriesSelected);

    //TODO enable it when getting changed ranges will be fixed
    myCbOnlyVcsChangedRegions.setEnabled(false);
    myCbOnlyVcsChangedRegions.setSelected(false);

    myDoNotAskMeCheckBox.setEnabled(false);
    myDoNotAskMeCheckBox.setSelected(false);

    myCbIncludeSubdirs.setEnabled(true);
  }

  private void setUpCbsStatesForSelectedTextFormatting() {
    myCbOptimizeImports.setEnabled(false);
    myCbOptimizeImports.setSelected(false);

    myCbArrangeEntries.setEnabled(true);
    myCbArrangeEntries.setSelected(myRearrangeEntriesSelected);

    myCbOnlyVcsChangedRegions.setEnabled(false);
    myCbOnlyVcsChangedRegions.setSelected(false);

    myDoNotAskMeCheckBox.setEnabled(true);
    myDoNotAskMeCheckBox.setSelected(myDoNotShowDialogSelected);

    myCbIncludeSubdirs.setEnabled(false);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

  @Override
  protected JComponent createCenterPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 0));
    GridBagConstraints gbConstraints = new GridBagConstraints();
    gbConstraints.gridy = 0;
    gbConstraints.gridx = 0;
    gbConstraints.gridwidth = 3;
    gbConstraints.gridheight = 1;
    gbConstraints.weightx = 1;

    gbConstraints.fill = GridBagConstraints.BOTH;
    gbConstraints.insets = new Insets(0, 0, 0, 0);

    myRbFile = new JRadioButton(CodeInsightBundle.message("process.scope.file",
                                                          (myFile != null ? "'" + myFile.getVirtualFile().getPresentableUrl() + "'" : "")));
    panel.add(myRbFile, gbConstraints);

    myRbSelectedText = new JRadioButton(CodeInsightBundle.message("reformat.option.selected.text"));
    if (myTextSelected != null) {
      gbConstraints.gridy++;
      gbConstraints.insets = new Insets(0, 0, 0, 0);
      panel.add(myRbSelectedText, gbConstraints);
    }

    myRbDirectory = new JRadioButton();
    myCbIncludeSubdirs = new JCheckBox(CodeInsightBundle.message("reformat.option.include.subdirectories"));
    if (myDirectory != null) {
      myRbDirectory.setText(CodeInsightBundle.message("reformat.option.all.files.in.directory",
                                                      myDirectory.getVirtualFile().getPresentableUrl()));
      gbConstraints.gridy++;
      gbConstraints.insets = new Insets(0, 0, 0, 0);
      panel.add(myRbDirectory, gbConstraints);

      if (myDirectory.getSubdirectories().length > 0) {
        gbConstraints.gridy++;
        gbConstraints.insets = new Insets(0, 20, 0, 0);
        panel.add(myCbIncludeSubdirs, gbConstraints);
      }
    }

    myCbOptimizeImports = new JCheckBox(CodeInsightBundle.message("reformat.option.optimize.imports"));
    if (myTextSelected != null && LanguageImportStatements.INSTANCE.hasAnyExtensions()) {
      gbConstraints.gridy++;
      gbConstraints.insets = new Insets(0, 0, 0, 0);
      panel.add(myCbOptimizeImports, gbConstraints);
    }

    myCbArrangeEntries = new JCheckBox(CodeInsightBundle.message("reformat.option.rearrange.entries"));
    if (myDirectory != null || myFile != null && Rearranger.EXTENSION.forLanguage(myFile.getLanguage()) != null)
    {
      gbConstraints.gridy++;
      gbConstraints.insets = new Insets(0, 0, 0, 0);
      panel.add(myCbArrangeEntries, gbConstraints);
    }

    myCbOnlyVcsChangedRegions = new JCheckBox(CodeInsightBundle.message("reformat.option.vcs.changed.region"));
    gbConstraints.gridy++;
    panel.add(myCbOnlyVcsChangedRegions, gbConstraints);

    ButtonGroup buttonGroup = new ButtonGroup();
    buttonGroup.add(myRbFile);
    buttonGroup.add(myRbSelectedText);
    buttonGroup.add(myRbDirectory);

    myRbFile.setEnabled(myFile != null);
    myRbSelectedText.setEnabled(myTextSelected == Boolean.TRUE);

    return panel;
  }

  @Override
  protected JComponent createSouthPanel() {
    JComponent southPanel = super.createSouthPanel();
    myDoNotAskMeCheckBox = new JCheckBox(CommonBundle.message("dialog.options.do.not.show"));
    return DialogWrapper.addDoNotShowCheckBox(southPanel, myDoNotAskMeCheckBox);
  }

  @NotNull
  @Override
  protected Action[] createActions() {
    return new Action[]{getOKAction(), getCancelAction(), getHelpAction()};
  }

  @Override
  protected void doHelpAction() {
    HelpManager.getInstance().invokeHelp(myHelpId);
  }

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")

  public boolean isProcessSelectedText() {
    return myRbSelectedText.isSelected();
  }

=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  @Override
  public boolean isProcessWholeFile() {
    return myRbFile.isSelected();
  }

  @Override
  public boolean isProcessDirectory() {
    return myRbDirectory.isSelected();
  }

  @Override
  public boolean isIncludeSubdirectories() {
    return myCbIncludeSubdirs.isSelected();
  }

  @Override
  public boolean isOptimizeImports() {
    return myCbOptimizeImports.isSelected();
  }

  @Override
  public boolean isRearrangeEntries() {
    return myCbArrangeEntries.isSelected();
  }

  @Override
  public boolean isProcessOnlyChangedText() {
    return myCbOnlyVcsChangedRegions.isEnabled() && myCbOnlyVcsChangedRegions.isSelected();
  }

  public boolean isDoNotAskMe() {
    if (myDoNotAskMeCheckBox.isEnabled()) {
      return myDoNotAskMeCheckBox.isSelected();
    }
    else {
      return !EditorSettingsExternalizable.getInstance().getOptions().SHOW_REFORMAT_DIALOG;
    }
  }

  @Override
  protected void doOKAction() {
    super.doOKAction();
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    PropertiesComponent.getInstance().setValue(LayoutCodeConstants.OPTIMIZE_IMPORTS_KEY, Boolean.toString(myCbOptimizeImports.isSelected()));
    PropertiesComponent.getInstance().setValue(LayoutCodeConstants.PROCESS_CHANGED_TEXT_KEY, Boolean.toString(myCbOnlyVcsChangedRegions.isSelected()));
    saveRearrangeCbState(myCbArrangeEntries.isSelected());
  }

  private void saveRearrangeCbState(boolean isSelected) {
    if (myFile != null)
      LayoutCodeSettingsStorage.saveRearrangeEntriesOptionFor(myProject, myFile.getLanguage(), isSelected);
    else
      LayoutCodeSettingsStorage.saveRearrangeEntriesOptionFor(myProject, isSelected);
=======
    persistEnabledCbsStates();
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

  private void persistEnabledCbsStates() {
    if (myCbOptimizeImports.isEnabled()) {
      String optimizeImports = Boolean.toString(myCbOptimizeImports.isSelected());
      PropertiesComponent.getInstance().setValue(LayoutCodeConstants.OPTIMIZE_IMPORTS_KEY, optimizeImports);
    }
    if (myCbOnlyVcsChangedRegions.isEnabled()) {
      String formatVcsChangedRegions = Boolean.toString(myCbOnlyVcsChangedRegions.isSelected());
      PropertiesComponent.getInstance().setValue(LayoutCodeConstants.PROCESS_CHANGED_TEXT_KEY, formatVcsChangedRegions);
    }
    if (myCbArrangeEntries.isEnabled()) {
      saveRearrangeCbState(myCbArrangeEntries.isSelected());
    }
  }

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    if (isProcessWholeFile()) {
      return myFile != null && FormatChangedTextUtil.hasChanges(myFile);
    }

    if (isProcessDirectory()) {
      if (myDirectory == null) {
        return false;
      }
      return FormatChangedTextUtil.hasChanges(myDirectory);
    }
    return false;
=======
  private void saveRearrangeCbState(boolean isSelected) {
    if (myFile != null)
      LayoutCodeSettingsStorage.saveRearrangeEntriesOptionFor(myProject, myFile.getLanguage(), isSelected);
    else
      LayoutCodeSettingsStorage.saveRearrangeEntriesOptionFor(myProject, isSelected);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }
}
