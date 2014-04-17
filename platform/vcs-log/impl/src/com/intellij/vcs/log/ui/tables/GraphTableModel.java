package com.intellij.vcs.log.ui.tables;

import com.intellij.openapi.diagnostic.Logger;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import com.intellij.openapi.vcs.changes.Change;
=======
import com.intellij.openapi.util.Condition;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.vcs.log.Hash;
import com.intellij.vcs.log.VcsRef;
import com.intellij.vcs.log.VcsShortCommitDetails;
import com.intellij.vcs.log.data.DataPack;
import com.intellij.vcs.log.data.LoadMoreStage;
import com.intellij.vcs.log.data.VcsLogDataHolder;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import com.intellij.vcs.log.graph.elements.Node;
import com.intellij.vcs.log.graph.render.GraphCommitCell;
import com.intellij.vcs.log.printmodel.GraphPrintCell;
import com.intellij.vcs.log.ui.VcsLogUI;
=======
import com.intellij.vcs.log.ui.render.GraphCommitCell;
import com.intellij.vcs.log.impl.VcsLogUtil;
import com.intellij.vcs.log.ui.VcsLogUiImpl;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import java.util.ArrayList;
=======
import java.util.Collection;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
import java.util.Collections;
import java.util.List;

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
/**
 * @author Kirill Likhodedov
 */
public class GraphTableModel extends AbstractVcsLogTableModel<GraphCommitCell, Node> {
=======
public class GraphTableModel extends AbstractVcsLogTableModel<GraphCommitCell> {
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

  private static final Logger LOG = Logger.getInstance(GraphTableModel.class);

  @NotNull private final DataPack myDataPack;
  @NotNull private final VcsLogDataHolder myDataHolder;

  public GraphTableModel(@NotNull DataPack dataPack, @NotNull VcsLogDataHolder dataHolder, @NotNull VcsLogUiImpl UI,
                         @NotNull LoadMoreStage loadMoreStage) {
    super(dataHolder, UI, dataPack, loadMoreStage);
    myDataPack = dataPack;
    myDataHolder = dataHolder;
  }

  @Override
  public int getRowCount() {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    return myDataPack.getGraphModel().getGraph().getNodeRows().size();
  }

  @Nullable
  @Override
  protected VcsShortCommitDetails getShortDetails(int rowIndex) {
    return myDataHolder.getMiniDetailsGetter().getCommitData(rowIndex, this);
  }

  @Nullable
  @Override
  public VcsFullCommitDetails getFullCommitDetails(int row) {
    return myDataHolder.getCommitDetailsGetter().getCommitData(row, this);
=======
    return myDataPack.getGraphFacade().getVisibleCommitCount();
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

  @Override
  public void requestToLoadMore(@NotNull Runnable onLoaded) {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    myDataHolder.showFullLog(onLoaded);
  }

  @Override
  public boolean canRequestMore() {
    return !myDataHolder.isFullLogShowing();
  }

  @Nullable
  @Override
  public List<Change> getSelectedChanges(@NotNull List<Integer> selectedRows) {
    List<Change> changes = new ArrayList<Change>();
    for (int row : selectedRows) {
      VcsFullCommitDetails commitData = myDataHolder.getCommitDetailsGetter().getCommitData(row, this);
      if (commitData == null || commitData instanceof LoadingDetails) {
        return null;
      }
      changes.addAll(commitData.getChanges());
=======
    if (!myDataHolder.isFullLogShowing()) {
      myDataHolder.showFullLog(onLoaded);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    }
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    return changes;
  }

  @Nullable
  private GraphPrintCell getGraphPrintCellForRow(int row) {
    Object commitValue = getValueAt(row, AbstractVcsLogTableModel.COMMIT_COLUMN);
    if (commitValue instanceof GraphCommitCell) {
      GraphCommitCell commitCell = (GraphCommitCell)commitValue;
      return commitCell.getPrintCell();
    }
    return null;
=======
    else if (!myUi.getFilters().isEmpty()) {
      super.requestToLoadMore(onLoaded);
    }
  }

  @Override
  public boolean canRequestMore() {
    return !myDataHolder.isFullLogShowing() || super.canRequestMore();
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

  @NotNull
  @Override
  public VirtualFile getRoot(int rowIndex) {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    Node commitNode = myDataPack.getGraphModel().getGraph().getCommitNodeInRow(rowIndex);
    return commitNode != null ? commitNode.getBranch().getRepositoryRoot() : FAKE_ROOT;
=======
    int head = myDataPack.getGraphFacade().getInfoProvider().getRowInfo(rowIndex).getOneOfHeads();
    Collection<VcsRef> refs = myDataPack.getRefsModel().refsToCommit(head);
    if (refs.isEmpty()) {
      LOG.error("No references pointing to head " + head + " identified for commit at row " + rowIndex);
      return FAKE_ROOT;
    }
    return refs.iterator().next().getRoot();
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

  @NotNull
  @Override
  protected GraphCommitCell getCommitColumnCell(int rowIndex, @Nullable VcsShortCommitDetails details) {
    String message = "";
    List<VcsRef> refs = Collections.emptyList();
    if (details != null) {
      message = details.getSubject();
      refs = (List<VcsRef>)myDataPack.getRefsModel().refsToCommit(details.getHash());
    }
    return new GraphCommitCell(message, refs);
  }

  @NotNull
  @Override
  protected Class<GraphCommitCell> getCommitColumnClass() {
    return GraphCommitCell.class;
  }

  @Nullable
  @Override
  public Hash getHashAtRow(int row) {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    Node node = myDataPack.getGraphModel().getGraph().getCommitNodeInRow(row);
    return node == null ? null : myDataHolder.getHash(node.getCommitIndex());
=======
    return myDataHolder.getHash(myDataPack.getGraphFacade().getCommitAtRow(row));
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

  @Override
  public int getRowOfCommit(@NotNull final Hash hash) {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    return myDataPack.getRowByHash(hash);
  }

  @Override
  public int getRowOfCommitByPartOfHash(@NotNull String hash) {
    Node node = myDataPack.getNodeByPartOfHash(hash);
    return node != null ? node.getRowIndex() : -1;
=======
    final int commitIndex = myDataHolder.getCommitIndex(hash);
    return ContainerUtil.indexOf(VcsLogUtil.getVisibleCommits(myDataPack.getGraphFacade()), new Condition<Integer>() {
      @Override
      public boolean value(Integer integer) {
        return integer == commitIndex;
      }
    });
  }

  @Override
  public int getRowOfCommitByPartOfHash(@NotNull String partialHash) {
    Hash hash = myDataHolder.findHashByString(partialHash);
    return hash != null ? getRowOfCommit(hash) : -1;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

}
