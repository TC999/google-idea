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
package com.intellij.vcs.log.ui.render;

import com.intellij.vcs.log.data.VcsLogDataHolder;
import com.intellij.vcs.log.graph.GraphFacade;
import com.intellij.vcs.log.graph.PaintInfo;
import com.intellij.vcs.log.ui.VcsLogColorManager;
import com.intellij.vcs.log.ui.frame.VcsLogGraphTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collection;

import static com.intellij.vcs.log.graph.render.PrintParameters.HEIGHT_CELL;
import static com.intellij.vcs.log.graph.render.PrintParameters.WIDTH_NODE;

/**
 * @author erokhins
 */
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
public class GraphCommitCellRender extends AbstractPaddingCellRender {

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  // In case of diagonal edges, one node can be at most 3 "arrows" + 2 nodes at the left from another - that is enough for sure
  private static final int IMAGE_WIDTH_RESERVE = 5 * WIDTH_NODE;

  @NotNull private final GraphCellPainter graphPainter;
  @NotNull private final VcsLogDataHolder myDataHolder;
=======
  @NotNull private GraphFacade myGraphFacade;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  public GraphCommitCellRender(@NotNull GraphCellPainter graphPainter, @NotNull VcsLogDataHolder logDataHolder,
                               @NotNull VcsLogColorManager colorManager) {
    super(logDataHolder.getProject(), colorManager);
    this.graphPainter = graphPainter;
    myDataHolder = logDataHolder;
=======
  public GraphCommitCellRender(@NotNull VcsLogColorManager colorManager, @NotNull VcsLogDataHolder dataHolder,
                               @NotNull GraphFacade graphFacade, @NotNull VcsLogGraphTable table) {
    super(colorManager, dataHolder, table);
    myGraphFacade = graphFacade;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

  @Nullable
  @Override
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  protected int getLeftPadding(JTable table, @Nullable Object value) {
    GraphCommitCell cell = (GraphCommitCell)value;
    if (cell == null) {
      return 0;
    }
    return calcPaddingBeforeText(cell, (Graphics2D)table.getGraphics());
  }

  private int calcPaddingBeforeText(GraphCommitCell cell, Graphics2D g) {
    int refPadding = calcRefsPadding(cell.getRefsToThisCommit(), g);
    int countCells = cell.getPrintCell().countCell();
    int graphPadding = countCells * WIDTH_NODE;
    return refPadding + graphPadding;
=======
  protected PaintInfo getGraphImage(int row) {
    return myGraphFacade.paint(row);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

  public void updateGraphFacade(@NotNull GraphFacade graphFacade) {
    myGraphFacade = graphFacade;
  }

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  @Override
  protected void additionPaint(Graphics g, @Nullable Object value) {
    GraphCommitCell cell = (GraphCommitCell)value;
    if (cell == null) {
      return;
    }

    int width = calcPaddingBeforeText(cell, (Graphics2D)g) + IMAGE_WIDTH_RESERVE;
    BufferedImage image = UIUtil.createImage(width, HEIGHT_CELL, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = image.createGraphics();

    graphPainter.draw(g2, cell.getPrintCell());

    int countCells = cell.getPrintCell().countCell();
    int padding = countCells * WIDTH_NODE;
    Collection<VcsRef> refs = cell.getRefsToThisCommit();
    if (!refs.isEmpty()) {
      VirtualFile root = refs.iterator().next().getRoot(); // all refs are from the same commit => they have the same root
      refs = myDataHolder.getLogProvider(root).getReferenceManager().sort(refs);
    }
    drawRefs(g2, refs, padding);

    UIUtil.drawImage(g, image, 0, 0, null);
  }
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
}
