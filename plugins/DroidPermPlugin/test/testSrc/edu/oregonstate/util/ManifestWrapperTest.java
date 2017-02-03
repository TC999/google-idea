package edu.oregonstate.util;

import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created by Jacob on 1/16/2017.
 */
public class ManifestWrapperTest {

    private static final int NUMBER_PERMISSIONS = 139;
    private static final int NUMBER_PERMISSIONGROUPS = 9;

    @Test
    public void testNumberPermissions(){
        ManifestWrapper manifestWrapper = new ManifestWrapper();

        assertEquals(NUMBER_PERMISSIONS, manifestWrapper.permissions.size());
        assertEquals(NUMBER_PERMISSIONGROUPS, manifestWrapper.permissionGroups.size());
    }

}
