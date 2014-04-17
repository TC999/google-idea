package com.intellij.vcs.log;

import org.jetbrains.annotations.NotNull;

/**
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
 * Filter which needs {@link VcsFullCommitDetails} to work.
 *
 * @see VcsLogGraphFilter
 */
public interface VcsLogDetailsFilter extends VcsLogFilter {

  boolean matches(@NotNull VcsFullCommitDetails details);
=======
 * Filter which needs {@link VcsCommitMetadata} to work.
 *
 * @see VcsLogGraphFilter
 */
public interface VcsLogDetailsFilter extends VcsLogFilter {

  boolean matches(@NotNull VcsCommitMetadata details);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

}
