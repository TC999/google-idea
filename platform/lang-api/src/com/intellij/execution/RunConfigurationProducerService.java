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
package com.intellij.execution;

import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.containers.ContainerUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Project component that keeps track of {@link RunConfigurationProducer} implementations that should be ignored for a given project. All
 * subclasses of classes specified here will be ignored when looking for configuration producers.
 */
@State(
  name = "RunConfigurationProducerService",
  storages = {
    @Storage(id = "default", file = StoragePathMacros.PROJECT_FILE),
    @Storage(id = "dir", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/runConfigurations.xml", scheme = StorageScheme.DIRECTORY_BASED)
  }
)
public class RunConfigurationProducerService implements PersistentStateComponent<Element> {

  @NotNull
  public static RunConfigurationProducerService getInstance(@NotNull Project project) {
    return project.getComponent(RunConfigurationProducerService.class);
  }

  private Set<Class<? extends RunConfigurationProducer<?>>> myIgnoredProducers = ContainerUtil.newHashSet();

  /**
   * Persists state in an XML element.
   *
   * <p>Example output:
   * <pre>
   * {@code
   * <component name="RunConfigurationProducerService">
   *   <ignored-producer>org.jetbrains.plugins.gradle.execution.test.runner.GradleTestRunConfigurationProducer</ignored-producer>
   * </component>
   * }
   * </pre>
   */
  @Nullable
  @Override
  public Element getState() {
    Element parent = new Element("state");
    for (Class<?> ignoredProducer : myIgnoredProducers) {
      Element producerElement = new Element("ignored-producer");
      producerElement.setText(ignoredProducer.getName());
      parent.addContent(producerElement);
    }
    return parent;
  }

  @Override
  public void loadState(Element state) {
    myIgnoredProducers.clear();

    for (Element child : state.getChildren()) {
      if (child.getName().equals("ignored-producer")) {
        try {
          @SuppressWarnings("unchecked") Class<? extends RunConfigurationProducer<?>> producer =
            (Class<? extends RunConfigurationProducer<?>>)Class.forName(child.getText());
          myIgnoredProducers.add(producer);
        }
        catch (ClassNotFoundException e) {
          // ignore unknown classes.
        }
      }
    }
  }

  @NotNull
  public Set<Class<? extends RunConfigurationProducer<?>>> getIgnoredProducers() {
    return ContainerUtil.newHashSet(myIgnoredProducers);
  }

  public void addIgnoredProducer(@NotNull Class<? extends RunConfigurationProducer<?>> ignoredProducer) {
    myIgnoredProducers.add(ignoredProducer);
  }
}
