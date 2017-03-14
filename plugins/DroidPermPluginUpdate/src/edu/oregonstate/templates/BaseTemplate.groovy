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

package edu.oregonstate.templates

import edu.oregonstate.util.CallbackModel
import edu.oregonstate.util.ManifestWrapper

/**
 * Abstract parent class for other groovy templates
 * contains getters for template code blocks
 * handles converting permission string to fully qualified permission string
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
    String developerContext
    String checkVersionBlockBefore
    String checkOnlyBlock
    String checkRequestBlock
    String checkVersionElse
    String checkVersionBlockAfter
    CallbackModel model
    String callbackMethod
    String callbackIfBranch
    String callbackSwitchBranch

    BaseTemplate(String permission, String contextClass, String contextMethodSignature,
                 String targetText, String requestCodeName, Integer requestCodeValue, CallbackModel model, String developerContext) {
        this.permissionName = permission.toUpperCase()
        this.qualifiedPermissionName = toQualifiedPermission(permission)
        this.developerContext = developerContext
        this.requestCodeName = requestCodeName
        this.requestCodeValue = requestCodeValue
        this.requestCodeDecl = toRequestCodeDecl()
        this.contextClassName = contextClass
        this.contextMethodSignature = contextMethodSignature
        this.targetText = targetText
        this.checkVersionBlockBefore = toCheckVersionBlockBefore()
        this.checkOnlyBlock = toCheckOnlyBlock()
        this.checkRequestBlock = toCheckRequestBlock()
        this.checkVersionElse = toCheckVersionElse()
        this.checkVersionBlockAfter = toCheckVersionBlockAfter()
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

    protected abstract def String toCheckVersionBlockBefore()
    protected abstract def String toCheckOnlyBlock()
    protected abstract def String toCheckRequestBlock()
    protected abstract def String toCheckVersionElse()
    protected abstract def String toCheckVersionBlockAfter()
    //protected abstract def String toCheckBlockBefore()
    //protected abstract def String toCheckBlockAfter()
    //protected abstract def String toCheckBlockElse()
    protected abstract def String toCallbackMethod()
    protected abstract def String toCallbackIfBranch()
    protected abstract def String toCallbackSwitchBranch()
}
