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
package com.intellij.designer;

import com.intellij.designer.designSurface.DesignerEditorPanel;
import com.intellij.designer.palette.PaletteToolWindowContent;
import com.intellij.designer.palette.PaletteToolWindowManager;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowAnchor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DesignerCustomizations {
  public static final ExtensionPointName<DesignerCustomizations> EP_NAME = ExtensionPointName.create("Designer.customizations");

  /**
   * Default location of the palette
   */
  @NotNull
  public ToolWindowAnchor getPaletteAnchor() {
    return ToolWindowAnchor.RIGHT;
  }

  /**
   * Default location of the designer/structure window
   */
  @NotNull
  public ToolWindowAnchor getStructureAnchor() {
    return ToolWindowAnchor.LEFT;
  }

  @Nullable public abstract AbstractToolWindowManager getPaletteWindowManager(Project project);
  @Nullable public abstract AbstractToolWindowManager getDesignerWindowManager(Project project);
  @Nullable public abstract PaletteToolWindowContent getPaletteWindowContent(DesignerEditorPanel designer);
  @Nullable public abstract DesignerToolWindowContent getDesignerWindowContent(DesignerEditorPanel designer);
}