package org.jetbrains.plugins.gradle.util;

import com.intellij.openapi.externalSystem.model.ProjectSystemId;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * Holds object representation of icons used at the <code>Gradle</code> plugin.
 * 
 * @author Denis Zhdanov
 * @since 8/1/11 3:10 PM
 */
public class GradleConstants {

  @NotNull @NonNls public static final ProjectSystemId SYSTEM_ID = new ProjectSystemId("GRADLE");

<<<<<<< HEAD   (ba434a Merge "Cherry-pick commits from IDEA repo.")
  @NonNls public static final String EXTENSION           = "gradle";
  @NonNls public static final String DEFAULT_SCRIPT_NAME = "build.gradle";
  @NonNls public static final String SETTINGS_FILE_NAME  = "setting.gradle";
=======
  @NotNull @NonNls public static final String EXTENSION           = "gradle";
  @NotNull @NonNls public static final String DEFAULT_SCRIPT_NAME = "build.gradle";
  @NotNull @NonNls public static final String SETTINGS_FILE_NAME  = "settings.gradle";
>>>>>>> BRANCH (6739a8 Snapshot af729d01433bb5bbd6ca93c0fdf9778b36d624ce from maste)

  @NotNull @NonNls public static final String SYSTEM_DIRECTORY_PATH_KEY = "GRADLE_USER_HOME";

  @NotNull @NonNls public static final String TOOL_WINDOW_TOOLBAR_PLACE = "GRADLE_SYNC_CHANGES_TOOLBAR";

  @NotNull @NonNls public static final String HELP_TOPIC_TOOL_WINDOW = "reference.toolwindows.gradle";

  private GradleConstants() {
  }
}
