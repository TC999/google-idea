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

/**
 * Template for classes that are children / grandchildren etc.. of android.app.Activity
 * Contains code blocks used for permission guard insertion
 * Android views only support permission checks
 */

class ViewTemplate extends BaseTemplate {

    ViewTemplate(String permission, String contextClass, String contextMethodSignature,
                 String targetText, String requestCodeName, Integer requestCodeValue, CallbackModel model, String developerContext) {
        super(permission, contextClass, contextMethodSignature, targetText, requestCodeName, requestCodeValue, model, developerContext)
    }

    @Override
    protected String toCheckVersionBlockBefore() {
        def block =
        """if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {"""
        return block
    }

    @Override
    protected String toCheckOnlyBlock() {
        def block =
        """
        if (android.support.v4.app.ActivityCompat.checkSelfPermission(this.getContext(), ${qualifiedPermissionName})
            == android.content.pm.PackageManager.PERMISSION_GRANTED) {"""
        return block
    }

    @Override
    protected String toCheckRequestBlock() {
        return """"""
    }

    @Override
    protected String toCheckVersionElse() {
        def block =
        """
        }
        } else {
        """
        return block
    }


    @Override
    protected String toCheckVersionBlockAfter() {
        def block =
        """
        }
        """
        return block
    }

    @Override
    protected String toCallbackMethod() {
        // android.view.View does not have guaranteed access to a running UI thread
        return """"""
    }

    @Override
    protected String toCallbackIfBranch() {
        // android.view.View does not have guaranteed access to a running UI thread
        return """"""
    }

    @Override
    protected String toCallbackSwitchBranch() {
        // android.view.View does not have guaranteed access to a running UI thread
        return """"""
    }

}
