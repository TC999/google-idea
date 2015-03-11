/*
 * Copyright 2000-2013 JetBrains s.r.o.
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
package org.jetbrains.plugins.gradle.service.settings;

import com.intellij.openapi.externalSystem.service.settings.AbstractExternalProjectSettingsControl;
import com.intellij.openapi.externalSystem.service.settings.AbstractImportFromExternalSystemControl;
import com.intellij.openapi.externalSystem.util.ExternalSystemUiUtil;
import com.intellij.openapi.externalSystem.util.PaintAwarePanel;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.gradle.settings.GradleProjectSettings;

/**
 * @author Denis Zhdanov
 * @since 4/24/13 1:45 PM
 */
public class GradleProjectSettingsControl extends AbstractExternalProjectSettingsControl<GradleProjectSettings> {

<<<<<<< HEAD   (ea1a52 Merge "Remove XML editor browser toolbar" into idea14-1.2-de)
  private static final long BALLOON_DELAY_MILLIS = TimeUnit.SECONDS.toMillis(1);

  @NotNull private final Alarm myAlarm = new Alarm(Alarm.ThreadToUse.SWING_THREAD);

  @NotNull private LocationSettingType myGradleHomeSettingType = LocationSettingType.UNKNOWN;

  @NotNull private final GradleInstallationManager myInstallationManager;

  @SuppressWarnings("FieldCanBeLocal") // Used implicitly by reflection at disposeUIResources() and showUi()
  private JLabel                    myGradleHomeLabel;
  @SuppressWarnings("FieldCanBeLocal") // Used implicitly by reflection at disposeUIResources() and showUi()
  private JLabel myGradleJdkLabel;
  private ExternalSystemJdkComboBox myGradleJdkComboBox;
  private TextFieldWithBrowseButton myGradleHomePathField;
  private JBRadioButton             myUseWrapperButton;
  private JBRadioButton             myUseLocalDistributionButton;
  private JBRadioButton             myUseBundledDistributionButton;

  private boolean myShowBalloonIfNecessary;
=======
  private final GradleProjectSettingsControlBuilder myBuilder;
>>>>>>> BRANCH (fffd69 Snapshot idea/141.104.1 from git://git.jetbrains.org/idea/co)

  public GradleProjectSettingsControl(@NotNull GradleProjectSettings initialSettings) {
    this(GradleSettingsControlProvider.get().getProjectSettingsControlBuilder(initialSettings));
  }

  public GradleProjectSettingsControl(@NotNull GradleProjectSettingsControlBuilder builder) {
    super(null, builder.getInitialSettings(), builder.getExternalSystemSettingsControlCustomizer());
    myBuilder = builder;
  }

  @Override
  protected void fillExtraControls(@NotNull PaintAwarePanel content, int indentLevel) {
<<<<<<< HEAD   (ea1a52 Merge "Remove XML editor browser toolbar" into idea14-1.2-de)
    content.setPaintCallback(new Consumer<Graphics>() {
      @Override
      public void consume(Graphics graphics) {
        showBalloonIfNecessary();
      }
    });

    content.addPropertyChangeListener(new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if (!"ancestor".equals(evt.getPropertyName())) {
          return;
        }

        // Configure the balloon to show on initial configurable drawing.
        myShowBalloonIfNecessary = evt.getNewValue() != null && evt.getOldValue() == null;

        if (evt.getNewValue() == null && evt.getOldValue() != null) {
          // Cancel delayed balloons when the configurable is hidden.
          myAlarm.cancelAllRequests();
        }
      }
    });

    myGradleHomeLabel = new JBLabel(GradleBundle.message("gradle.settings.text.home.path"));
    initGradleHome();
    myGradleJdkLabel = new JBLabel(GradleBundle.message("gradle.settings.text.jvm.path"));
    myGradleJdkComboBox = new ExternalSystemJdkComboBox().withoutJre();

    initControls();
    content.add(myUseWrapperButton, ExternalSystemUiUtil.getFillLineConstraints(indentLevel));
    //content.add(Box.createGlue(), ExternalSystemUiUtil.getFillLineConstraints(indentLevel));
    // Hide bundled distribution option for a while
    // content.add(myUseBundledDistributionButton, ExternalSystemUiUtil.getFillLineConstraints(indentLevel));
    content.add(myUseLocalDistributionButton, ExternalSystemUiUtil.getFillLineConstraints(indentLevel));

    content.add(myGradleHomeLabel, ExternalSystemUiUtil.getLabelConstraints(indentLevel));
    content.add(myGradleHomePathField, ExternalSystemUiUtil.getFillLineConstraints(0));

    content.add(myGradleJdkLabel, ExternalSystemUiUtil.getLabelConstraints(indentLevel));
    content.add(myGradleJdkComboBox, ExternalSystemUiUtil.getFillLineConstraints(0));
  }

  private void initControls() {
    ActionListener listener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        boolean localDistributionEnabled = myUseLocalDistributionButton.isSelected();
        myGradleHomePathField.setEnabled(localDistributionEnabled);
        if (localDistributionEnabled) {
          if(myGradleHomePathField.getText().isEmpty()){
            deduceGradleHomeIfPossible();
          } else {
            if(myInstallationManager.isGradleSdkHome(myGradleHomePathField.getText())){
              myGradleHomeSettingType = LocationSettingType.EXPLICIT_CORRECT;
            } else {
              myGradleHomeSettingType = LocationSettingType.EXPLICIT_INCORRECT;
              myShowBalloonIfNecessary = true;
            }
          }
          showBalloonIfNecessary();
        }
        else {
          myAlarm.cancelAllRequests();
        }
      }
    };

    myUseWrapperButton = new JBRadioButton(GradleBundle.message("gradle.settings.text.use.default_wrapper.configured"));
    myUseWrapperButton.addActionListener(listener);

    myUseLocalDistributionButton = new JBRadioButton(GradleBundle.message("gradle.settings.text.use.local.distribution"));
    myUseLocalDistributionButton.addActionListener(listener);

    myUseBundledDistributionButton = new JBRadioButton(
      GradleBundle.message("gradle.settings.text.use.bundled.distribution", GradleVersion.current().getVersion()));
    myUseBundledDistributionButton.addActionListener(listener);
    myUseBundledDistributionButton.setEnabled(false);

    ButtonGroup buttonGroup = new ButtonGroup();
    buttonGroup.add(myUseWrapperButton);
    buttonGroup.add(myUseBundledDistributionButton);
    buttonGroup.add(myUseLocalDistributionButton);
  }

  private void initGradleHome() {
    myGradleHomePathField = new TextFieldWithBrowseButton();

    FileChooserDescriptor fileChooserDescriptor = GradleUtil.getGradleHomeFileChooserDescriptor();

    myGradleHomePathField.addBrowseFolderListener(
      "",
      GradleBundle.message("gradle.settings.text.home.path"),
      null,
      fileChooserDescriptor,
      TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT,
      false
    );
    myGradleHomePathField.getTextField().getDocument().addDocumentListener(new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
        myGradleHomePathField.getTextField().setForeground(LocationSettingType.EXPLICIT_CORRECT.getColor());
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        myGradleHomePathField.getTextField().setForeground(LocationSettingType.EXPLICIT_CORRECT.getColor());
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
      }
    });
=======
    myBuilder.createAndFillControls(content, indentLevel);
>>>>>>> BRANCH (fffd69 Snapshot idea/141.104.1 from git://git.jetbrains.org/idea/co)
  }

  @Override
  public boolean validate(@NotNull GradleProjectSettings settings) throws ConfigurationException {
    return myBuilder.validate(settings);
  }

  @Override
  protected void applyExtraSettings(@NotNull GradleProjectSettings settings) {
<<<<<<< HEAD   (ea1a52 Merge "Remove XML editor browser toolbar" into idea14-1.2-de)
    String gradleHomePath = FileUtil.toCanonicalPath(myGradleHomePathField.getText());
    if (StringUtil.isEmpty(gradleHomePath)) {
      settings.setGradleHome(null);
    }
    else {
      settings.setGradleHome(gradleHomePath);
      GradleUtil.storeLastUsedGradleHome(gradleHomePath);
    }

    final String gradleJvm = FileUtil.toCanonicalPath(myGradleJdkComboBox.getSelectedValue());
    settings.setGradleJvm(StringUtil.isEmpty(gradleJvm) ? null : gradleJvm);

    if (myUseLocalDistributionButton.isSelected()) {
      settings.setDistributionType(DistributionType.LOCAL);
    } else if(myUseWrapperButton.isSelected()) {
      settings.setDistributionType(DistributionType.DEFAULT_WRAPPED);
    }
=======
    myBuilder.apply(settings);
>>>>>>> BRANCH (fffd69 Snapshot idea/141.104.1 from git://git.jetbrains.org/idea/co)
  }

  @Override
  protected void updateInitialExtraSettings() {
<<<<<<< HEAD   (ea1a52 Merge "Remove XML editor browser toolbar" into idea14-1.2-de)
    String gradleHomePath = FileUtil.toCanonicalPath(myGradleHomePathField.getText());
    getInitialSettings().setGradleHome(StringUtil.isEmpty(gradleHomePath) ? null : gradleHomePath);
    final String gradleJvm = FileUtil.toCanonicalPath(myGradleJdkComboBox.getSelectedValue());
    getInitialSettings().setGradleJvm(StringUtil.isEmpty(gradleJvm) ? null : gradleJvm);
    if (myUseLocalDistributionButton.isSelected()) {
      getInitialSettings().setDistributionType(DistributionType.LOCAL);
    } else if(myUseWrapperButton.isSelected()) {
      getInitialSettings().setDistributionType(DistributionType.DEFAULT_WRAPPED);
    }
=======
    myBuilder.apply(getInitialSettings());
>>>>>>> BRANCH (fffd69 Snapshot idea/141.104.1 from git://git.jetbrains.org/idea/co)
  }

  @Override
  protected boolean isExtraSettingModified() {
<<<<<<< HEAD   (ea1a52 Merge "Remove XML editor browser toolbar" into idea14-1.2-de)
    DistributionType distributionType = getInitialSettings().getDistributionType();
    if (myUseBundledDistributionButton.isSelected() && distributionType != DistributionType.BUNDLED) {
      return true;
    }

    if (myUseWrapperButton.isSelected() && distributionType != DistributionType.DEFAULT_WRAPPED) {
        return true;
    }

    if (myUseLocalDistributionButton.isSelected() && distributionType != DistributionType.LOCAL) {
      return true;
    }

    if (!StringUtil.equals(myGradleJdkComboBox.getSelectedValue(), getInitialSettings().getGradleJvm())) {
      return true;
    }

    String gradleHome = FileUtil.toCanonicalPath(myGradleHomePathField.getText());
    if (StringUtil.isEmpty(gradleHome)) {
      return !StringUtil.isEmpty(getInitialSettings().getGradleHome());
    }
    else {
      return !gradleHome.equals(getInitialSettings().getGradleHome());
    }
=======
    return myBuilder.isModified();
>>>>>>> BRANCH (fffd69 Snapshot idea/141.104.1 from git://git.jetbrains.org/idea/co)
  }

  @Override
  protected void resetExtraSettings(boolean isDefaultModuleCreation) {
    myBuilder.reset(getProject(), getInitialSettings(), isDefaultModuleCreation);
  }

<<<<<<< HEAD   (ea1a52 Merge "Remove XML editor browser toolbar" into idea14-1.2-de)
  public void updateWrapperControls(@Nullable String linkedProjectPath, boolean isDefaultModuleCreation) {
    if(StringUtil.isEmpty(linkedProjectPath) && !isDefaultModuleCreation) {
        myUseLocalDistributionButton.setSelected(true);
        myGradleHomePathField.setEnabled(true);
        return;
    }

    final boolean isGradleDefaultWrapperFilesExist = GradleUtil.isGradleDefaultWrapperFilesExist(linkedProjectPath);
    if (isGradleDefaultWrapperFilesExist || isDefaultModuleCreation) {
      myUseWrapperButton.setEnabled(true);
      myUseWrapperButton.setSelected(true);
      myGradleHomePathField.setEnabled(false);
      myUseWrapperButton.setText(GradleBundle.message("gradle.settings.text.use.default_wrapper.configured"));
    } else {
      myUseWrapperButton.setEnabled(false);
      myUseLocalDistributionButton.setSelected(true);
      myGradleHomePathField.setEnabled(true);
      myUseWrapperButton.setText(GradleBundle.message("gradle.settings.text.use.default_wrapper.not_configured"));
    }

    if(getInitialSettings().getDistributionType() == null) {
      return;
    }

    switch (getInitialSettings().getDistributionType()) {
      case LOCAL:
        myGradleHomePathField.setEnabled(true);
        myUseLocalDistributionButton.setSelected(true);
        break;
      case WRAPPED:
        getInitialSettings().setDistributionType(DistributionType.DEFAULT_WRAPPED);
      case DEFAULT_WRAPPED:
        myGradleHomePathField.setEnabled(false);
        myUseWrapperButton.setSelected(true);
        myUseWrapperButton.setEnabled(true);
        break;
      case BUNDLED:
        myGradleHomePathField.setEnabled(false);
        myUseBundledDistributionButton.setSelected(true);
        break;
    }
=======
  public void update(@Nullable String linkedProjectPath, boolean isDefaultModuleCreation) {
    myBuilder.update(linkedProjectPath, getInitialSettings(), isDefaultModuleCreation);
>>>>>>> BRANCH (fffd69 Snapshot idea/141.104.1 from git://git.jetbrains.org/idea/co)
  }

  @Override
  public void showUi(boolean show) {
    super.showUi(show);
    myBuilder.showUi(show);
  }

  /**
   * see {@linkplain AbstractImportFromExternalSystemControl#setCurrentProject(Project)}
   */
  public void setCurrentProject(@Nullable Project project) {
    super.setCurrentProject(project);
    myBuilder.reset(getProject(), getInitialSettings(), false);
  }

  @Override
  public void disposeUIResources() {
    super.disposeUIResources();
    myBuilder.disposeUIResources();
  }
}
