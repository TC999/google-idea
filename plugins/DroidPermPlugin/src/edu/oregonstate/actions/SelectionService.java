package edu.oregonstate.actions;

import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

/**
 * @author Nicholas Nelson <nelsonni@oregonstate.edu> Created on on 8/17/16.
 */
public class SelectionService implements ApplicationComponent {

    private static SelectionService instance = null;
    public boolean selectionComplete;
    public boolean insertRequest;
    public String permissionRequested;

    protected SelectionService() {
        // defeat instantiation
    }

    public static SelectionService getInstance() {
        if (instance == null) {
            instance = new SelectionService();
        }
        return instance;
    }

    private void clear() {
        selectionComplete = false;
        insertRequest = false;
        permissionRequested = null;
    }

    @Override
    public void initComponent() {
        clear();
    }

    @Override
    public void disposeComponent() {
        clear();
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "SelectionService";
    }
}
