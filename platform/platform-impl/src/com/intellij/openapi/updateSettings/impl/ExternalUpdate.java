/*
  Not sure what's needed here.
 */
package com.intellij.openapi.updateSettings.impl;

import com.intellij.ide.externalComponents.ExternalComponentSource;
import com.intellij.ide.externalComponents.UpdatableExternalComponent;

import java.util.Collection;

public class ExternalUpdate {
  private final Collection<UpdatableExternalComponent> myComponents;
  private final ExternalComponentSource mySource;

  public ExternalUpdate(Collection<UpdatableExternalComponent> c, ExternalComponentSource s) {
    myComponents = c;
    mySource = s;
  }

  public Collection<UpdatableExternalComponent> getComponents() {
    return myComponents;
  }

  public ExternalComponentSource getSource() {
    return mySource;
  }
}
