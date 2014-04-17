package org.jetbrains.idea.svn.content;

import com.intellij.openapi.vcs.VcsException;
import com.intellij.openapi.vcs.impl.ContentRevisionCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.idea.svn.api.BaseSvnClient;
import org.jetbrains.idea.svn.commandLine.CommandExecutor;
import org.jetbrains.idea.svn.commandLine.CommandUtil;
import org.jetbrains.idea.svn.commandLine.SvnCommandName;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc2.SvnTarget;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Konstantin Kolosovsky.
 */
public class CmdContentClient extends BaseSvnClient implements ContentClient {

  @Override
  public byte[] getContent(@NotNull SvnTarget target, @Nullable SVNRevision revision, @Nullable SVNRevision pegRevision)
    throws VcsException, FileTooBigRuntimeException {
    // TODO: rewrite this to provide output as Stream
    // TODO: Also implement max size constraint like in SvnKitContentClient
    // NOTE: Export could not be used to get content of scheduled for deletion file

    List<String> parameters = new ArrayList<String>();
    CommandUtil.put(parameters, target.getPathOrUrlString(), pegRevision);
    CommandUtil.put(parameters, revision);

    CommandExecutor command = CommandUtil.execute(myVcs, target, SvnCommandName.cat, parameters, null);
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    // TODO: currently binary output will be null for terminal mode - use text output in this case
    ByteArrayOutputStream output = command.getBinaryOutput();
    byte[] bytes = output != null ? output.toByteArray() : CharsetToolkit.getUtf8Bytes(command.getOutput());
=======
    byte[] bytes = command.getBinaryOutput().toByteArray();
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

    ContentRevisionCache.checkContentsSize(target.getPathOrUrlString(), bytes.length);

    return bytes;
  }
}
