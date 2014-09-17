/*
 * Copyright (C) 2014 The Android Open Source Project
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
package com.intellij.openapi.externalSystem;

import com.intellij.openapi.externalSystem.model.DataNode;
import com.intellij.openapi.externalSystem.model.ProjectSystemId;
import com.intellij.openapi.externalSystem.model.project.ProjectData;
import org.jetbrains.annotations.NotNull;

/**
 * There is a possible case that particular ide project is backed by an external project (gradle, maven etc). We sync that ide and
 * external projects from time to time.
 * <p/>
 * This interface defines contract for callback interested in external project state. It's assumed to be notified every time
 * new external project representation is available.
 *
 * @author Denis Zhdanov
 * @since 17/09/14 20:33
 */
public interface ExternalProjectRefreshedListener {

  void onExternalProjectState(@NotNull ProjectSystemId externalSystemId, @NotNull DataNode<ProjectData> projectState);
}
