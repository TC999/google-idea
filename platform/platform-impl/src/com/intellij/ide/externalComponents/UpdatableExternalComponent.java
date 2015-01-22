/*
  Not sure what's needed here.
 */
package com.intellij.ide.externalComponents;

/**
 * Created by jbakermalone on 1/20/15.
 */
public interface UpdatableExternalComponent {
  Object getKey();
  boolean isUpdateFor(UpdatableExternalComponent c);
  String getName();
}
