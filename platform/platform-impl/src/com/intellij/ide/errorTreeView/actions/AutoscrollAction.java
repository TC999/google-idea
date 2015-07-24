/*
 * Copyright (C) 2015 The Android Open Source Project
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
package com.intellij.ide.errorTreeView.actions;

import com.intellij.icons.AllIcons;
import com.intellij.idea.ActionsBundle;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.DumbAware;

import javax.swing.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * Action which, when toggled, forces its target scroll pane to always stick to the bottom.
 */
public final class AutoscrollAction extends ToggleAction implements DumbAware {

  private boolean myEnabled;
  private JScrollPane myScrollPane;
  private AdjustmentListener myAutoscrollAdjustment = new AdjustmentListener() {
    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
      e.getAdjustable().setValue(e.getAdjustable().getMaximum());
    }
  };

  public AutoscrollAction(JScrollPane scrollPane) {
    super(ActionsBundle.message("action.EditorConsoleScrollToTheEnd.text"), ActionsBundle.message("action.EditorConsoleScrollToTheEnd.text"), AllIcons.RunConfigurations.Scroll_down);
    myScrollPane = scrollPane;
  }

  @Override
  public boolean isSelected(AnActionEvent e) {
    return myEnabled;
  }

  @Override
  public void setSelected(AnActionEvent e, boolean state) {
    if (myEnabled == state) {
      return;
    }

    myEnabled = state;
    JScrollBar vscroll = myScrollPane.getVerticalScrollBar();
    if (myEnabled) {
      vscroll.setValue(vscroll.getMaximum());
      vscroll.addAdjustmentListener(myAutoscrollAdjustment);
    }
    else {
      vscroll.removeAdjustmentListener(myAutoscrollAdjustment);
    }
  }
}
