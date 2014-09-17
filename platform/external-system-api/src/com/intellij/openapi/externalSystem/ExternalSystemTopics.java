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

import com.intellij.util.messages.Topic;

/**
 * @author Denis Zhdanov
 * @since 17/09/14 20:32
 */
public class ExternalSystemTopics {

  /** Project-level. */
  public static final Topic<ExternalProjectRefreshedListener> EXTERNAL_PROJECT
    = new Topic<ExternalProjectRefreshedListener>("external project state", ExternalProjectRefreshedListener.class);

  private ExternalSystemTopics() {
  }
}
