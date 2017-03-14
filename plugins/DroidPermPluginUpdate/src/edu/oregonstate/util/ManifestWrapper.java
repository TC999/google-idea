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

package edu.oregonstate.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * Handles Vectors of permission and permission group data
 */

public class ManifestWrapper {

    public static Vector<String> permissionGroups = new Vector<>();
    public static Vector<String> permissions = new Vector<>();
    public static Vector<String> permissionsAndGroups = new Vector<>();

    public ManifestWrapper() {
        permissionGroups.clear();
        permissions.clear();
        permissionsAndGroups.clear();

        Field[] permission_groupFields = Manifest.permission_group.class.getDeclaredFields();
        Field[] permissionFields = Manifest.permission.class.getDeclaredFields();

        permissionGroups.clear();
        permissions.clear();
        permissionsAndGroups.clear();

        permissionGroups.addAll(Arrays.stream(permission_groupFields)
                .filter(field -> field.getType().equals(String.class))
                .map(Field::getName)
                .collect(Collectors.toList()));

        permissions.addAll(Arrays.stream(permissionFields)
                .filter(field -> field.getType().equals(String.class))
                .map(Field::getName)
                .collect(Collectors.toList()));

        permissionsAndGroups.addAll(permissions);
        permissionsAndGroups.addAll(permissionGroups);
    }
}