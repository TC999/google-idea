package org.jetbrains.jps.maven.compiler;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.io.FileUtilRt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.builders.BuildOutputConsumer;
import org.jetbrains.jps.builders.DirtyFilesHolder;
import org.jetbrains.jps.builders.FileProcessor;
import org.jetbrains.jps.builders.storage.BuildDataPaths;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.ProjectBuildException;
import org.jetbrains.jps.incremental.TargetBuilder;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.CompilerMessage;
import org.jetbrains.jps.incremental.messages.ProgressMessage;
import org.jetbrains.jps.maven.model.JpsMavenExtensionService;
import org.jetbrains.jps.maven.model.impl.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import java.util.Arrays;
import java.util.Collections;
=======
import java.util.*;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

/**
 * @author Eugene Zhuravlev
 *         Date: 10/6/11
 */
public class MavenResourcesBuilder extends TargetBuilder<MavenResourceRootDescriptor, MavenResourcesTarget> {
  public static final String BUILDER_NAME = "Maven Resources Compiler";

  public MavenResourcesBuilder() {
    super(Arrays.asList(MavenResourcesTargetType.PRODUCTION, MavenResourcesTargetType.TEST));
  }

  @Override
  public void build(@NotNull final MavenResourcesTarget target, @NotNull final DirtyFilesHolder<MavenResourceRootDescriptor, MavenResourcesTarget> holder, @NotNull final BuildOutputConsumer outputConsumer, @NotNull final CompileContext context) throws ProjectBuildException, IOException {
    final BuildDataPaths dataPaths = context.getProjectDescriptor().dataManager.getDataPaths();
    final MavenProjectConfiguration projectConfig = JpsMavenExtensionService.getInstance().getMavenProjectConfiguration(dataPaths);
    final MavenModuleResourceConfiguration config = target.getModuleResourcesConfiguration(dataPaths);
    if (config == null) {
      return;
    }

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    final MavenResourceFileProcessor fileProcessor = new MavenResourceFileProcessor(projectConfig, target.getModule().getProject(), config);
=======
    final Map<MavenResourceRootDescriptor, List<File>> files = new HashMap<MavenResourceRootDescriptor, List<File>>();

>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    holder.processDirtyFiles(new FileProcessor<MavenResourceRootDescriptor, MavenResourcesTarget>() {

      @Override
      public boolean apply(MavenResourcesTarget t, File file, MavenResourceRootDescriptor rd) throws IOException {
        assert target == t;

        List<File> fileList = files.get(rd);
        if (fileList == null) {
          fileList = new ArrayList<File>();
          files.put(rd, fileList);
        }

        fileList.add(file);
        return true;
      }
    });

    MavenResourceRootDescriptor[] roots = files.keySet().toArray(new MavenResourceRootDescriptor[files.keySet().size()]);
    Arrays.sort(roots, new Comparator<MavenResourceRootDescriptor>() {
      @Override
      public int compare(MavenResourceRootDescriptor r1, MavenResourceRootDescriptor r2) {
        int res = r1.getIndexInPom() - r2.getIndexInPom();

        if (r1.isOverwrite()) {
          assert r2.isOverwrite(); // 'overwrite' parameters is common for all roots in module.

          return res;
        }

        if (r1.getConfiguration().isFiltered && !r2.getConfiguration().isFiltered) return 1;
        if (!r1.getConfiguration().isFiltered && r2.getConfiguration().isFiltered) return -1;

        if (!r1.getConfiguration().isFiltered) {
          res = -res;
        }

        return res;
      }
    });

    MavenResourceFileProcessor fileProcessor = new MavenResourceFileProcessor(projectConfig, target.getModule().getProject(), config);

    for (MavenResourceRootDescriptor rd : roots) {
      for (File file : files.get(rd)) {

        String relPath = FileUtil.getRelativePath(rd.getRootFile(), file);
        if (relPath == null) {
          continue;
        }
        final File outputDir = MavenResourcesTarget.getOutputDir(target.getModuleOutputDir(), rd.getConfiguration(), config.outputDirectory);
        if (outputDir == null) {
          continue;
        }
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
        final File outputFile = new File(outputDir, relPath);
        final String sourcePath = file.getPath();
=======
        File outputFile = new File(outputDir, relPath);
        String sourcePath = file.getPath();
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
        try {
          context.processMessage(new ProgressMessage("Copying resources... [" + target.getModule().getName() + "]"));

          fileProcessor.copyFile(file, outputFile, rd.getConfiguration(), context, FileUtilRt.ALL_FILES);
          outputConsumer.registerOutputFile(outputFile, Collections.singleton(sourcePath));
        }
        catch (UnsupportedEncodingException e) {
          context.processMessage(
            new CompilerMessage(BUILDER_NAME, BuildMessage.Kind.INFO, "Resource was not copied: " + e.getMessage(), sourcePath));
        }
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
        return !context.getCancelStatus().isCanceled();
      }
    });
=======

        if (context.getCancelStatus().isCanceled()) {
          return;
        }
      }
    }
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

    context.checkCanceled();

    context.processMessage(new ProgressMessage(""));
  }

  @NotNull
  public String getPresentableName() {
    return BUILDER_NAME;
  }
}
