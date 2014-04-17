package com.intellij.vcs.log.data;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
=======
import com.intellij.openapi.progress.ProcessCanceledException;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.EmptyRunnable;
import com.intellij.openapi.util.Ref;
import com.intellij.util.Consumer;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.HashSet;
import com.intellij.util.ui.UIUtil;
import com.intellij.vcs.log.*;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import com.intellij.vcs.log.graph.elements.Node;
import com.intellij.vcs.log.graphmodel.GraphModel;
import com.intellij.vcs.log.ui.VcsLogUI;
=======
import com.intellij.vcs.log.graph.GraphFacade;
import com.intellij.vcs.log.impl.VcsLogUtil;
import com.intellij.vcs.log.ui.VcsLogUiImpl;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
import com.intellij.vcs.log.ui.tables.AbstractVcsLogTableModel;
import com.intellij.vcs.log.ui.tables.EmptyTableModel;
import com.intellij.vcs.log.ui.tables.GraphTableModel;
import gnu.trove.TIntHashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class VcsLogFilterer {

  private static final Logger LOG = Logger.getInstance(VcsLogFilterer.class);

  private static final int LOAD_MORE_COMMITS_FIRST_STEP_LIMIT = 200;

  private static final Logger LOG = Logger.getInstance(VcsLogFilterer.class);

  private static final int LOAD_MORE_COMMITS_FIRST_STEP_LIMIT = 200;

  @NotNull private final VcsLogDataHolder myLogDataHolder;
  @NotNull private final VcsLogUiImpl myUI;

  public VcsLogFilterer(@NotNull VcsLogDataHolder logDataHolder, @NotNull VcsLogUiImpl ui) {
    myLogDataHolder = logDataHolder;
    myUI = ui;
  }

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  public void applyFiltersAndUpdateUi(@NotNull VcsLogFilterCollection filters) {
    DataPack dataPack = myLogDataHolder.getDataPack();
    final GraphModel graphModel = dataPack.getGraphModel();
    List<VcsLogGraphFilter> graphFilters = filters.getGraphFilters();
    List<VcsLogDetailsFilter> detailsFilters = filters.getDetailsFilters();

    // it is important to apply graph filters first:
    // if we apply other filters first, we loose the graph and won't be able to apple graph filters in that case
    // (e.g. won't be able to find out if a commit belongs to the branch selected by user).

    // hide invisible nodes from the graph
    if (!graphFilters.isEmpty()) {
      applyGraphFilters(graphModel, graphFilters);
    }
    else {
      myUI.getTable().executeWithoutRepaint(new Runnable() {
        @Override
        public void run() {
          graphModel.setVisibleBranchesNodes(ALL_NODES_VISIBLE);
        }
      });
    }

    // apply details filters, and use simple table without graph (we can't filter by details and keep the graph yet).
    final AbstractVcsLogTableModel model;
    if (!detailsFilters.isEmpty()) {
      List<VcsFullCommitDetails> filteredCommits = filterByDetails(graphModel, detailsFilters);
      model = new NoGraphTableModel(myUI, filteredCommits, dataPack.getRefsModel(), LoadMoreStage.INITIAL);
    }
    else {
      model = new GraphTableModel(myLogDataHolder, myUI);
    }

    updateUi(model);
=======
  @NotNull
  public AbstractVcsLogTableModel applyFiltersAndUpdateUi(@NotNull DataPack dataPack, @NotNull VcsLogFilterCollection filters) {
    resetFilters(dataPack);
    List<VcsLogDetailsFilter> detailsFilters = filters.getDetailsFilters();
    applyGraphFilters(dataPack, filters.getBranchFilter());
    return applyDetailsFilter(dataPack, detailsFilters);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

  private static void resetFilters(@NotNull DataPack dataPack) {
    GraphFacade facade = dataPack.getGraphFacade();
    facade.setVisibleBranches(null);
    facade.setFilter(null);
  }

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
        if (model.getRowCount() == 0) {
          model.requestToLoadMore(EmptyRunnable.INSTANCE);
        }
=======
  private AbstractVcsLogTableModel applyDetailsFilter(DataPack dataPack, List<VcsLogDetailsFilter> detailsFilters) {
    if (!detailsFilters.isEmpty()) {
      List<Hash> filteredCommits = filterInMemory(dataPack, detailsFilters);
      if (filteredCommits.isEmpty()) {
        return new EmptyTableModel(dataPack, myLogDataHolder, myUI, LoadMoreStage.INITIAL);
      }
      else{
        Condition<Integer> filter = getFilterFromCommits(filteredCommits);
        dataPack.getGraphFacade().setFilter(filter);
      }
    }
    else {
      dataPack.getGraphFacade().setFilter(null);
    }
    return new GraphTableModel(dataPack, myLogDataHolder, myUI, LoadMoreStage.INITIAL);
  }

  private Condition<Integer> getFilterFromCommits(List<Hash> filteredCommits) {
    final Set<Integer> commitSet = ContainerUtil.map2Set(filteredCommits, new Function<Hash, Integer>() {
      @Override
      public Integer fun(Hash hash) {
        return myLogDataHolder.getCommitIndex(hash);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
      }
    });
    return new Condition<Integer>() {
      @Override
      public boolean value(Integer integer) {
        return commitSet.contains(integer);
      }
    };
  }

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  public void requestVcs(@NotNull VcsLogFilterCollection filters, final LoadMoreStage loadMoreStage, @NotNull final Runnable onSuccess) {
    ApplicationManager.getApplication().assertIsDispatchThread();
    int maxCount = loadMoreStage == LoadMoreStage.INITIAL ? LOAD_MORE_COMMITS_FIRST_STEP_LIMIT : -1;
    myLogDataHolder.getFilteredDetailsFromTheVcs(filters, new Consumer<List<VcsFullCommitDetails>>() {
=======
  public void requestVcs(@NotNull final DataPack dataPack, @NotNull VcsLogFilterCollection filters,
                         @NotNull final LoadMoreStage loadMoreStage, @NotNull final Runnable onSuccess) {
    ApplicationManager.getApplication().assertIsDispatchThread();
    int maxCount = loadMoreStage == LoadMoreStage.INITIAL ? LOAD_MORE_COMMITS_FIRST_STEP_LIMIT : -1;
    myLogDataHolder.getFilteredDetailsFromTheVcs(filters, new Consumer<List<Hash>>() {
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
      @Override
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
      public void consume(List<VcsFullCommitDetails> details) {
        LoadMoreStage newLoadMoreStage = advanceLoadMoreStage(loadMoreStage);
        myUI.setModel(new NoGraphTableModel(myUI, details, myLogDataHolder.getDataPack().getRefsModel(), newLoadMoreStage));
        myUI.updateUI();
=======
      public void consume(List<Hash> hashes) {
        LoadMoreStage newLoadMoreStage = advanceLoadMoreStage(loadMoreStage);
        TIntHashSet previouslySelected = myUI.getSelectedCommits();
        AbstractVcsLogTableModel model;
        if (hashes.isEmpty()) {
          model = new EmptyTableModel(dataPack, myLogDataHolder, myUI, newLoadMoreStage);
        }
        else {
          dataPack.getGraphFacade().setFilter(getFilterFromCommits(hashes));
          model = new GraphTableModel(dataPack, myLogDataHolder, myUI, newLoadMoreStage);
        }
        myUI.setModel(model, dataPack, previouslySelected);
        myUI.repaintUI();
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
        onSuccess.run();
      }
    }, maxCount);
  }

  @NotNull
  private static LoadMoreStage advanceLoadMoreStage(@NotNull LoadMoreStage loadMoreStage) {
    LoadMoreStage newLoadMoreStage;
    if (loadMoreStage == LoadMoreStage.INITIAL) {
      newLoadMoreStage = LoadMoreStage.LOADED_MORE;
    }
    else if (loadMoreStage == LoadMoreStage.LOADED_MORE) {
      newLoadMoreStage = LoadMoreStage.ALL_REQUESTED;
    }
    else {
      LOG.warn("Incorrect previous load more stage: " + loadMoreStage);
      newLoadMoreStage = LoadMoreStage.ALL_REQUESTED;
    }
    return newLoadMoreStage;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
=======
  }

  private void applyGraphFilters(@NotNull final DataPack dataPack, @Nullable final VcsLogBranchFilter branchFilter) {
    try {
      dataPack.getGraphFacade().setVisibleBranches(branchFilter != null ? getMatchingHeads(dataPack, branchFilter) : null);
    }
    catch (InvalidRequestException e) {
      if (!myLogDataHolder.isFullLogShowing()) {
        myLogDataHolder.showFullLog(EmptyRunnable.getInstance());
        throw new ProcessCanceledException();
      }
      else {
        throw e;
      }
    }
  }

  @NotNull
  private Collection<Integer> getMatchingHeads(@NotNull DataPack dataPack, @NotNull VcsLogBranchFilter branchFilter) {
    final Collection<String> branchNames = new HashSet<String>(branchFilter.getBranchNames());
    return ContainerUtil.mapNotNull(dataPack.getRefsModel().getAllRefs(), new Function<VcsRef, Integer>() {
      @Override
      public Integer fun(VcsRef ref) {
        if (branchNames.contains(ref.getName())) {
          return myLogDataHolder.getCommitIndex(ref.getCommitHash());
        }
        return null;
      }
    });
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

  @NotNull
  private List<Hash> filterInMemory(@NotNull DataPack dataPack, @NotNull List<VcsLogDetailsFilter> detailsFilters) {
    List<Hash> result = ContainerUtil.newArrayList();
    for (int visibleCommit : VcsLogUtil.getVisibleCommits(dataPack.getGraphFacade())) {
      VcsCommitMetadata data = getDetailsFromCache(visibleCommit);
      if (data == null) {
        // no more continuous details in the cache
        break;
      }
      if (matchesAllFilters(data, detailsFilters)) {
        result.add(data.getHash());
      }
    }
    return result;
  }

  private static boolean matchesAllFilters(@NotNull final VcsCommitMetadata commit, @NotNull List<VcsLogDetailsFilter> detailsFilters) {
    return !ContainerUtil.exists(detailsFilters, new Condition<VcsLogDetailsFilter>() {
      @Override
      public boolean value(VcsLogDetailsFilter filter) {
        return !filter.matches(commit);
      }
    });
  }

  @Nullable
  private VcsCommitMetadata getDetailsFromCache(final int commitIndex) {
    final Hash hash = myLogDataHolder.getHash(commitIndex);
    VcsCommitMetadata details = myLogDataHolder.getTopCommitDetails(hash);
    if (details != null) {
      return details;
    }
    final Ref<VcsCommitMetadata> ref = Ref.create();
    UIUtil.invokeAndWaitIfNeeded(new Runnable() {
      @Override
      public void run() {
        ref.set(myLogDataHolder.getCommitDetailsGetter().getCommitDataIfAvailable(hash));
      }
    });
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  }

  @NotNull
  private List<VcsFullCommitDetails> filterByDetails(@NotNull GraphModel graphModel, @NotNull List<VcsLogDetailsFilter> detailsFilters) {
    List<VcsFullCommitDetails> result = ContainerUtil.newArrayList();
    int topCommits = myLogDataHolder.getSettings().getRecentCommitsCount();
    for (int i = 0; i < topCommits && i < graphModel.getGraph().getNodeRows().size(); i++) {
      Node node = graphModel.getGraph().getCommitNodeInRow(i);
      if (node == null) {
        // there can be nodes which contain no commits (IDEA-115442, branch filter case)
        continue;
      }
      final VcsFullCommitDetails details = getDetailsFromCache(node);
      if (details == null) {
        // Details for recent commits should be available in the cache.
        // However if they are not there for some reason, we stop filtering.
        // If we continue, if this commit without details matches filters,
        // if details of an older commit are found in the cache, and if this older commit matches the filter,
        // then we will return the list which incorrectly misses some matching commit in the middle.
        // => Instead we rather will return a smaller list: this is not a problem,
        // because the VCS will be requested for filtered details if there are not enough of them.
        LOG.debug("No details found for a recent commit " + myLogDataHolder.getHash(node.getCommitIndex()));
        break;
      }
      boolean allFiltersMatch = !ContainerUtil.exists(detailsFilters, new Condition<VcsLogDetailsFilter>() {
        @Override
        public boolean value(VcsLogDetailsFilter filter) {
          return !filter.matches(details);
        }
      });
      if (allFiltersMatch) {
        result.add(details);
      }
    }
    return result;
  }

  @Nullable
  private VcsFullCommitDetails getDetailsFromCache(@NotNull final Node node) {
    final Ref<VcsFullCommitDetails> ref = Ref.create();
    UIUtil.invokeAndWaitIfNeeded(new Runnable() {
      @Override
      public void run() {
        ref.set(myLogDataHolder.getCommitDetailsGetter().getCommitDataIfAvailable(myLogDataHolder.getHash(node.getCommitIndex())));
      }
    });
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    return ref.get();
  }

}
