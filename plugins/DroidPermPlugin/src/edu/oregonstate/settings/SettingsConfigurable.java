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
 * @author Nicholas Nelson <nelsonni@oregonstate.edu> Created on on 12/30/15.
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
