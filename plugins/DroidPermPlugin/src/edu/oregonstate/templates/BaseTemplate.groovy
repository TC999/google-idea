package edu.oregonstate.templates

import edu.oregonstate.util.CallbackModel
import edu.oregonstate.util.ManifestWrapper

/**
 * @author Nicholas Nelson <nelsonni@oregonstate.edu> Created on on 7/11/16.
 */
abstract class BaseTemplate {

    String permissionName
    String qualifiedPermissionName
    String requestCodeName
    String requestCodeValue
    String requestCodeDecl
    String contextClassName
    String contextMethodSignature
    String targetText
    String checkBlockBefore
    String checkBlockAfter
    String checkBlockElse
    CallbackModel model
    String callbackMethod
    String callbackIfBranch
    String callbackSwitchBranch

    BaseTemplate(String permission, String contextClass, String contextMethodSignature,
                 String targetText, String requestCodeName, Integer requestCodeValue, CallbackModel model) {
        this.permissionName = permission.toUpperCase()
        this.qualifiedPermissionName = toQualifiedPermission(permission)
        this.requestCodeName = requestCodeName
        this.requestCodeValue = requestCodeValue
        this.requestCodeDecl = toRequestCodeDecl()
        this.contextClassName = contextClass
        this.contextMethodSignature = contextMethodSignature
        this.targetText = targetText
        this.checkBlockBefore = toCheckBlockBefore()
        this.checkBlockAfter = toCheckBlockAfter()
        this.checkBlockElse = toCheckBlockElse()
        this.model = toCallbackModel(model)
        this.callbackMethod = toCallbackMethod()
        this.callbackIfBranch = toCallbackIfBranch()
        this.callbackSwitchBranch = toCallbackSwitchBranch()
    }

    private static def toQualifiedPermission(String permission) {
        if (permission == null) throw new InputMismatchException("Requested permission provided to templates is null")
        permission = permission.split("\\.").last().toUpperCase()
        new ManifestWrapper();
        if (ManifestWrapper.permissions.contains(permission)) {
            return "android.Manifest.permission.${permission}"
        } else if (ManifestWrapper.permissionGroups.contains(permission)) {
            return "android.Manifest.permission_group.${permission}"
        } else {
            throw new InputMismatchException("Requested permission not found in android.Manifest")
        }
    }

    private def toRequestCodeDecl() {
        return "private static final int ${requestCodeName} = ${requestCodeValue};"
    }

    private def CallbackModel toCallbackModel(CallbackModel model) {
        if (toCallbackMethod() == "") {
            return CallbackModel.NONE;
        } else {
            return model;
        }
    }

    protected abstract def String toCheckBlockBefore()
    protected abstract def String toCheckBlockAfter()
    protected abstract def String toCheckBlockElse()
    protected abstract def String toCallbackMethod()
    protected abstract def String toCallbackIfBranch()
    protected abstract def String toCallbackSwitchBranch()
}
