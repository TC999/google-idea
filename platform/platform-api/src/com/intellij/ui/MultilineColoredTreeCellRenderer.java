/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.ui;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.plaf.TreeUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Denis Zhdanov
 * @since 11/09/14
 */
public abstract class MultilineColoredTreeCellRenderer extends ColoredTreeCellRenderer {

  @NonNls protected static final String FONT_PROPERTY_NAME = "font";

  private final Insets myLabelInsets = new Insets(1, 2, 1, 2);

  @Nullable private String myPrefix;

  private int myPrefixWidth;
  private int myMinHeight;

  public MultilineColoredTreeCellRenderer() {
    setWrapText(true);
    addPropertyChangeListener(new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        if (FONT_PROPERTY_NAME.equalsIgnoreCase(evt.getPropertyName())) {
          onFontChanged();
        }
      }
    });
  }

  protected void setMinHeight(int height) {
    myMinHeight = height;
  }

  private void onFontChanged() {
    resetTextLayoutCache();
  }

  private FontMetrics getCurrFontMetrics() {
    return getFontMetrics(getFont());
  }

  public void setText(String[] lines, String prefix) {
    myPrefix = prefix;
    for (int i = 0; i < lines.length; i++) {
      String line = lines[i];
      append(line);
      if (i < lines.length - 1) {
        appendLineBreak();
      }
    }
  }

  @Override
  protected void beforePaintText(@NotNull Graphics g, int x, int textBaseLine) {
    if (!StringUtil.isEmpty(myPrefix)) {
      g.drawString(myPrefix, x - myPrefixWidth + 1, textBaseLine);
    }
  }

  @NotNull
  @Override
  public Dimension getMinimumSize() {
    Dimension preferredSize = getPreferredSize();
    Dimension result = new Dimension(preferredSize);
    Insets padding = getIpad();
    result.width = Math.max(result.width, padding.left + padding.right);
    result.height = Math.max(myMinHeight, Math.max(result.height, padding.top + padding.bottom));
    return result;
  }

  private static int getChildIndent(JTree tree) {
    TreeUI newUI = tree.getUI();
    if (newUI instanceof BasicTreeUI) {
      BasicTreeUI ui = (BasicTreeUI)newUI;
      return ui.getLeftChildIndent() + ui.getRightChildIndent();
    }
    else {
      return ((Integer)UIUtil.getTreeLeftChildIndent()).intValue() + ((Integer)UIUtil.getTreeRightChildIndent()).intValue();
    }
  }

  private static int getAvailableWidth(Object forValue, JTree tree) {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)forValue;
    int busyRoom = tree.getInsets().left + tree.getInsets().right + getChildIndent(tree) * node.getLevel();
    return tree.getVisibleRect().width - busyRoom - 2;
  }

  protected abstract void initComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus);

  @Override
  public void customizeCellRenderer(@NotNull JTree tree,
                                    Object value,
                                    boolean selected,
                                    boolean expanded,
                                    boolean leaf,
                                    int row,
                                    boolean hasFocus)
  {
    setFont(UIUtil.getTreeFont());

    initComponent(tree, value, selected, expanded, leaf, row, hasFocus);

    int availWidth = getAvailableWidth(value, tree);
    if (availWidth > 0) {
      setSize(availWidth, 100);     // height will be calculated automatically
    }

    int leftInset = myLabelInsets.left;

    Icon icon = getIcon();
    if (icon != null) {
      leftInset += icon.getIconWidth() + 2;
    }

    if (!StringUtil.isEmpty(myPrefix)) {
      myPrefixWidth = getCurrFontMetrics().stringWidth(myPrefix) + 5;
      leftInset += myPrefixWidth;
    }

    setIpad(new Insets(myLabelInsets.top, leftInset, myLabelInsets.bottom, myLabelInsets.right));
    if (icon != null) {
      setMinHeight(icon.getIconHeight());
    }
    else {
      setMinHeight(1);
    }

    setSize(getPreferredSize());
    resetTextLayoutCache();
  }

  public static JScrollPane installRenderer(final JTree tree, final MultilineColoredTreeCellRenderer renderer) {
    final TreeCellRenderer defaultRenderer = tree.getCellRenderer();

    JScrollPane scrollPane = new JBScrollPane(tree){
      private int myAddRemoveCounter = 0;
      private boolean myShouldResetCaches = false;
      @Override
      public void setSize(Dimension d) {
        boolean isChanged = getWidth() != d.width || myShouldResetCaches;
        super.setSize(d);
        if (isChanged) resetCaches();
      }

      @Override
      public void setBounds(int x, int y, int width, int height) {
        boolean isChanged = width != getWidth() || myShouldResetCaches;
        super.setBounds(x, y, width, height);
        if (isChanged) resetCaches();
      }

      private void resetCaches() {
        resetHeightCache(tree, defaultRenderer, renderer);
        myShouldResetCaches = false;
      }

      @Override
      public void addNotify() {
        super.addNotify();
        if (myAddRemoveCounter == 0) myShouldResetCaches = true;
        myAddRemoveCounter++;
      }

      @Override
      public void removeNotify() {
        super.removeNotify();
        myAddRemoveCounter--;
      }
    };
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    tree.setCellRenderer(renderer);

    scrollPane.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        resetHeightCache(tree, defaultRenderer, renderer);
      }

      @Override
      public void componentShown(ComponentEvent e) {
        // componentResized not called when adding to opened tool window.
        // Seems to be BUG#4765299, however I failed to create same code to reproduce it.
        // To reproduce it with IDEA: 1. remove this method, 2. Start any Ant task, 3. Keep message window open 4. start Ant task again.
        resetHeightCache(tree, defaultRenderer, renderer);
      }
    });

    return scrollPane;
  }

  private static void resetHeightCache(final JTree tree,
                                       final TreeCellRenderer defaultRenderer,
                                       final MultilineColoredTreeCellRenderer renderer) {
    tree.setCellRenderer(defaultRenderer);
    tree.setCellRenderer(renderer);
  }
}

