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

import com.intellij.openapi.components.*;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
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
