<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
package com.intellij.vcs.log.ui.filter;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.vcs.log.VcsLogFilterCollection;
import org.jetbrains.annotations.NotNull;

/**
 * Graphical UI for filtering commits in the log.
 *
 * @author Kirill Likhodedov
 */
public interface VcsLogFilterUi {

  /**
   * Returns filter components which will be added to the Log toolbar.
   */
  ActionGroup getFilterActionComponents();

  /**
   * Returns the filters currently active, i.e. switched on by user.
   */
  @NotNull
  VcsLogFilterCollection getFilters();

}
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
