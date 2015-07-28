/*
 * Copyright 2000-2014 JetBrains s.r.o.
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
package com.intellij.idea;

import com.intellij.ide.plugins.PluginManager;
import com.intellij.internal.statistic.analytics.PlatformUsageTracker;
import com.intellij.openapi.application.ConfigImportHelper;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.util.PlatformUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileFilter;

@SuppressWarnings({"UnusedDeclaration"})
public class MainImpl {
  private MainImpl() { }

  /**
   * Called from PluginManager via reflection.
   */
  protected static void start(final String[] args) {
    System.setProperty(PlatformUtils.PLATFORM_PREFIX_KEY, PlatformUtils.getPlatformPrefix(PlatformUtils.IDEA_CE_PREFIX));

    reportPreviousCrashes();

    StartupUtil.prepareAndStart(args, new StartupUtil.AppStarter() {
      @Override
      public void start(final boolean newConfigFolder) {
        //noinspection SSBasedInspection
        SwingUtilities.invokeLater(new Runnable() {
          @Override
          public void run() {
            PluginManager.installExceptionHandler();

            if (newConfigFolder && !Boolean.getBoolean(ConfigImportHelper.CONFIG_IMPORTED_IN_CURRENT_SESSION_KEY)) {
              StartupUtil.runStartupWizard();
            }

            final IdeaApplication app = new IdeaApplication(args);
            //noinspection SSBasedInspection
            SwingUtilities.invokeLater(new Runnable() {
              @Override
              public void run() {
                app.run();
              }
            });
          }
        });
      }
    });
  }

  private static void reportPreviousCrashes() {
    if (!"AndroidStudio".equals(System.getProperty(PlatformUtils.PLATFORM_PREFIX_KEY))) {
      return;
    }

    File[] previousRecords = new File(PathManager.getTempPath()).listFiles(new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return pathname.getName().startsWith("AndroidStudio.");
      }
    });
    for (File record : previousRecords) {
      PlatformUsageTracker.trackException(new Throwable(), true);
      FileUtil.delete(record);
    }
  }
}
