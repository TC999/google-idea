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
package com.intellij.refactoring.util;

import com.intellij.JavaTestUtil;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.PsiTestCase;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class ClsElementWritingAccessProviderTest extends PsiTestCase {
  public void test1() throws IOException {
    ClsElementWritingAccessProvider provider = new ClsElementWritingAccessProvider();

    // get some .jar from the dependencies, this should return a jar from the JDK
    VirtualFile jar = getExternalJarFile();
    assertNotNull(jar);
    assertFalse("the test jar should've been outside the project folder", VfsUtilCore.isAncestor(myProject.getBaseDir(), jar, false));

    // should not be able to write to an external dependency .jar file
    assertFalse("should not be allowed to over-write a .jar file that is a dependency", provider.canAllowWrites(jar, myProject));

    // should not be able to write a file within an external dependency .jar file
    VirtualFile jarRootForLocalFile = JarFileSystem.getInstance().getJarRootForLocalFile(jar);
    assertNotNull(jarRootForLocalFile);

    // test that files inside the .jar cannot be deleted
    String[] testPaths = new String[] { "META-INF", "META-INF/MANIFEST.MF"};
    for (String path : testPaths) {
      VirtualFile manifestFile = jarRootForLocalFile.findFileByRelativePath(path);
      assertNotNull("test jar expected to have file " + path, manifestFile);
      assertFalse("should not be allowed to write to a file within a jar", provider.canAllowWrites(manifestFile, myProject));
    }
  }

  // TODO: this could be environment sensitive as it just returns the first .jar that it finds
  // It should probably return a specific jar that contains certain files that are expected by the caller.
  @Nullable
  private VirtualFile getExternalJarFile() {
    for (VirtualFile root : ModuleRootManager.getInstance(myModule).orderEntries().classes().getRoots()) {
      if (root.getFileSystem() instanceof JarFileSystem) {
        VirtualFile virtualFileForJar = JarFileSystem.getInstance().getVirtualFileForJar(root);
        if (virtualFileForJar != null) {
          return virtualFileForJar;
        }
      }
    }

    return null;
  }

  @Override
  protected Sdk getTestProjectJdk() {
    return JavaTestUtil.getTestJdk();
  }
}
