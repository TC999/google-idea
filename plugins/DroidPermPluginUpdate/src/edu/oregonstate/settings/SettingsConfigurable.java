/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.oregonstate.settings;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.jgoodies.common.base.Objects;
import edu.oregonstate.ui.SettingsPanel;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 *
 * Configuration interface for {@link PersistentSettings}.
 */
class SettingsConfigurable implements SearchableConfigurable {

    private final PersistentSettings globalSettings;
    private SettingsPanel settingsPanel = null;

    public SettingsConfigurable() {
        globalSettings = PersistentSettings.getInstance();
    }

    @NotNull
    @Override
    public String getId() {
        return "DroidPermPlugin";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String s) {
        return null;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return getId();
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    /**
     * createComponent: creates the UI visual form and returns its root element.
     * @return fully instantiated JPanel (derived from JComponent) representing the perm-refactor plugin settings
     */
    @Nullable
    @Override
    public JComponent createComponent() {
        if (settingsPanel == null) settingsPanel = new SettingsPanel();
        reset();
        return settingsPanel.panel;
    }

    /**
     * isModified: this method is regularly called to check the form for changes.
     * If the method returns false, the Apply button is disabled.
     * @return true if elements within the component form have been modified, false otherwise
     */
    @Override
    public boolean isModified() {
        return settingsPanel == null
                || !Objects.equals(globalSettings.droidPermHome, settingsPanel.droidPermHome.getText())
                || !Objects.equals(globalSettings.notifications,
                        settingsPanel.notificationsCheckbox.isSelected());
    }

    /**
     * apply: this method is called when the user clicks the OK or Apply button.
     * @throws ConfigurationException thrown to indicate that SettingsConfigurable cannot apply entered values
     */
    @Override
    public void apply() throws ConfigurationException {
        if (settingsPanel != null) {
            globalSettings.droidPermHome = settingsPanel.droidPermHome.getText();
            globalSettings.notifications = settingsPanel.notificationsCheckbox.isSelected();
        }
    }

    /**
     * reset: this method is called when the user clicks the Cancel button.
     */
    @Override
    public void reset() {
        if (settingsPanel != null) {
            settingsPanel.droidPermHome.setText(globalSettings.droidPermHome);
            settingsPanel.notificationsCheckbox.setSelected(globalSettings.notifications);
        }
    }

    /**
     * disposeUIResources: this method is called when the user closes the form.
     * In this method, resources used by the form are released.
     */
    @Override
    public void disposeUIResources() {
        settingsPanel = null;
    }
}
