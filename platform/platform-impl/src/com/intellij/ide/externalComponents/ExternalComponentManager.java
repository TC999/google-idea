/*
 * Copyright 2000-2015 JetBrains s.r.o.
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
package com.intellij.ide.externalComponents;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.intellij.util.containers.MultiMap;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

/**
 * Singleton class to keep track of the external components and component sources that can be updated by the updater.
 */
public class ExternalComponentManager {
  private Set<ExternalComponentSource> mySources = Sets.newHashSet();
  private static final ExternalComponentManager myInstance = new ExternalComponentManager();

  private ExternalComponentManager() {}

  public static ExternalComponentManager getInstance() {
    return myInstance;
  }

  public ImmutableSet<ExternalComponentSource> getComponentSources() {
    return ImmutableSet.copyOf(mySources);
  }

  public void registerComponentSource(ExternalComponentSource site) {
    mySources.add(site);
  }

  /**
   * Find an installed component that could be updated by the given component.
   * @param update The potential update.
   * @param source The source for the update.
   * @return A component from the same source for which the given component is an update, or null if no such component is found.
   */
  @Nullable
  public UpdatableExternalComponent findExistingComponentMatching(UpdatableExternalComponent update, ExternalComponentSource source) {
    Collection<UpdatableExternalComponent> existing = source.getCurrentVersions();
    for (UpdatableExternalComponent c : existing) {
      if (update.isUpdateFor(c)) {
        return c;
      }
    }
    return null;
  }
}
