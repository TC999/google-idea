package edu.oregonstate.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * @author Nicholas Nelson <nelsonni@oregonstate.edu> Created on on 4/15/16.
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