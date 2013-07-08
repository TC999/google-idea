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

package com.intellij.codeInspection.ex;

import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.codeInsight.daemon.HighlightDisplayKey;
import com.intellij.codeInspection.InspectionProfileEntry;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.psi.search.scope.packageSet.NamedScope;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * User: anna
 * Date: Dec 8, 2004
 */
public class Descriptor {
  private final String myText;
  private final String[] myGroup;
  private final HighlightDisplayKey myKey;

  private Element myConfig;
  private final InspectionToolWrapper myToolWrapper;
  private final HighlightDisplayLevel myLevel;
  private boolean myEnabled = false;
  private final NamedScope myScope;
  private static final Logger LOG = Logger.getInstance("#com.intellij.codeInspection.ex.Descriptor");
  private final ScopeToolState myState;
  private final InspectionProfileImpl myInspectionProfile;

  public Descriptor(@NotNull ScopeToolState state, @NotNull InspectionProfileImpl inspectionProfile, @NotNull Project project) {
    myState = state;
    myInspectionProfile = inspectionProfile;
<<<<<<< HEAD   (39f68d Merge "Revert "Snapshot d8891a7de15cebb78b6ce5711e50e531b42c)
    InspectionToolWrapper tool = (InspectionToolWrapper)state.getTool();
=======
    InspectionToolWrapper tool = state.getTool();
>>>>>>> BRANCH (c1ace1 Snapshot aea001abfc1b38fec3a821bcd5174cc77dc75787 from maste)
    myText = tool.getDisplayName();
    final String[] groupPath = tool.getGroupPath();
    myGroup = groupPath.length == 0 ? new String[]{InspectionProfileEntry.GENERAL_GROUP_NAME} : groupPath;
    myKey = HighlightDisplayKey.find(tool.getShortName());
<<<<<<< HEAD   (39f68d Merge "Revert "Snapshot d8891a7de15cebb78b6ce5711e50e531b42c)
    myLevel = inspectionProfile.getErrorLevel(myKey, ScopeToolStateUtil.getScope(state));
    myEnabled = inspectionProfile.isToolEnabled(myKey, ScopeToolStateUtil.getScope(state));
    myToolWrapper = tool;
    myScope = ScopeToolStateUtil.getScope(state);
=======
    myScope = state.getScope(project);
    myLevel = inspectionProfile.getErrorLevel(myKey, myScope, project);
    myEnabled = inspectionProfile.isToolEnabled(myKey, myScope, project);
    myToolWrapper = tool;
>>>>>>> BRANCH (c1ace1 Snapshot aea001abfc1b38fec3a821bcd5174cc77dc75787 from maste)
  }

  public boolean equals(Object obj) {
    if (!(obj instanceof Descriptor)) return false;
    final Descriptor descriptor = (Descriptor)obj;
    return myKey.equals(descriptor.getKey()) &&
           myLevel.equals(descriptor.getLevel()) &&
           myEnabled == descriptor.isEnabled() &&
           myState.equalTo(descriptor.getState());
  }

  public int hashCode() {
    final int hash = myKey.hashCode() + 29 * myLevel.hashCode();
    return myScope != null ? myScope.hashCode() + 29 * hash : hash;
  }

  public boolean isEnabled() {
    return myEnabled;
  }

  public void setEnabled(final boolean enabled) {
    myEnabled = enabled;
  }

  public String getText() {
    return myText;
  }

  @NotNull
  public HighlightDisplayKey getKey() {
    return myKey;
  }

  public HighlightDisplayLevel getLevel() {
    return myLevel;
  }

  @Nullable
  public Element getConfig() {
    return myConfig;
  }

  @NotNull
<<<<<<< HEAD   (39f68d Merge "Revert "Snapshot d8891a7de15cebb78b6ce5711e50e531b42c)
  public InspectionToolWrapper getTool() {
=======
  public InspectionToolWrapper getToolWrapper() {
>>>>>>> BRANCH (c1ace1 Snapshot aea001abfc1b38fec3a821bcd5174cc77dc75787 from maste)
    return myToolWrapper;
  }

  @Nullable
  public String loadDescription() {
    if (myConfig == null) {
<<<<<<< HEAD   (39f68d Merge "Revert "Snapshot d8891a7de15cebb78b6ce5711e50e531b42c)
      InspectionToolWrapper toolWrapper = getTool();
=======
      InspectionToolWrapper toolWrapper = getToolWrapper();
>>>>>>> BRANCH (c1ace1 Snapshot aea001abfc1b38fec3a821bcd5174cc77dc75787 from maste)
      myConfig = createConfigElement(toolWrapper);
    }

    return myToolWrapper.loadDescription();
  }

  public InspectionProfileImpl getInspectionProfile() {
    return myInspectionProfile;
  }

  public static Element createConfigElement(InspectionToolWrapper toolWrapper) {
    Element element = new Element("options");
    try {
<<<<<<< HEAD   (39f68d Merge "Revert "Snapshot d8891a7de15cebb78b6ce5711e50e531b42c)
      toolWrapper.writeSettings(element);
=======
      toolWrapper.getTool().writeSettings(element);
>>>>>>> BRANCH (c1ace1 Snapshot aea001abfc1b38fec3a821bcd5174cc77dc75787 from maste)
    }
    catch (WriteExternalException e) {
      LOG.error(e);
    }
    return element;
  }

  public String[] getGroup() {
    return myGroup;
  }

  public NamedScope getScope() {
    return myScope;
  }

  public ScopeToolState getState() {
    return myState;
  }

  @Override
  public String toString() {
    return myKey.toString();
  }
}
