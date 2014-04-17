/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.intellij.tasks.jira.soap;

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import com.atlassian.theplugin.jira.api.JIRAComment;
import com.atlassian.theplugin.jira.api.JIRAIssue;
import com.intellij.tasks.Comment;
import com.intellij.tasks.TaskRepository;
import com.intellij.tasks.TaskState;
import com.intellij.tasks.TaskType;
import com.intellij.tasks.jira.JiraTask;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Slightly refactored original version of {@link JIRAIssue} adapter for SOAP version of JIRA API.
 *
 * @author Dmitry Avdeev
 */
class JiraSoapTask extends JiraTask {

  private final JIRAIssue myJiraIssue;

  public JiraSoapTask(@NotNull JIRAIssue jiraIssue, @NotNull TaskRepository repository) {
    super(repository);
    myJiraIssue = jiraIssue;
  }

  @NotNull
  public String getId() {
    return myJiraIssue.getKey();
  }

  @NotNull
  public String getSummary() {
    return myJiraIssue.getSummary();
  }

  public String getDescription() {
    return myJiraIssue.getDescription();
  }

  @NotNull
  public Comment[] getComments() {
    List<JIRAComment> jiraComments = myJiraIssue.getComments();
    if (jiraComments == null) return new Comment[0];
    return ContainerUtil.map2Array(jiraComments, Comment.class, new Function<JIRAComment, Comment>() {
      public Comment fun(final JIRAComment comment) {
        return new Comment() {

          public String getText() {
            return comment.getBody();
          }

          public String getAuthor() {
            return comment.getAuthorFullName();
          }

          public Date getDate() {
            return comment.getCreationDate().getTime();
          }

          @Override
          public String toString() {
            return comment.getAuthorFullName();
          }
        };
      }
    });
  }

  @Nullable
  @Override
  protected String getIconUrl() {
    return myJiraIssue.getTypeIconUrl();
  }

  @NotNull
  @Override
  public TaskType getType() {
    return getTypeByName(myJiraIssue.getType());
  }

  @Override
  public TaskState getState() {
    return getStateById((int)myJiraIssue.getStatusId());
  }

  @Nullable
  @Override
  public Date getUpdated() {
    return getDate(myJiraIssue.getUpdated());
  }

  @Nullable
  private static Date getDate(String date) {
    try {
      return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US).parse(date);
    }
    catch (ParseException e) {
      return null;
    }
  }

  @Override
  public Date getCreated() {
    return getDate(myJiraIssue.getCreated());
  }

  @Override
  public String getIssueUrl() {
    return myJiraIssue.getIssueUrl();
=======
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.tasks.Comment;
import com.intellij.tasks.TaskRepository;
import com.intellij.tasks.TaskState;
import com.intellij.tasks.TaskType;
import com.intellij.tasks.jira.JiraTask;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Slightly refactored original version of {@link JIRAIssue} adapter for SOAP version of JIRA API.
 *
 * @author Mikhail Golubev
 * @author Dmitry Avdeev
 */
class JiraSoapTask extends JiraTask {

  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

  private final String myKey;
  private final String mySummary;
  private final String myDescription;
  private final String myIconUrl;
  private final Date myUpdated;
  private final Date myCreated;
  private final TaskState myState;
  private final TaskType myType;

  private final List<Comment> myComments;

  public JiraSoapTask(@NotNull Element element, @NotNull TaskRepository repository) {
    super(repository);
    myKey = element.getChildText("key");
    mySummary = element.getChildText("summary");
    myDescription = element.getChildText("description");

    myIconUrl = getChildAttribute(element, "type", "iconUrl");

    myType = getTypeByName(element.getChildText("type"));

    String statusIdText = getChildAttribute(element, "status", "id");
    myState = getStateById(StringUtil.isEmpty(statusIdText) ? 0 : Integer.parseInt(statusIdText));

    myCreated = parseDate(element.getChildText("created"));
    myUpdated = parseDate(element.getChildText("updated"));

    Element comments = element.getChild("comments");
    if (comments != null) {
      myComments = ContainerUtil.map(comments.getChildren("comment"), new Function<Element, Comment>() {
        @Override
        public Comment fun(final Element element) {
          return new Comment() {
            @Override
            public String getText() {
              return element.getText();
            }

            @Nullable
            @Override
            public String getAuthor() {
              return element.getAttributeValue("author");
            }

            @Nullable
            @Override
            public Date getDate() {
              return parseDate(element.getAttributeValue("created"));
            }
          };
        }
      });
    } else {
      myComments = ContainerUtil.emptyList();
    }
  }

  @NotNull
  public String getId() {
    return myKey;
  }

  @NotNull
  public String getSummary() {
    return mySummary;
  }

  public String getDescription() {
    return myDescription;
  }

  @NotNull
  public Comment[] getComments() {
    return myComments.toArray(new Comment[myComments.size()]);
  }

  @Nullable
  @Override
  protected String getIconUrl() {
    return myIconUrl;
  }

  @NotNull
  @Override
  public TaskType getType() {
    return myType;
  }

  @Override
  public TaskState getState() {
    return myState;
  }

  @Nullable
  @Override
  public Date getUpdated() {
    return myUpdated;
  }

  @Override
  public Date getCreated() {
    return myCreated;
  }

  @Nullable
  private static Date parseDate(@NotNull String date) {
    try {
      return DATE_FORMAT.parse(date);
    }
    catch (ParseException e) {
      return null;
    }
  }

  @Nullable
  private static String getChildAttribute(@NotNull Element parent, @NotNull String childName, @NotNull String attributeName) {
    Element child = parent.getChild(childName);
    if (child == null) {
      return null;
    }
    return child.getAttributeValue(attributeName);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }
}
