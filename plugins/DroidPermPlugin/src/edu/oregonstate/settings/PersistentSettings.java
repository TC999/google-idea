package edu.oregonstate.settings;

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by nelsonni on 12/30/15.
 *
 * Persistent global settings object for DroidPermPlugin.
 */

@State(
    name = "DroidPermPluginPersistentSettings",
    storages = {
        @Storage(file = StoragePathMacros.APP_CONFIG + "/DroidPermPlugin.xml")}
)

public class PersistentSettings implements PersistentStateComponent<PersistentSettings> {

    public String droidPermHome;
    public Map<String, String> templates;
    public boolean notifications;

    private PersistentSettings() {
        loadTemplates();
    }

    public static PersistentSettings getInstance() {
        return ServiceManager.getService(PersistentSettings.class);
    }

    @Nullable
    @Override
    public PersistentSettings getState() {
        return this;
    }

    @Override
    public void loadState(PersistentSettings state) {
        XmlSerializerUtil.copyBean(state, this);
        loadTemplates();
    }

    private void loadTemplates() {
        Properties properties = new Properties();
        try {
            InputStream input = PersistentSettings.class.getClassLoader().getResourceAsStream("templates.properties");
            if (input == null) throw new IOException("Missing resource: templates.properties");
            properties.load(input);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.templates = new HashMap<>();
        for (final String name: properties.stringPropertyNames()) {
            String property = properties.getProperty(name);
            templates.put(name, property);
        }
    }
}
