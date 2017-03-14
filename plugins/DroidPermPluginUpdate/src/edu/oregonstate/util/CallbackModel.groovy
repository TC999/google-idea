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

package edu.oregonstate.util

/**
 * Callback Enumeration used for classification of current onRequestPermission
 * callbacks - if any - in class under transformation
 */

enum CallbackModel {
    NONE,           // no callback allowed in this context
    NEW_METHOD,     // a new callback method is required
    IF_BLOCK,       // a new if branch in an existing callback method is required
    SWITCH_BLOCK    // a new switch branch in an existing callback method is required
}