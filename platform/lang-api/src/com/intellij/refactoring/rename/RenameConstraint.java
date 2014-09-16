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
package com.intellij.refactoring.rename;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * There is a possible case that particular 'rename' operation breaks some non-general ide constraint (i.e. the project is linked
 * to an external project and renaming it at the ide makes external build fail).
 * <p/>
 * This interface defines an extension point which allows to adjust 'rename operation' processing by e.g. showing a warning or completely
 * skipping further processing.
 *
 * @author Denis Zhdanov
 * @since 15/09/14
 */
public interface RenameConstraint {

  ExtensionPointName<RenameConstraint> EP_NAME = new ExtensionPointName<RenameConstraint>("com.intellij.renameConstraint");

  /**
   * Is called before 'rename' operation is executed.
   *
   * @param elementToRename    target element being renamed
   * @param newName            new name for the given element
   * @param callback           a callback which allows to control further 'continue rename operation' processing, e.g. actual
   *                           implementation is free to return <code>false</code> from this method thus taking responsibility
   *                           for further refactoring processing by itself (e.g. showing an 'ok/cancel' warning dialog and
   *                           {@link Callback#goFurther() proceed} the refactoring only if the user chooses 'ok')
   * @return                   <code>true</code> as an indication that refactoring processing should be continued
   *                           <code>false</code> otherwise
   */
  boolean onRename(@NotNull PsiElement elementToRename, @NotNull String newName, @NotNull Callback callback);

  interface Callback {

    /** Asks to proceed current 'rename' operation. */
    void goFurther();

    /** Cancels current 'rename' operation. */
    void cancel();
  }
}
