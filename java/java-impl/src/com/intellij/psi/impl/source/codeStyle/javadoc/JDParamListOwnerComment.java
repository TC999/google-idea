/*
 * Copyright 2000-2010 JetBrains s.r.o.
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

/*
 * User: anna
 * Date: 30-Apr-2010
 */
package com.intellij.psi.impl.source.codeStyle.javadoc;

import com.intellij.formatting.IndentInfo;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.util.containers.ContainerUtilRt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class JDParamListOwnerComment extends JDComment {
  protected List<NameDesc> myParamsList;

  public JDParamListOwnerComment(@NotNull CommentFormatter formatter) {
    super(formatter);
  }

  @Override
  protected void generateSpecial(@NotNull String prefix, @NotNull StringBuilder sb) {
     if (myParamsList != null) {
      int before = sb.length();
      generateList(prefix, sb, myParamsList, JDTag.PARAM.getWithEndWhitespace(),
                   myFormatter.getSettings().JD_ALIGN_PARAM_COMMENTS,
                   myFormatter.getSettings().JD_KEEP_EMPTY_PARAMETER,
                   myFormatter.getSettings().JD_PARAM_DESCRIPTION_ON_NEW_LINE
      );

      int size = sb.length() - before;
      if (size > 0 && myFormatter.getSettings().JD_ADD_BLANK_AFTER_PARM_COMMENTS) {
        sb.append(prefix);
        sb.append('\n');
      }
    }
  }

  @Nullable
  public NameDesc getParameter(@Nullable String name) {
    return getNameDesc(name, myParamsList);
  }

<<<<<<< HEAD   (39f68d Merge "Revert "Snapshot d8891a7de15cebb78b6ce5711e50e531b42c)
  public void addParameter(String name, String description) {
    if (parmsList == null) {
      parmsList = new ArrayList<NameDesc>();
=======
  public void addParameter(@NotNull String name, @Nullable String description) {
    if (myParamsList == null) {
      myParamsList = ContainerUtilRt.newArrayList();
>>>>>>> BRANCH (c1ace1 Snapshot aea001abfc1b38fec3a821bcd5174cc77dc75787 from maste)
    }
<<<<<<< HEAD   (39f68d Merge "Revert "Snapshot d8891a7de15cebb78b6ce5711e50e531b42c)
    parmsList.add(new NameDesc(name, description));
  }

  static NameDesc getNameDesc(String name, ArrayList<NameDesc> list) {
=======
    myParamsList.add(new NameDesc(name, description));
  }

  @Nullable
  private static NameDesc getNameDesc(@Nullable String name, @Nullable List<NameDesc> list) {
>>>>>>> BRANCH (c1ace1 Snapshot aea001abfc1b38fec3a821bcd5174cc77dc75787 from maste)
    if (list == null) return null;
    for (NameDesc aList : list) {
      if (aList.name.equals(name)) {
        return aList;
      }
    }
    return null;
  }

  /**
   * Generates parameters or exceptions
   *
   */
<<<<<<< HEAD   (39f68d Merge "Revert "Snapshot d8891a7de15cebb78b6ce5711e50e531b42c)
  protected void generateList(String prefix,
                              StringBuffer sb,
                              ArrayList<NameDesc> list,
                              String tag,
=======
  protected void generateList(@NotNull String prefix,
                              @NotNull StringBuilder sb,
                              @NotNull List<NameDesc> list,
                              @NotNull String tag,
>>>>>>> BRANCH (c1ace1 Snapshot aea001abfc1b38fec3a821bcd5174cc77dc75787 from maste)
                              boolean align_comments,
                              boolean generate_empty_tags,
                              boolean wrapDescription)
  {
    CodeStyleSettings settings = myFormatter.getSettings();
    CommonCodeStyleSettings.IndentOptions indentOptions = settings.getIndentOptions(JavaFileType.INSTANCE);
    String continuationIndent = new IndentInfo(0, indentOptions.CONTINUATION_INDENT_SIZE, 0).generateNewWhiteSpace(indentOptions);

    int max = 0;

    if (align_comments && !wrapDescription) {
      for (NameDesc nd: list) {
        int currentLength = nd.name.length();
        if (isNull(nd.desc) && !generate_empty_tags) continue;
        //finding longest parameter length
        if (currentLength > max) {
          max = currentLength;
        }
      }
    }

<<<<<<< HEAD   (39f68d Merge "Revert "Snapshot d8891a7de15cebb78b6ce5711e50e531b42c)
    StringBuffer fill = new StringBuffer(prefix.length() + tag.length() + max + 1);
=======
    StringBuilder fill = new StringBuilder(prefix.length() + tag.length() + max + 1);
>>>>>>> BRANCH (c1ace1 Snapshot aea001abfc1b38fec3a821bcd5174cc77dc75787 from maste)
    fill.append(prefix);
    StringUtil.repeatSymbol(fill, ' ', max + 1 + tag.length());

    String wrapParametersPrefix = prefix + continuationIndent;
    for (NameDesc nd : list) {
      if (isNull(nd.desc) && !generate_empty_tags) continue;
      if (wrapDescription && !isNull(nd.desc)) {
        sb.append(prefix).append(tag).append(nd.name).append("\n");
        sb.append(wrapParametersPrefix);
        sb.append(myFormatter.getParser().formatJDTagDescription(nd.desc, wrapParametersPrefix));
      }
      else if (align_comments) {
        sb.append(prefix);
        sb.append(tag);
        sb.append(nd.name);
        int spacesNumber = max + 1 - nd.name.length();
        StringUtil.repeatSymbol(sb, ' ', Math.max(0, spacesNumber));
<<<<<<< HEAD   (39f68d Merge "Revert "Snapshot d8891a7de15cebb78b6ce5711e50e531b42c)
        sb.append(myFormatter.getParser().splitIntoCLines(nd.desc, fill, false));
=======
        sb.append(myFormatter.getParser().formatJDTagDescription(nd.desc, fill));
>>>>>>> BRANCH (c1ace1 Snapshot aea001abfc1b38fec3a821bcd5174cc77dc75787 from maste)
      }
      else {
        sb.append(prefix);
        String description = (nd.desc == null) ? "" : nd.desc;
        sb.append(myFormatter.getParser().formatJDTagDescription(tag + nd.name + " " + description, prefix));
      }
    }
  }
}
