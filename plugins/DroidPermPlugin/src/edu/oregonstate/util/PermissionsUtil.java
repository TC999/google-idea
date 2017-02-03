package edu.oregonstate.util;

import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiIfStatement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiSwitchStatement;
import com.intellij.psi.util.InheritanceUtil;
import edu.oregonstate.settings.NotificationsManager;
import edu.oregonstate.settings.PersistentSettings;
import edu.oregonstate.templates.BaseTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static edu.oregonstate.util.CallbackModel.*;
import static java.util.AbstractMap.Entry;

/**
 * @author Nicholas Nelson <nelsonni@oregonstate.edu> Created on on 7/10/16.
 */
public class PermissionsUtil {

    /**
     * Evaluates the psiClass to determine the Android base class and returns an instantiation of the corresponding
     * templates, including a model flag for the existence and branching logic contained within any
     * 'onRequestPermissionsResult' callback method found within the psiClass.
     *
     * @param permission     the Android permission to be guarded.
     * @param psiClass       the PsiClass instance to evaluate for a templates target.
     * @param selectionModel the SelectionModel containing the target text and contextual information.
     * @return the templates targeted at the specific Android base class with context model options set
     */
    @Nullable
    public static <T extends BaseTemplate> T getTemplate(@NotNull String permission, @NotNull PsiClass psiClass,
                                                         @NotNull SelectionModel selectionModel) {
        final String targetText = selectionModel.getSelectedText();
        final PsiMethod targetMethod =
                PluginUtil.getContainingMethod(psiClass.getContainingFile(), selectionModel.getSelectionStart());
        assert targetMethod != null;
        final String methodSignature = PluginUtil.getMethodSignature(targetMethod);

        final Pair<String, Integer> requestCodeField = CallbackUtil.getRequestCodeField(psiClass, permission);
        final CallbackModel callbackModel = getCallbackModel(psiClass);
        final String templateName = getQualifiedTemplateName(psiClass);

        try {
            Class<?> templateCls = Class.forName(templateName);
            Constructor<?> templateConstructor = templateCls.getConstructor(String.class, String.class, String.class,
                    String.class, String.class, Integer.class, CallbackModel.class);
            return (T) templateConstructor.newInstance(permission, psiClass.getName(), methodSignature,
                    targetText, requestCodeField.getFirst(), requestCodeField.getSecond(), callbackModel);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException
                | IllegalAccessException | InvocationTargetException e) {
            NotificationsManager.getInstance(psiClass.getProject()).templateLoadError(templateName);
            return null;
        }
    }

    public static <T extends BaseTemplate> boolean templateContainsCallback(@NotNull T template){
        return !template.getCallbackMethod().equals("");
    }

    @NotNull
    private static CallbackModel getCallbackModel(PsiClass psiClass) {
        final Optional<PsiMethod> existingCallback = CallbackUtil.getExistingCallback(psiClass);

        if (existingCallback.isPresent()) {
            Optional<PsiSwitchStatement> requestCodeSwitchElement =
                    CallbackUtil.getRequestCodeSwitchElement(existingCallback.get());
            if (requestCodeSwitchElement.isPresent()) {
                return SWITCH_BLOCK;
            }
            Optional<PsiIfStatement> requestCodeIfElement = CallbackUtil.getRequestCodeIfElement(existingCallback.get());
            if (requestCodeIfElement.isPresent()) {
                return IF_BLOCK;
            }
            return NONE;
        } else {
            // CallbackModel will revert to NONE if the selected templates does not define a callback method
            return NEW_METHOD;
        }
    }

    @Nullable
    private static String getQualifiedTemplateName(PsiClass psiClass) {
        Optional<Entry<String, String>> templateEntry =
                PersistentSettings.getInstance().templates.entrySet().stream()
                        .filter(t -> InheritanceUtil.isInheritor(psiClass, t.getKey())).findFirst();

        if (templateEntry.isPresent()) {
            return templateEntry.get().getValue();
        }
        return "edu.oregonstate.templates.DefaultTemplate";
    }

    @Nullable
    public static String getTemplateName(PsiClass psiClass) {
        Optional<Entry<String, String>> templateEntry =
                PersistentSettings.getInstance().templates.entrySet().stream()
                        .filter(t -> InheritanceUtil.isInheritor(psiClass, t.getKey())).findFirst();

        if (templateEntry.isPresent()) {
            String templateName = templateEntry.get().getValue();
            return templateName.substring(templateName.lastIndexOf(".") + 1).trim();
        }
        return "DefaultTemplate";
    }

}
