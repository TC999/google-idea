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

import com.intellij.codeInspection.reference.RefMethod;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import edu.oregonstate.dp.inspect.jaxb.JaxbCallback;

import java.util.List;

/**
 * DP-Detect side of DroidPerm - Unused
 * We may use DP-Detect in the future but for now it is not mature enough to be used with DP-Transform
 */

class DroidPermDataUtil {

    /**
     * @return The callback corresponding to this method or null if no corresponding callback was found.
     */
    static JaxbCallback getCallbackFor(RefMethod method, List<JaxbCallback> data) {
        if (data == null) return null;
        return data.stream().filter(callback -> sameMethod(method, callback)).findAny().orElseGet(() -> null);
    }

    private static boolean sameMethod(RefMethod method, JaxbCallback callback) {
        //todo support anonymous classes, for them getQualifiedName() is null
        assert method.getOwnerClass().getElement() != null;
        return callback.getDeclaringClass().equals(method.getOwnerClass().getElement().getQualifiedName())
                && getSigNoReturnType(callback).equals(getSubSignature(method));
    }

    private static String getSigNoReturnType(JaxbCallback callback) {
        int spaceIndex = callback.getSignature().indexOf(' ');
        return callback.getSignature().substring(spaceIndex + 1);
    }

    private static String getSubSignature(RefMethod refMethod) {
        if (!(refMethod.getElement() instanceof PsiMethod)) {
            return null;
        }
        PsiMethod method = (PsiMethod) refMethod.getElement();
        StringBuilder sb = new StringBuilder();
        sb.append(method.getName()).append("(");
        boolean first = true;
        for (PsiParameter parameter : method.getParameterList().getParameters()) {
            if (!first) {
                sb.append(",");
                first = false;
            }
            assert parameter.getTypeElement() != null;
            sb.append(parameter.getTypeElement().getType().getCanonicalText());
        }
        sb.append(")");
        return sb.toString();
    }
}
