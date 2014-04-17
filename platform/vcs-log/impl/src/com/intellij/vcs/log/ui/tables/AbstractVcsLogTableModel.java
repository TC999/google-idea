package com.intellij.vcs.log.ui.tables;

import com.intellij.openapi.util.EmptyRunnable;
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.impl.NullVirtualFile;
import com.intellij.util.text.DateFormatUtil;
import com.intellij.vcs.log.Hash;
import com.intellij.vcs.log.VcsFullCommitDetails;
import com.intellij.vcs.log.VcsShortCommitDetails;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import com.intellij.vcs.log.graph.elements.Node;
=======
import com.intellij.vcs.log.data.DataPack;
import com.intellij.vcs.log.data.LoadMoreStage;
import com.intellij.vcs.log.data.LoadingDetails;
import com.intellij.vcs.log.data.VcsLogDataHolder;
import com.intellij.vcs.log.ui.VcsLogUiImpl;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
/**
 * @param <CommitColumnClass> commit column class
 * @param <CommitId>          Commit identifier, which can be different depending on the model nature,
 *                            for example, a {@link Hash} or an {@link Integer} or a {@link Node}.
 */
public abstract class AbstractVcsLogTableModel<CommitColumnClass, CommitId> extends AbstractTableModel {
=======
public abstract class AbstractVcsLogTableModel<CommitColumnClass> extends AbstractTableModel {
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

  public static final VirtualFile FAKE_ROOT = NullVirtualFile.INSTANCE;

  public static final int ROOT_COLUMN = 0;
  public static final int COMMIT_COLUMN = 1;
  public static final int AUTHOR_COLUMN = 2;
  public static final int DATE_COLUMN = 3;
  private static final int COLUMN_COUNT = DATE_COLUMN + 1;

  private static final String[] COLUMN_NAMES = {"", "Subject", "Author", "Date"};
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
=======

  @NotNull private final VcsLogDataHolder myLogDataHolder;
  @NotNull protected final VcsLogUiImpl myUi;
  @NotNull protected final DataPack myDataPack;
  @NotNull private final LoadMoreStage myLoadMoreStage;

  @NotNull private final AtomicBoolean myLoadMoreWasRequested = new AtomicBoolean();


  protected AbstractVcsLogTableModel(@NotNull VcsLogDataHolder logDataHolder, @NotNull VcsLogUiImpl ui, @NotNull DataPack dataPack,
                                     @NotNull LoadMoreStage loadMoreStage) {
    myLogDataHolder = logDataHolder;
    myUi = ui;
    myDataPack = dataPack;
    myLoadMoreStage = loadMoreStage;
  }
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

  @Override
  public final int getColumnCount() {
    return COLUMN_COUNT;
  }

  @Nullable
  protected VcsShortCommitDetails getShortDetails(int rowIndex) {
    return myLogDataHolder.getMiniDetailsGetter().getCommitData(rowIndex, this);
  }

  @Nullable
  public VcsFullCommitDetails getFullCommitDetails(int rowIndex) {
    return myLogDataHolder.getCommitDetailsGetter().getCommitData(rowIndex, this);
  }

  /**
   * Requests the proper data provider to load more data from the log & recreate the model.
   * @param onLoaded will be called upon task completion on the EDT.
   */
  public void requestToLoadMore(@NotNull Runnable onLoaded) {
    if (myLoadMoreWasRequested.compareAndSet(false, true)     // Don't send the request to VCS twice
        && myLoadMoreStage != LoadMoreStage.ALL_REQUESTED) {  // or when everything possible is loaded
      myUi.getTable().setPaintBusy(true);
      myUi.getFilterer().requestVcs(myDataPack, myUi.getFilters(), myLoadMoreStage, onLoaded);
    }
  }

  @NotNull
  @Override
  public final Object getValueAt(int rowIndex, int columnIndex) {
    if (rowIndex >= getRowCount() - 1) {
      requestToLoadMore(EmptyRunnable.INSTANCE);
    }

    VcsShortCommitDetails data = getShortDetails(rowIndex);
    switch (columnIndex) {
      case ROOT_COLUMN:
        return getRoot(rowIndex);
      case COMMIT_COLUMN:
        return getCommitColumnCell(rowIndex, data);
      case AUTHOR_COLUMN:
        if (data == null) {
          return "";
        }
        else {
          return data.getAuthor().getName();
        }
      case DATE_COLUMN:
        if (data == null || data.getTime() < 0) {
          return "";
        }
        else {
          return DateFormatUtil.formatDateTime(data.getTime());
        }
      default:
        throw new IllegalArgumentException("columnIndex is " + columnIndex + " > " + (COLUMN_COUNT - 1));
    }
  }

  /**
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
   * Requests the proper data provider to load more data from the log & recreate the model.
   * @param onLoaded will be called upon task completion on the EDT.
   */
  public abstract void requestToLoadMore(@NotNull Runnable onLoaded);
=======
   * Returns true if not all data has been loaded, i.e. there is sense to {@link #requestToLoadMore(Runnable) request more data}.
   */
  public boolean canRequestMore() {
    return !myUi.getFilters().isEmpty() && myLoadMoreStage != LoadMoreStage.ALL_REQUESTED;
  }
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

  /**
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
   * Returns true if not all data has been loaded, i.e. there is sense to {@link #requestToLoadMore(Runnable) request more data}.
   */
  public abstract boolean canRequestMore();

  /**
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
   * Returns Changes for commits at selected rows.<br/>
   * Rows are given in the order as they appear in the table, i. e. in reverse chronological order. <br/>
   * Changes can be returned as-is, i.e. with duplicate changes for a single file.
   * @return Changes selected in all rows, or null if this data is not ready yet.
   */
  @Nullable
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  public abstract List<Change> getSelectedChanges(@NotNull List<Integer> selectedRows);
=======
  public List<Change> getSelectedChanges(@NotNull List<Integer> selectedRows) {
    List<Change> changes = new ArrayList<Change>();
    for (int row : selectedRows) {
      VcsFullCommitDetails commitData = getFullCommitDetails(row);
      if (commitData == null || commitData instanceof LoadingDetails) {
        return null;
      }
      changes.addAll(commitData.getChanges());
    }
    return changes;
  }
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

  @NotNull
  public abstract VirtualFile getRoot(int rowIndex);

  @NotNull
  protected abstract CommitColumnClass getCommitColumnCell(int index, @Nullable VcsShortCommitDetails details);

  @NotNull
  protected abstract Class<CommitColumnClass> getCommitColumnClass();

  /**
   * Returns the Hash of the commit displayed in the given row.
   * May be null if there is no commit in the row
   * (such situations may appear, for example, if graph is filtered by branch, as described in IDEA-115442).
   */
  @Nullable
  public abstract Hash getHashAtRow(int row);

  /**
   * Returns the row number containing the given commit,
   * or -1 if the requested commit is not contained in this table model (possibly because not all data has been loaded).
   */
  public abstract int getRowOfCommit(@NotNull Hash hash);

  /**
   * Returns the number of the first row which contains a commit which hash starts with the given value,
   * or -1 if no such commit was found (possibly because not all data has been loaded).
   */
  public abstract int getRowOfCommitByPartOfHash(@NotNull String hash);

  @Override
  public Class<?> getColumnClass(int column) {
    switch (column) {
      case ROOT_COLUMN:
        return VirtualFile.class;
      case COMMIT_COLUMN:
        return getCommitColumnClass();
      case AUTHOR_COLUMN:
        return String.class;
      case DATE_COLUMN:
        return String.class;
      default:
        throw new IllegalArgumentException("columnIndex is " + column + " > " + (COLUMN_COUNT - 1));
    }
  }

  @Override
  public String getColumnName(int column) {
    return COLUMN_NAMES[column];
  }

}
