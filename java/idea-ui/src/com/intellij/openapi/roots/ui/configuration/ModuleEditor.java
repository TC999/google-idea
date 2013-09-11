/*
 * Copyright (C) 2013 The Android Open Source Project
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
package com.intellij.openapi.roots.ui.configuration;

import com.intellij.facet.impl.ProjectFacetsConfigurator;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleConfigurationEditor;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootModel;
import com.intellij.openapi.roots.OrderEntry;
import com.intellij.ui.navigation.History;
import com.intellij.ui.navigation.Place;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.EventListener;

/**
 * Created with IntelliJ IDEA.
 * User: sbarta
 * Date: 9/10/13
 * Time: 7:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ModuleEditor extends Place.Navigator, Disposable {
  void init(History history);

  ProjectFacetsConfigurator getFacetsConfigurator();

  @Nullable
  ModuleConfigurationEditor getSelectedEditor();

  void selectEditor(String displayName);

  @Nullable
  ModuleConfigurationEditor getEditor(@NotNull String displayName);

  void addChangeListener(ChangeListener listener);

  void removeChangeListener(ChangeListener listener);

  @Nullable
  Module getModule();

  ModifiableRootModel getModifiableRootModel();

  OrderEntry[] getOrderEntries();

  ModifiableRootModel getModifiableRootModelProxy();

  ModuleRootModel getRootModel();

  boolean isModified();

  ModuleConfigurationState createModuleConfigurationState();

  JPanel getPanel();

  void moduleCountChanged();

  void updateCompilerOutputPathChanged(String baseUrl, String moduleName);

  @Override
  void dispose();

  ModifiableRootModel apply() throws ConfigurationException;

  void canApply() throws ConfigurationException;

  String getName();

  @Nullable
  String getHelpTopic();

  void setModuleName(String name);

  @Override
  void setHistory(History history);

  public interface ChangeListener extends EventListener {
    void moduleStateChanged(ModifiableRootModel moduleRootModel);
  }

  public interface ProxyDelegateAccessor {
    Object getDelegate();
  }
}
