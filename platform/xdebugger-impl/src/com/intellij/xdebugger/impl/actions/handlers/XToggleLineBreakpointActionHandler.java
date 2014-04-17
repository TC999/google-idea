/*
 * Copyright 2000-2009 JetBrains s.r.o.
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
package com.intellij.xdebugger.impl.actions.handlers;

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import com.intellij.codeInsight.folding.impl.FoldingUtil;
import com.intellij.codeInsight.folding.impl.actions.ExpandRegionAction;
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import com.intellij.openapi.editor.FoldRegion;
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.HashSet;
import com.intellij.xdebugger.XDebuggerManager;
import com.intellij.xdebugger.XDebuggerUtil;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.breakpoints.XBreakpointManager;
import com.intellij.xdebugger.breakpoints.XLineBreakpointType;
import com.intellij.xdebugger.impl.XDebuggerUtilImpl;
import com.intellij.xdebugger.impl.actions.DebuggerActionHandler;
import com.intellij.xdebugger.impl.breakpoints.XBreakpointUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * @author nik
 */
public class XToggleLineBreakpointActionHandler extends DebuggerActionHandler {

  private final boolean myTemporary;

  public XToggleLineBreakpointActionHandler(boolean temporary) {
    myTemporary = temporary;
  }

  public boolean isEnabled(@NotNull final Project project, final AnActionEvent event) {
    XLineBreakpointType<?>[] breakpointTypes = XDebuggerUtil.getInstance().getLineBreakpointTypes();
    final XBreakpointManager breakpointManager = XDebuggerManager.getInstance(project).getBreakpointManager();
    for (XSourcePosition position : XDebuggerUtilImpl.getAllCaretsPositions(project, event.getDataContext())) {
      for (XLineBreakpointType<?> breakpointType : breakpointTypes) {
        final VirtualFile file = position.getFile();
        final int line = position.getLine();
        if (breakpointType.canPutAt(file, line, project) || breakpointManager.findBreakpointAtLine(breakpointType, file, line) != null) {
          return true;
        }
      }
    }
    return false;
  }

  public void perform(@NotNull final Project project, final AnActionEvent event) {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    XSourcePosition position = XDebuggerUtilImpl.getCaretPosition(project, event.getDataContext());
    if (position == null) return;

    Editor editor = event.getData(CommonDataKeys.EDITOR);

    // for folded text check each line and find out type with the biggest priority
    int lineStart = position.getLine();
    int linesEnd = lineStart;
    FoldRegion region = FoldingUtil.findFoldRegionStartingAtLine(editor, lineStart);
    if (region != null && !region.isExpanded()) {
      linesEnd = region.getDocument().getLineNumber(region.getEndOffset());
    }

    VirtualFile file = position.getFile();
    final XBreakpointManager breakpointManager = XDebuggerManager.getInstance(project).getBreakpointManager();
    XLineBreakpointType<?>[] lineTypes = XDebuggerUtil.getInstance().getLineBreakpointTypes();
    XLineBreakpointType<?> typeWinner = null;
    int lineWinner = -1;
    for (int line = lineStart; line <= linesEnd; line++) {
      for (XLineBreakpointType<?> type : lineTypes) {
        final XLineBreakpoint<? extends XBreakpointProperties> breakpoint = breakpointManager.findBreakpointAtLine(type, file, line);
        if (breakpoint != null && myTemporary && !breakpoint.isTemporary()) {
          breakpoint.setTemporary(true);
        } else if (type.canPutAt(file, line, project) || breakpoint != null) {
          if (typeWinner == null || type.getPriority() > typeWinner.getPriority()) {
            typeWinner = type;
            lineWinner = line;
          }
        }
=======
    Editor editor = event.getData(CommonDataKeys.EDITOR);
    // do not toggle more than once on the same line
    Set<Integer> processedLines = new HashSet<Integer>();
    for (XSourcePosition position : XDebuggerUtilImpl.getAllCaretsPositions(project, event.getDataContext())) {
      if (processedLines.add(position.getLine())) {
        XBreakpointUtil.toggleLineBreakpoint(project, position.getFile(), editor, position.getLine(), myTemporary, true);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
      }
    }
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")

    if (typeWinner != null) {
      XDebuggerUtil.getInstance().toggleLineBreakpoint(project, typeWinner, file, lineWinner, myTemporary);
    }

    ExpandRegionAction.expandRegionAtCaret(project, editor);
    if (editor != null && lineStart != lineWinner) {
      editor.getCaretModel().moveToOffset(editor.getDocument().getLineStartOffset(lineWinner));
    }
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }
}
