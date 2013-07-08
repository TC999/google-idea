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
package com.intellij.psi.impl.source.codeStyle.javadoc;

import com.intellij.util.containers.ContainerUtilRt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Method comment
 *
 * @author Dmitry Skavish
 */
public class JDMethodComment extends JDParamListOwnerComment {
<<<<<<< HEAD   (39f68d Merge "Revert "Snapshot d8891a7de15cebb78b6ce5711e50e531b42c)

  private static final @NonNls String THROWS_TAG = "@throws ";
  private static final @NonNls String EXCEPTION_TAG = "@exception ";

  private String returnTag;
  private ArrayList<NameDesc> throwsList;

  public JDMethodComment(CommentFormatter formatter) {
=======
  private String myReturnTag;
  private List<NameDesc> myThrowsList;

  public JDMethodComment(@NotNull CommentFormatter formatter) {
>>>>>>> BRANCH (c1ace1 Snapshot aea001abfc1b38fec3a821bcd5174cc77dc75787 from maste)
    super(formatter);
  }

  @Override
<<<<<<< HEAD   (39f68d Merge "Revert "Snapshot d8891a7de15cebb78b6ce5711e50e531b42c)
  protected void generateSpecial(String prefix, @NonNls StringBuffer sb) {
=======
  protected void generateSpecial(@NotNull String prefix, @NotNull StringBuilder sb) {
>>>>>>> BRANCH (c1ace1 Snapshot aea001abfc1b38fec3a821bcd5174cc77dc75787 from maste)
    super.generateSpecial(prefix, sb);

    if (myReturnTag != null) {
      if (myFormatter.getSettings().JD_KEEP_EMPTY_RETURN || myReturnTag.trim().length() != 0) {
        JDTag tag = JDTag.RETURN;
        sb.append(prefix);
        sb.append(tag.getWithEndWhitespace());
        sb.append(myFormatter.getParser().formatJDTagDescription(myReturnTag, prefix, true, tag.getDescriptionPrefix(prefix).length()));
        if (myFormatter.getSettings().JD_ADD_BLANK_AFTER_RETURN) {
          sb.append(prefix);
          sb.append('\n');
        }
      }
    }

    if (myThrowsList != null) {
      JDTag tag = myFormatter.getSettings().JD_USE_THROWS_NOT_EXCEPTION ? JDTag.THROWS : JDTag.EXCEPTION;
      generateList(prefix, sb, myThrowsList, tag.getWithEndWhitespace(),
                   myFormatter.getSettings().JD_ALIGN_EXCEPTION_COMMENTS,
                   myFormatter.getSettings().JD_KEEP_EMPTY_EXCEPTION,
                   myFormatter.getSettings().JD_PARAM_DESCRIPTION_ON_NEW_LINE
      );
    }
  }

<<<<<<< HEAD   (39f68d Merge "Revert "Snapshot d8891a7de15cebb78b6ce5711e50e531b42c)
  public void setReturnTag(String returnTag) {
    this.returnTag = returnTag;
  }

  public ArrayList<NameDesc> getThrowsList() {
    return throwsList;
  }

  public void addThrow(String className, String description) {
    if (throwsList == null) {
      throwsList = new ArrayList<NameDesc>();
=======
  public void setReturnTag(@NotNull String returnTag) {
    this.myReturnTag = returnTag;
  }

  public void addThrow(@NotNull String className, @Nullable String description) {
    if (myThrowsList == null) {
      myThrowsList = ContainerUtilRt.newArrayList();
>>>>>>> BRANCH (c1ace1 Snapshot aea001abfc1b38fec3a821bcd5174cc77dc75787 from maste)
    }
<<<<<<< HEAD   (39f68d Merge "Revert "Snapshot d8891a7de15cebb78b6ce5711e50e531b42c)
    throwsList.add(new NameDesc(className, description));
  }

=======
    myThrowsList.add(new NameDesc(className, description));
  }
>>>>>>> BRANCH (c1ace1 Snapshot aea001abfc1b38fec3a821bcd5174cc77dc75787 from maste)
}
