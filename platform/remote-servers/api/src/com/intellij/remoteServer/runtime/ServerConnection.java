package com.intellij.remoteServer.runtime;

import com.intellij.openapi.ui.ComponentContainer;
import com.intellij.remoteServer.configuration.RemoteServer;
import com.intellij.remoteServer.configuration.deployment.DeploymentConfiguration;
import com.intellij.remoteServer.runtime.deployment.DeploymentRuntime;
import com.intellij.remoteServer.runtime.deployment.DeploymentTask;
import com.intellij.util.ParameterizedRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * @author nik
 */
public interface ServerConnection<D extends DeploymentConfiguration> {
  @NotNull
  RemoteServer<?> getServer();

  @NotNull
  ConnectionStatus getStatus();

  @NotNull
  String getStatusText();


  void connect(@NotNull Runnable onFinished);


<<<<<<< HEAD   (d67919 Merge "Revert "Revert "Replaced Gradle 1.8 with Gradle 1.7.")
=======
  void disconnect();

>>>>>>> BRANCH (a3c369 Snapshot 13baaa319cd568c4e19b9232b24f2002f2631688 from maste)
  void deploy(@NotNull DeploymentTask<D> task, @NotNull ParameterizedRunnable<String> onDeploymentStarted);

  void computeDeployments(@NotNull Runnable onFinished);

  void undeploy(@NotNull Deployment deployment, @NotNull DeploymentRuntime runtime);

  @NotNull
  Collection<Deployment> getDeployments();

  @Nullable
  ComponentContainer getLogConsole(@NotNull Deployment deployment);
}
