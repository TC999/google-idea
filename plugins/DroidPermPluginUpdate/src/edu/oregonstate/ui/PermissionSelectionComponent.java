/*
 * Copyright (C) 2015 The Android Open Source Project
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

package edu.oregonstate.ui;

import edu.oregonstate.actions.SelectionService;
import edu.oregonstate.util.ManifestWrapper;

import javax.swing.*;

/**
 * Permission Selection menu
 * Allows user to select any permission from the permission Manifest
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
