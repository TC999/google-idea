/*
 * Copyright 2000-2013 JetBrains s.r.o.
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

package org.zmlx.hg4idea.repo;

import com.intellij.dvcs.repo.Repository;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;


public interface HgRepository extends Repository {
  @NotNull String DEFAULT_BRANCH = "default";

  @NotNull
  VirtualFile getHgDir();

  /**
   * Returns the current branch of this Hg repository.
   */

  @NotNull
  String getCurrentBranch();

  @NotNull
  Collection<String> getBranches();

  @NotNull
  Collection<String> getBookmarks();

  @Nullable
  String getCurrentBookmark();
<<<<<<< HEAD   (39f68d Merge "Revert "Snapshot d8891a7de15cebb78b6ce5711e50e531b42c)
=======

  @NotNull
  HgConfig getRepositoryConfig();

  void updateConfig();
>>>>>>> BRANCH (c1ace1 Snapshot aea001abfc1b38fec3a821bcd5174cc77dc75787 from maste)
}
