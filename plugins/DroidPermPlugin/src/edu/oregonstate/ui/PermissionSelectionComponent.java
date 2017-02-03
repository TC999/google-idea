package edu.oregonstate.ui;

import edu.oregonstate.actions.SelectionService;
import edu.oregonstate.util.ManifestWrapper;

import javax.swing.*;

/**
 * @author Nicholas Nelson <nelsonni@oregonstate.edu> Created on on 8/18/16.
 */
public class PermissionSelectionComponent extends JDialog {
    private JPanel contentPane;
    private JCheckBox callbackOption;
    private JList permissionsList;
    private JScrollPane permissionsScrollPane;
    private static boolean callbackAvailable;

    @SuppressWarnings("unchecked")
    PermissionSelectionComponent(boolean callbackAvailable) {
        PermissionSelectionComponent.callbackAvailable = callbackAvailable;
        callbackOption.setEnabled(callbackAvailable);
        callbackOption.setSelected(callbackAvailable);
        setContentPane(contentPane);
        setModal(true);
        permissionsScrollPane.getBorder();
        permissionsList.setListData(ManifestWrapper.permissions);
        permissionsList.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        PermissionSelectionComponent dialog = new PermissionSelectionComponent(callbackAvailable);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    void saveResults() {
        SelectionService service = SelectionService.getInstance();
        service.permissionRequested = (String) permissionsList.getSelectedValue();
        service.insertRequest = callbackOption.isSelected();
    }
}
