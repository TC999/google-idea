package com.intellij.vcs.log.impl;

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import com.intellij.openapi.diagnostic.Logger;
=======
import com.intellij.openapi.util.ThrowableComputable;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
import com.intellij.openapi.vcs.changes.Change;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.vcs.log.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public class VcsLogObjectsFactoryImpl implements VcsLogObjectsFactory {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")

  private static final Logger LOG = Logger.getInstance(VcsLogObjectsFactoryImpl.class);

  @NotNull private final VcsLogManager myLogManager;

  public VcsLogObjectsFactoryImpl(@NotNull VcsLogManager logManager) {
    myLogManager = logManager;
  }
=======
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

  @NotNull
  @Override
  public Hash createHash(@NotNull String stringHash) {
    return HashImpl.build(stringHash);
  }

  @NotNull
  @Override
  public VcsCommit createCommit(@NotNull Hash hash, @NotNull List<Hash> parents) {
    return new VcsCommitImpl(hash, parents);
  }

  @NotNull
  @Override
  public TimedVcsCommit createTimedCommit(@NotNull Hash hash, @NotNull List<Hash> parents, long timeStamp) {
    return new TimedVcsCommitImpl(hash, parents, timeStamp);
  }

  @NotNull
  @Override
  public VcsShortCommitDetails createShortDetails(@NotNull Hash hash, @NotNull List<Hash> parents, long timeStamp,
                                                  @NotNull VirtualFile root, @NotNull String subject,
                                                  @NotNull String authorName, String authorEmail) {
    VcsUser author = createUser(authorName, authorEmail);
    return new VcsShortCommitDetailsImpl(hash, parents, timeStamp, root, subject, author);
  }

  @NotNull
  @Override
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  public VcsFullCommitDetails createFullDetails(@NotNull Hash hash, @NotNull List<Hash> parents, long time, @NotNull VirtualFile root,
=======
  public VcsCommitMetadata createCommitMetadata(@NotNull Hash hash, @NotNull List<Hash> parents, long time, @NotNull VirtualFile root,
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
                                                @NotNull String subject, @NotNull String authorName, @NotNull String authorEmail,
                                                @NotNull String message, @NotNull String committerName,
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
                                                @NotNull String committerEmail, long authorTime, @NotNull List<Change> changes,
                                                @NotNull ContentRevisionFactory contentRevisionFactory) {
=======
                                                @NotNull String committerEmail, long authorTime) {
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    VcsUser author = createUser(authorName, authorEmail);
    VcsUser committer = createUser(committerName, committerEmail);
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    return new VcsFullCommitDetailsImpl(hash, parents, time, root, subject, author, message, committer, authorTime,
                                        changes, contentRevisionFactory);
=======
    return new VcsCommitMetadataImpl(hash, parents, time, root, subject, author, message, committer, authorTime);
  }

  @NotNull
  @Override
  public VcsFullCommitDetails createFullDetails(@NotNull Hash hash, @NotNull List<Hash> parents, long time, VirtualFile root,
                                                @NotNull String subject, @NotNull String authorName, @NotNull String authorEmail,
                                                @NotNull String message, @NotNull String committerName, @NotNull String committerEmail,
                                                long authorTime,
                                                @NotNull ThrowableComputable<Collection<Change>, ? extends Exception> changesGetter) {
    VcsUser author = createUser(authorName, authorEmail);
    VcsUser committer = createUser(committerName, committerEmail);
    return new VcsChangesLazilyParsedDetails(hash, parents, time, root, subject, author, message, committer, authorTime, changesGetter);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

  @NotNull
  @Override
  public VcsUser createUser(@NotNull String name, @NotNull String email) {
    return new VcsUserImpl(name, email);
  }

  @NotNull
  @Override
  public VcsRef createRef(@NotNull Hash commitHash, @NotNull String name, @NotNull VcsRefType type, @NotNull VirtualFile root) {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    return new VcsRefImpl(new NotNullFunction<Hash, Integer>() {
      @NotNull
      @Override
      public Integer fun(Hash hash) {
        VcsLogDataHolder dataHolder = myLogManager.getDataHolder();
        if (dataHolder == null) {
          LOG.error("The log data holder should have been initialized at this point");
          return -1;
        }
        return dataHolder.putHash(hash);
      }
    }, commitHash, name, type, root);
=======
    return new VcsRefImpl(commitHash, name, type, root);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

}
