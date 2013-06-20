/*
 * Copyright 2000-2011 JetBrains s.r.o.
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
package com.intellij.internal.statistic.connect;


import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.openapi.extensions.ExtensionPointName;
<<<<<<< HEAD   (dcdbd6 Merge "Update ADT dictionary with a few more words")
=======
import org.jetbrains.annotations.NonNls;
>>>>>>> BRANCH (0e154c Snapshot 4a019151cb9b5542ea5ba9ed2f07b29cee0951f0 from maste)
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface StatisticsService {

  ExtensionPointName<StatisticsService> EP_NAME = ExtensionPointName.create("com.intellij.statisticsService");
<<<<<<< HEAD   (dcdbd6 Merge "Update ADT dictionary with a few more words")
=======

  @NonNls String TITLE = "title";
  @NonNls String DETAILS = "details";
  @NonNls String ALLOW_CHECKBOX = "allow-checkbox";
>>>>>>> BRANCH (0e154c Snapshot 4a019151cb9b5542ea5ba9ed2f07b29cee0951f0 from maste)

  StatisticsResult send();

  Notification createNotification(@NotNull String groupDisplayId, @Nullable NotificationListener listener);

<<<<<<< HEAD   (dcdbd6 Merge "Update ADT dictionary with a few more words")
=======
  /**
   * Returns the custom text to be displayed in the statistics dialog. The keys in the map are control IDs ({@link #TITLE}, {@link #DETAILS}
   * and {@link #ALLOW_CHECKBOX} and the values are the text to be displayed in the dialog.
   *
   * @return the text override map, or null if the standard text should be used.
   */
>>>>>>> BRANCH (0e154c Snapshot 4a019151cb9b5542ea5ba9ed2f07b29cee0951f0 from maste)
  @Nullable
  Map<String, String> getStatisticsConfigurationLabels();
}
