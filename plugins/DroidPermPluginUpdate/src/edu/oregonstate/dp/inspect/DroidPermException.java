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

package edu.oregonstate.dp.inspect;

/**
 * DP-Detect side of DroidPerm - Unused
 * We may use DP-Detect in the future but for now it is not mature enough to be used with DP-Transform
 */

@SuppressWarnings("WeakerAccess")
public class DroidPermException extends Exception {

    public DroidPermException(String message) {
        super(message);
    }

    public DroidPermException(String message, Throwable cause) {
        super(message, cause);
    }

    public DroidPermException(Throwable cause) {
        super(cause);
    }

    public DroidPermException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
