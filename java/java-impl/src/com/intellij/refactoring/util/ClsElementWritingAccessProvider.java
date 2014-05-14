/*
 * Copyright 2000-2012 JetBrains s.r.o.
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
package com.intellij.refactoring.util;

import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.WritingAccessProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * User: ksafonov
 */
public class ClsElementWritingAccessProvider extends WritingAccessProvider {

  @NotNull
  @Override
  public Collection<VirtualFile> requestWriting(final VirtualFile... files) {
    return Collections.emptyList();
  }

  @Override
  public boolean isPotentiallyWritable(@NotNull final VirtualFile file) {
    return true;
  }

  @Override
  protected boolean canAllowWrites(@NotNull VirtualFile file, @Nullable Project project) {
    if (file.getFileType() == JavaClassFileType.INSTANCE) {
      return false;
    }

    // Don't allow writing to files that are part of "external libraries". See https://code.google.com/p/android/issues/detail?id=67161
    // The following code disallows writes to all files within a .jar, and to the .jar file itself if it is present outside the
    // project root and is a dependency of this project.

    if (project != null && !VfsUtilCore.isAncestor(project.getBaseDir(), file, true)) {
      // check if it is part of a library dependency
      for (Module module : ModuleManager.getInstance(project).getModules()) {
        for (VirtualFile root : ModuleRootManager.getInstance(module).orderEntries().getAllLibrariesAndSdkClassesRoots()) {
          // if the file is within a library root, then it isn't writeable
          if (VfsUtilCore.isAncestor(root, file, false)) {
            return false;
          }

          // if the file points to a .jar file, then compare with the .jar file corresponding to the root
          if (root.getFileSystem() instanceof JarFileSystem) {
            VirtualFile virtualFileForJar = JarFileSystem.getInstance().getVirtualFileForJar(root);
            if (virtualFileForJar != null && VfsUtilCore.isAncestor(virtualFileForJar, file, false)) {
              return false;
            }
          }

        }
      }
    }

    return true;
  }
}
