<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
package com.intellij.vcs.log.graph.elements;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author erokhins
 */
public interface Node extends GraphElement {

  int getRowIndex();

  @NotNull
  NodeType getType();

  @NotNull
  List<Edge> getUpEdges();

  @NotNull
  List<Edge> getDownEdges();

  /**
   * @return if type == COMMIT_NODE - this commit.
   *         if type == END_COMMIT_NODE - parent of This Commit
   */
  int getCommitIndex();

  enum NodeType {
    COMMIT_NODE,
    END_COMMIT_NODE
  }

}
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
