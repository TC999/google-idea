/*
  Not sure what's needed here.
 */
package com.intellij.ide.externalComponents;

import com.intellij.openapi.progress.ProgressIndicator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Interface for classes that can provide information on and updates for installed components.
 */
public interface ExternalComponentSource {
  /**
   * Retrieve information on the updates that this source can provide.
   * @param indicator A {@code ProgressIndicator} that can be updated to show progress, or can be used to cancel the process.
   * @return A Collection of {@code UpdatableExternalComponents} representing the available updates.
   */
  @NotNull
  Collection<UpdatableExternalComponent> getAvailableVersions(@Nullable ProgressIndicator indicator);

  /**
   * Retrieve information on currently installed components.
   * @return A Collection of currently installed {@code UpdatableExternalComponents}.
   */
  @NotNull
  Collection<UpdatableExternalComponent> getCurrentVersions();

  /**
   * Install updates for the given {@code UpdatableExternalComponents}.
   * @param request
   */
  void installUpdates(@NotNull Collection<UpdatableExternalComponent> request);

  /**
   * Gets a human-readable name for this source.
   * @return The name.
   */
  @NotNull
  String getName();
}
