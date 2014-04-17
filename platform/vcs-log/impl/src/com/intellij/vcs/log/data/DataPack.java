package com.intellij.vcs.log.data;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.NotNullFunction;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.vcs.log.*;
import com.intellij.vcs.log.graph.GraphColorManagerImpl;
import com.intellij.vcs.log.graph.GraphFacade;
import com.intellij.vcs.log.newgraph.facade.GraphFacadeImpl;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DataPack implements VcsLogDataPack {

  @NotNull private final RefsModel myRefsModel;
  @NotNull private final GraphFacade myGraphFacade;
  @NotNull private final Map<VirtualFile, VcsLogProvider> myLogProviders;

  @NotNull
  public static DataPack build(@NotNull List<? extends GraphCommit> commits,
                               @NotNull Collection<VcsRef> allRefs,
                               @NotNull ProgressIndicator indicator,
                               @NotNull NotNullFunction<Hash, Integer> indexGetter,
                               @NotNull NotNullFunction<Integer, Hash> hashGetter, 
                               @NotNull Map<VirtualFile, VcsLogProvider> providers) {
    indicator.setText("Building graph...");
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")

    MutableGraph graph = GraphBuilder.build(commits, allRefs);

    GraphModel graphModel = new GraphModelImpl(graph);

    final GraphPrintCellModel printCellModel = new GraphPrintCellModelImpl(graphModel.getGraph());
    graphModel.addUpdateListener(new Consumer<UpdateRequest>() {
      @Override
      public void consume(UpdateRequest key) {
        printCellModel.recalculate(key);
      }
    });

    final RefsModel refsModel = new RefsModel(allRefs, indexGetter);
    graphModel.getFragmentManager().setUnconcealedNodeFunction(new Function<Node, Boolean>() {
      @NotNull
      @Override
      public Boolean fun(@NotNull Node key) {
        if (key.getDownEdges().isEmpty() || key.getUpEdges().isEmpty() || refsModel.isBranchRef(key.getCommitIndex())) {
          return true;
        }
        else {
          return false;
        }
      }
    });
    return new DataPack(graphModel, refsModel, printCellModel, hashGetter, indexGetter);
=======
    final RefsModel refsModel = new RefsModel(allRefs, indexGetter);
    GraphColorManagerImpl colorManager = new GraphColorManagerImpl(refsModel, hashGetter, getRefManagerMap(providers));
    if (!commits.isEmpty()) {
      return new DataPack(refsModel, GraphFacadeImpl.newInstance(commits, getBranchCommitHashIndexes(allRefs, indexGetter), colorManager),
                          providers);
    }
    else {
      return new DataPack(refsModel, new EmptyGraphFacade(), providers);
    }
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

  @NotNull
  private static Set<Integer> getBranchCommitHashIndexes(@NotNull Collection<VcsRef> allRefs,
                                                         @NotNull NotNullFunction<Hash, Integer> indexGetter) {
    Set<Integer> result = new HashSet<Integer>();
    for (VcsRef vcsRef : allRefs) {
      if (vcsRef.getType().isBranch())
        result.add(indexGetter.fun(vcsRef.getCommitHash()));
    }
    return result;
  }

  @NotNull
  private static Map<VirtualFile, VcsLogRefManager> getRefManagerMap(@NotNull Map<VirtualFile, VcsLogProvider> logProviders) {
    Map<VirtualFile, VcsLogRefManager> map = ContainerUtil.newHashMap();
    for (Map.Entry<VirtualFile, VcsLogProvider> entry : logProviders.entrySet()) {
      map.put(entry.getKey(), entry.getValue().getReferenceManager());
    }
    return map;
  }

  private DataPack(@NotNull RefsModel refsModel, @NotNull GraphFacade graphFacade, @NotNull Map<VirtualFile, VcsLogProvider> providers) {
    myRefsModel = refsModel;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    myPrintCellModel = printCellModel;
    myHashGetter = hashGetter;
    myIndexGetter = indexGetter;
=======
    myGraphFacade = graphFacade;
    myLogProviders = providers;
  }

  @NotNull
  public GraphFacade getGraphFacade() {
    return myGraphFacade;
  }

  @NotNull
  @Override
  public VcsLogRefs getRefs() {
    return myRefsModel;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

  @NotNull
  public RefsModel getRefsModel() {
    return myRefsModel;
  }

  @NotNull
  @Override
  public Map<VirtualFile, VcsLogProvider> getLogProviders() {
    return myLogProviders;
  }

}
