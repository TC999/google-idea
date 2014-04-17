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
package com.intellij.vcs.log.ui.filter;

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileTypes.FileTypes;
import com.intellij.openapi.keymap.KeymapUtil;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.*;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.spellchecker.ui.SpellCheckingEditorCustomization;
import com.intellij.ui.EditorCustomization;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.EditorTextFieldProvider;
import com.intellij.ui.SoftWrapsEditorCustomization;
=======
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
import com.intellij.util.Function;
import com.intellij.util.TextFieldCompletionProviderDumbAware;
import com.intellij.util.containers.ContainerUtil;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import com.intellij.vcs.log.VcsFullCommitDetails;
=======
import com.intellij.vcs.log.VcsCommitMetadata;
import com.intellij.vcs.log.VcsLogUserFilter;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
import com.intellij.vcs.log.VcsUser;
import com.intellij.vcs.log.data.VcsLogDataHolder;
import com.intellij.vcs.log.data.VcsLogUiProperties;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import com.intellij.vcs.log.VcsLogUserFilter;
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.util.*;
import java.util.List;
=======
import java.util.*;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

/**
 * Show a popup to select a user or enter the user name.
 */
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
class UserFilterPopupComponent extends FilterPopupComponent<VcsLogUserFilter> {
=======
class UserFilterPopupComponent extends MultipleValueFilterPopupComponent<VcsLogUserFilter> {
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

  private static final String ME = "me";
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  private static final char[] USERS_SEPARATORS = { ',', '|', '\n' };
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

  private final VcsLogDataHolder myDataHolder;
  private final VcsLogUiProperties myUiProperties;

  @Nullable private Collection<String> mySelectedUsers;

  UserFilterPopupComponent(@NotNull VcsLogClassicFilterUi filterUi, @NotNull VcsLogDataHolder dataHolder,
                           @NotNull VcsLogUiProperties uiProperties) {
    super(filterUi, "User");
    myDataHolder = dataHolder;
    myUiProperties = uiProperties;
  }

  @Override
  protected ActionGroup createActionGroup() {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    AnAction allAction = new DumbAwareAction(ALL) {
      @Override
      public void actionPerformed(AnActionEvent e) {
        apply(null, ALL, ALL);
      }
    };

    DefaultActionGroup group = new DefaultActionGroup();
    group.add(allAction);
    group.add(new UserAction(Collections.singleton(ME)));

    List<List<String>> recentlyFilteredUsers = myUiProperties.getRecentlyFilteredUserGroups();
    if (!recentlyFilteredUsers.isEmpty()) {
      group.addSeparator("Recently searched");
      for (List<String> recentGroup : recentlyFilteredUsers) {
        group.add(new UserAction(recentGroup));
      }
=======
    DefaultActionGroup group = new DefaultActionGroup();
    group.add(createAllAction());
    group.add(createSelectMultipleValuesAction());
    if (!myDataHolder.getCurrentUser().isEmpty()) {
      group.add(createPredefinedValueAction(Collections.singleton(ME)));
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    }
    group.addAll(createRecentItemsActionGroup());
    return group;
  }

  @NotNull
  @Override
  protected List<List<String>> getRecentValuesFromSettings() {
    return myUiProperties.getRecentlyFilteredUserGroups();
  }

  @Nullable
  @Override
  protected VcsLogUserFilter getFilter() {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    if (mySelectedUsers == null) {
=======
    if (getSelectedValues() == null) {
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
      return null;
    }
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    myUiProperties.addRecentlyFilteredUserGroup(new ArrayList<String>(mySelectedUsers));
    return new VcsLogUserFilterImpl(mySelectedUsers, myDataHolder.getCurrentUser());
  }

  private void apply(Collection<String> users, String text, String tooltip) {
    mySelectedUsers = users;
    applyFilters();
    setValue(text, tooltip);
  }

  @NotNull
  private static String displayableText(@NotNull Collection<String> users) {
    if (users.size() == 1) {
      return users.iterator().next();
    }
    return StringUtil.shortenTextWithEllipsis(StringUtil.join(users, "|"), 30, 0, true);
  }

  @NotNull
  private static String tooltip(@NotNull Collection<String> users) {
    return StringUtil.join(users, ", ");
  }

  private class UserAction extends DumbAwareAction {
    @NotNull private final Collection<String> myUsers;

    UserAction(@NotNull Collection<String> users) {
      super(displayableText(users), tooltip(users), null);
      myUsers = users;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
      apply(myUsers, displayableText(myUsers), tooltip(myUsers));
    }
=======
    return new VcsLogUserFilterImpl(getSelectedValues(), myDataHolder.getCurrentUser());
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

  @Override
  protected void rememberValuesInSettings(@NotNull Collection<String> values) {
    myUiProperties.addRecentlyFilteredUserGroup(new ArrayList<String>(values));
  }

  @NotNull
  @Override
  protected List<String> getAllValues() {
    return ContainerUtil.map(myDataHolder.getAllUsers(), new Function<VcsUser, String>() {
      @Override
      public String fun(VcsUser user) {
        return user.getName();
      }
    });
  }

  private static class VcsLogUserFilterImpl implements VcsLogUserFilter {

    @NotNull private final Collection<String> myUsers;
    @NotNull private final Map<VirtualFile, VcsUser> myData;

    public VcsLogUserFilterImpl(@NotNull Collection<String> users, @NotNull Map<VirtualFile, VcsUser> meData) {
      myUsers = users;
      myData = meData;
    }

    @NotNull
    @Override
    public Collection<String> getUserNames(@NotNull final VirtualFile root) {
      return ContainerUtil.mapNotNull(myUsers, new Function<String, String>() {
        @Override
        public String fun(String user) {
          if (ME.equals(user)) {
            VcsUser vcsUser = myData.get(root);
            return vcsUser == null ? null : vcsUser.getName();
          }
          return user;
        }
      });
    }

    @Override
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    public void actionPerformed(AnActionEvent e) {
      Project project = e.getProject();
      if (project == null) {
        return;
      }

      Collection<String> users = ContainerUtil.map(myDataHolder.getAllUsers(), new Function<VcsUser, String>() {
=======
    public boolean matches(@NotNull final VcsCommitMetadata commit) {
      return ContainerUtil.exists(getUserNames(commit.getRoot()), new Condition<String>() {
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
        @Override
        public boolean value(String user) {
          String lowerUser = user.toLowerCase();
          return commit.getAuthor().getName().toLowerCase().contains(lowerUser) ||
                 commit.getAuthor().getEmail().toLowerCase().contains(lowerUser);
        }
      });
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")

      final MultilinePopupBuilder popupBuilder = new MultilinePopupBuilder(project, users);
      JBPopup popup = popupBuilder.createPopup();
      popup.addListener(new JBPopupAdapter() {
        @Override
        public void onClosed(LightweightWindowEvent event) {
          if (event.isOk()) {
            final String userText = popupBuilder.getText().trim();
            Collection<String> selectedUsers = ContainerUtil.toCollection(StringUtil.tokenize(userText, new String(USERS_SEPARATORS)));
            if (selectedUsers.isEmpty()) {
              apply(null, ALL, ALL);
            }
            else {
              apply(selectedUsers, displayableText(selectedUsers), tooltip(selectedUsers));
            }
          }
        }
      });
      popup.showUnderneathOf(UserFilterPopupComponent.this);
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    }
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  }

  private static class MultilinePopupBuilder {
    private final EditorTextField myTextField;

    MultilinePopupBuilder(@NotNull Project project, @NotNull final Collection<String> users) {
      myTextField = createTextField(project);
      new UsersCompletionProvider(users).apply(myTextField);
    }

    @NotNull
    private static EditorTextField createTextField(@NotNull Project project) {
      final EditorTextFieldProvider service = ServiceManager.getService(project, EditorTextFieldProvider.class);
      List<EditorCustomization> features = Arrays.<EditorCustomization>asList(SoftWrapsEditorCustomization.ENABLED,
                                                                              SpellCheckingEditorCustomization.DISABLED);
      EditorTextField textField = service.getEditorField(FileTypes.PLAIN_TEXT.getLanguage(), project, features);
      textField.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2), textField.getBorder()));
      textField.setOneLineMode(false);
      return textField;
    }

    @NotNull
    JBPopup createPopup() {
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(myTextField, BorderLayout.CENTER);
      ComponentPopupBuilder builder = JBPopupFactory.getInstance().createComponentPopupBuilder(panel, myTextField)
        .setCancelOnClickOutside(true)
        .setAdText(KeymapUtil.getShortcutsText(CommonShortcuts.CTRL_ENTER.getShortcuts()) + " to finish")
        .setMovable(true)
        .setRequestFocus(true)
        .setResizable(true)
        .setMayBeParent(true);

      final JBPopup popup = builder.createPopup();
      popup.setMinimumSize(new Dimension(200, 90));
      AnAction okAction = new DumbAwareAction() {
        @Override
        public void actionPerformed(AnActionEvent e) {
          unregisterCustomShortcutSet(popup.getContent());
          popup.closeOk(e.getInputEvent());
        }
      };
      okAction.registerCustomShortcutSet(CommonShortcuts.CTRL_ENTER, popup.getContent());
      return popup;
    }

    String getText() {
      return myTextField.getText();
    }

    private static class UsersCompletionProvider extends TextFieldCompletionProviderDumbAware {
      @NotNull private final Collection<String> myUsers;

      UsersCompletionProvider(@NotNull Collection<String> users) {
        super(true);
        myUsers = users;
      }

      @NotNull
      @Override
      protected String getPrefix(@NotNull String currentTextPrefix) {
        final int separatorPosition = lastSeparatorPosition(currentTextPrefix);
        return separatorPosition == -1 ? currentTextPrefix : currentTextPrefix.substring(separatorPosition + 1).trim();
      }

      private static int lastSeparatorPosition(@NotNull String text) {
        int lastPosition = -1;
        for (char separator : USERS_SEPARATORS) {
          int lio = text.lastIndexOf(separator);
          if (lio > lastPosition) {
            lastPosition = lio;
          }
        }
        return lastPosition;
      }

      @Override
      protected void addCompletionVariants(@NotNull String text, int offset, @NotNull String prefix,
                                           @NotNull CompletionResultSet result) {
        result.addLookupAdvertisement("Select one or more users separated with comma, | or new lines");
        for (String completionVariant : myUsers) {
          final LookupElementBuilder element = LookupElementBuilder.create(completionVariant);
          result.addElement(element.withLookupString(completionVariant.toLowerCase()));
        }
      }
    }
  }

  private static class VcsLogUserFilterImpl implements VcsLogUserFilter {

    @NotNull private final Collection<String> myUsers;
    @NotNull private final Map<VirtualFile, VcsUser> myData;

    public VcsLogUserFilterImpl(@NotNull Collection<String> users, @NotNull Map<VirtualFile, VcsUser> meData) {
      myUsers = users;
      myData = meData;
    }

    @NotNull
    @Override
    public Collection<String> getUserNames(@NotNull final VirtualFile root) {
      return ContainerUtil.map(myUsers, new Function<String, String>() {
        @Override
        public String fun(String user) {
          return ME.equals(user) ? myData.get(root).getName() : user;
        }
      });
    }

    @Override
    public boolean matches(@NotNull final VcsFullCommitDetails commit) {
      return ContainerUtil.exists(getUserNames(commit.getRoot()), new Condition<String>() {
        @Override
        public boolean value(String user) {
          String lowerUser = user.toLowerCase();
          return commit.getAuthor().getName().toLowerCase().contains(lowerUser) ||
                 commit.getAuthor().getEmail().toLowerCase().contains(lowerUser);
        }
      });
    }

=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }
}
