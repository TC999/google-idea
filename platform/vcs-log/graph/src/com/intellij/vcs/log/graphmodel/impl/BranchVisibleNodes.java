<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
package com.intellij.vcs.log.graphmodel.impl;

import com.intellij.util.Function;
import com.intellij.vcs.log.graph.elements.Edge;
import com.intellij.vcs.log.graph.elements.Node;
import com.intellij.vcs.log.graph.mutable.MutableGraph;
import com.intellij.vcs.log.graph.mutable.elements.MutableNode;
import com.intellij.vcs.log.graph.mutable.elements.MutableNodeRow;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author erokhins
 */
public class BranchVisibleNodes {

  @NotNull private final MutableGraph myGraph;
  @NotNull private Set<Node> myVisibleNodes = Collections.emptySet();

  public BranchVisibleNodes(@NotNull MutableGraph graph) {
    myGraph = graph;
  }

  @NotNull
  public Set<Node> generateVisibleBranchesNodes(@NotNull Function<Node, Boolean> isStartedNode) {
    Set<Node> visibleNodes = new HashSet<Node>();
    for (MutableNodeRow row : myGraph.getAllRows()) {
      for (MutableNode node : row.getInnerNodeList()) {
        if (isStartedNode.fun(node)) {
          visibleNodes.add(node);
        }
        if (isStartedNode.fun(node) || visibleNodes.contains(node)) {
          for (Edge edge : node.getInnerDownEdges()) {
            visibleNodes.add(edge.getDownNode());
          }
        }
      }
    }
    return visibleNodes;
  }

  public void setVisibleNodes(@NotNull Set<Node> visibleNodes) {
    myVisibleNodes = visibleNodes;
  }

  public Set<Node> getVisibleNodes() {
    return Collections.unmodifiableSet(myVisibleNodes);
  }

  public boolean isVisibleNode(@NotNull Node node) {
    return myVisibleNodes.contains(node);
  }
}
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
