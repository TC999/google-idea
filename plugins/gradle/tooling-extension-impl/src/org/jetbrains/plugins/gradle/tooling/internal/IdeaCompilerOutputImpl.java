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
package org.jetbrains.plugins.gradle.tooling.internal;

import org.jetbrains.plugins.gradle.model.ExtIdeaCompilerOutput;

import java.io.File;

public class IdeaCompilerOutputImpl implements ExtIdeaCompilerOutput {
  private File outputDir;
  private File testOutputDir;
  private File resourcesDir;
  private File testResourcesDir;

  @Override
  public boolean getInheritOutputDirs() {
    return false;
  }

  @Override
  public File getOutputDir() {
    return outputDir;
  }

  @Override
  public File getTestOutputDir() {
    return testOutputDir;
  }

  @Override
  public File getResourcesDir() {
    return resourcesDir;
  }

  @Override
  public File getTestResourcesDir() {
    return testResourcesDir;
  }

  public void setOutputDir(File outputDir) {
    this.outputDir = outputDir;
  }

  public void setTestOutputDir(File testOutputDir) {
    this.testOutputDir = testOutputDir;
  }

  public void setResourcesDir(File resourcesDir) {
    this.resourcesDir = resourcesDir;
  }

  public void setTestResourcesDir(File testResourcesDir) {
    this.testResourcesDir = testResourcesDir;
  }
}
