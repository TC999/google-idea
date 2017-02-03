package edu.oregonstate.ui;

import com.intellij.analysis.AnalysisScope;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import edu.oregonstate.dp.inspect.DroidPermException;
import edu.oregonstate.dp.inspect.IntellijUtil;
import edu.oregonstate.settings.PersistentSettings;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.intellij.diff.merge.TextMergeTool.LOG;


/**
 * @author Nicholas Nelson <nelsonni@oregonstate.edu> Created on on 12/30/15.
 */
public class SettingsPanel {

    public JPanel panel;
    public JTextField droidPermHome;
    JList<String> templates;
    public JCheckBox notificationsCheckbox;
    private JLabel toolDirLabel;
    private JButton verifyExists;
    private JLabel templatesLabel;
    private JScrollPane templatesScrollPane;
    private JLabel instructLabel;
    private JButton moduleCheckButton;
    private JList<Path> pathsList;
    private JLabel pathsLabel;
    private JScrollPane pathsScrollPane;
    private JLabel currentModule;
    private JTextField moduleName;
    private JButton fileChooser;
    private JLabel notificationsLabel;

    public SettingsPanel() {
        Font newLabelFont=new Font(instructLabel.getFont().getName(),Font.ITALIC, instructLabel.getFont().getSize());
        instructLabel.setFont(newLabelFont);

        DefaultListModel<String> templateModel = new DefaultListModel<>();
        PersistentSettings.getInstance().templates.values().stream()
                .map(t -> t.substring(t.lastIndexOf(".") + 1).trim())
                .distinct()
                .forEach(templateModel::addElement);
        templates.setModel(templateModel);

        fileChooser.addActionListener(e -> {
            FileChooserDescriptor fcd = new FileChooserDescriptor(false, true, false, false, false, false);
            fcd.setTitle("Select DroidPermHome Directory");
            fcd.setHideIgnored(false);

            Project[] openProjects = ProjectManager.getInstance().getOpenProjects();
            assert openProjects.length != 0;
            Project project = openProjects[0];

            final VirtualFile baseDir = project.getBaseDir();
            final VirtualFile[] files = FileChooser.chooseFiles(fcd, project, baseDir);
            if (files.length == 0) {
                return;
            }
            final VirtualFile root = files[0];
            droidPermHome.setText(root.getCanonicalPath());
        });

        verifyExists.addActionListener(e -> {
            Path permPath = Paths.get(droidPermHome.getText() + "/droid-perm.jar");
            if (Files.isDirectory(Paths.get(droidPermHome.getText())) && Files.exists(permPath)) {
                Messages.showInfoMessage(panel, "Directory exists and contains 'droid-perm.jar'.", "Success");
            } else {
                Messages.showInfoMessage(panel, "Directory does not contain 'droid-perm.jar'.", "Failure");
            }
        });

        moduleCheckButton.addActionListener(e -> {
            DefaultListModel<Path> model = new DefaultListModel<>();

            try {
                Project[] openProjects = ProjectManager.getInstance().getOpenProjects();
                assert openProjects.length != 0;
                Project project = openProjects[0];

                AnalysisScope scope = new AnalysisScope(project);
                Module module = IntellijUtil.getModule(scope, project);
                if (module == null) {
                    Messages.showInfoMessage(panel, "Analysis scope does not contain module.", "APK Check");
                    LOG.warn("Analysis scope does not contain a module.");
                    return;
                }

                Path moduleDir = IntellijUtil.getModulePath(module);
                moduleName.setText(moduleDir.getFileName().toString());

                List<Path> matches = new ArrayList<>();
                Files.walk(moduleDir)
                        .filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".apk"))
                        .forEach(matches::add);

                for (Path p : matches) {
                    model.addElement(p.getFileName());
                }
                pathsList.setModel(model);

            } catch (DroidPermException e1) {
                LOG.error(e1);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }
}
