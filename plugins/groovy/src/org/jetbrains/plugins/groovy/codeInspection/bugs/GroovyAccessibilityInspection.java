/*
 * Copyright 2000-2014 JetBrains s.r.o.
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
package org.jetbrains.plugins.groovy.codeInspection.bugs;

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.diagnostic.Logger;
=======
import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.codeInsight.daemon.HighlightDisplayKey;
import com.intellij.codeInspection.InspectionProfile;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
import com.intellij.openapi.project.Project;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import com.intellij.psi.*;
import com.intellij.psi.util.PsiFormatUtil;
import com.intellij.psi.util.PsiFormatUtilBase;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ArrayUtil;
import com.intellij.util.Function;
import com.intellij.util.IncorrectOperationException;
=======
import com.intellij.profile.codeInspection.InspectionProjectProfileManager;
import com.intellij.psi.PsiElement;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.groovy.codeInspection.BaseInspection;
import org.jetbrains.plugins.groovy.codeInspection.GroovyInspectionBundle;
import org.jetbrains.plugins.groovy.codeInspection.GroovySuppressableInspectionTool;
import org.jetbrains.plugins.groovy.lang.psi.GrReferenceElement;
import org.jetbrains.plugins.groovy.lang.psi.GroovyFileBase;

/**
 * @author Maxim.Medvedev
 */
public class GroovyAccessibilityInspection extends GroovySuppressableInspectionTool {
  private static final String SHORT_NAME = "GroovyAccessibility";

  @Nls
  @NotNull
  @Override
  public String getGroupDisplayName() {
    return BaseInspection.PROBABLE_BUGS;
  }

  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return GroovyInspectionBundle.message("access.to.inaccessible.element");
  }

  @Override
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  protected String buildErrorString(Object... args) {
    return GroovyBundle.message("cannot.access", args);
  }

  @Override
  protected GroovyFix[] buildFixes(@NotNull PsiElement location) {
    if (!(location instanceof GrReferenceElement || location instanceof GrConstructorCall)) {
      location = location.getParent();
    }

    final GroovyResolveResult resolveResult;
    if (location instanceof GrConstructorCall) {
      resolveResult = ((GrConstructorCall)location).advancedResolve();
    }
    else {
      resolveResult = ((GrReferenceElement)location).advancedResolve();
    }

    final PsiElement element = resolveResult.getElement();
    if (!(element instanceof PsiMember)) return GroovyFix.EMPTY_ARRAY;
    final PsiMember refElement = (PsiMember)element;

    if (refElement instanceof PsiCompiledElement) return GroovyFix.EMPTY_ARRAY;

    PsiModifierList modifierList = refElement.getModifierList();
    if (modifierList == null) return GroovyFix.EMPTY_ARRAY;

    List<GroovyFix> fixes = new ArrayList<GroovyFix>();
    try {
      Project project = refElement.getProject();
      JavaPsiFacade facade = JavaPsiFacade.getInstance(project);
      PsiModifierList modifierListCopy = facade.getElementFactory().createFieldFromText("int a;", null).getModifierList();
      assert modifierListCopy != null;
      modifierListCopy.setModifierProperty(PsiModifier.STATIC, modifierList.hasModifierProperty(PsiModifier.STATIC));
      String minModifier = PsiModifier.PROTECTED;
      if (refElement.hasModifierProperty(PsiModifier.PROTECTED)) {
        minModifier = PsiModifier.PUBLIC;
      }
      String[] modifiers = {PsiModifier.PROTECTED, PsiModifier.PUBLIC, PsiModifier.PACKAGE_LOCAL};
      PsiClass accessObjectClass = PsiTreeUtil.getParentOfType(location, PsiClass.class, false);
      if (accessObjectClass == null) {
        final PsiFile file = location.getContainingFile();
        if (!(file instanceof GroovyFile)) return GroovyFix.EMPTY_ARRAY;
        accessObjectClass = ((GroovyFile)file).getScriptClass();
      }
      for (int i = ArrayUtil.indexOf(modifiers, minModifier); i < modifiers.length; i++) {
        String modifier = modifiers[i];
        modifierListCopy.setModifierProperty(modifier, true);
        if (facade.getResolveHelper().isAccessible(refElement, modifierListCopy, location, accessObjectClass, null)) {
          fixes.add(new GrModifierFix(refElement, modifier, true, true, new Function<ProblemDescriptor, PsiModifierList>() {
            @Override
            public PsiModifierList fun(ProblemDescriptor descriptor) {
              final PsiElement element = descriptor.getPsiElement();
              assert element instanceof GrReferenceElement : element;
              final PsiElement resolved = ((GrReferenceElement)element).resolve();
              assert resolved instanceof PsiModifierListOwner : resolved;
              return ((PsiModifierListOwner)resolved).getModifierList();
            }
          }));
        }
      }
    }
    catch (IncorrectOperationException e) {
      LOG.error(e);
    }
    return fixes.toArray(new GroovyFix[fixes.size()]);
  }

  @Override
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  public boolean isEnabledByDefault() {
    return true;
  }

  public static boolean isInspectionEnabled(GroovyFileBase file, Project project) {
    return getInspectionProfile(project).isToolEnabled(findDisplayKey(), file);
  }

  public static GroovyAccessibilityInspection getInstance(GroovyFileBase file, Project project) {
    return (GroovyAccessibilityInspection)getInspectionProfile(project).getUnwrappedTool(SHORT_NAME, file);
  }

  public static HighlightDisplayKey findDisplayKey() {
    return HighlightDisplayKey.find(SHORT_NAME);
  }

  public static HighlightDisplayLevel getHighlightDisplayLevel(Project project, GrReferenceElement ref) {
    return getInspectionProfile(project).getErrorLevel(findDisplayKey(), ref);
  }

  @NotNull
  private static InspectionProfile getInspectionProfile(@NotNull Project project) {
    return InspectionProjectProfileManager.getInstance(project).getInspectionProfile();
  }

  public static boolean isSuppressed(PsiElement ref) {
    return isElementToolSuppressedIn(ref, SHORT_NAME);
  }

}
