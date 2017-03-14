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

package edu.oregonstate.dp.inspect.jaxb;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * DP-Detect side of DroidPerm - Unused
 * We may use DP-Detect in the future but for now it is not mature enough to be used with DP-Transform
 */

@XmlRootElement
public class JaxbStmt {

    private String callClass;
    private String callSignature;
    private int line;

    /**
     * key = permission
     * <p>
     * value: true = checked, false = not checked
     */
    private Map<String, Boolean> permissionStatusMap;

    public JaxbStmt() {
    }

    @XmlAttribute
    public String getCallClass() {
        return callClass;
    }

    public void setCallClass(String callClass) {
        this.callClass = callClass;
    }

    @XmlAttribute
    public String getCallSignature() {
        return callSignature;
    }

    public String getCallFullSignature() {
        return ("<" +
                callClass +
                ": " +
                callSignature +
                ">")
                .intern();
    }

    public void setCallSignature(String callSignature) {
        this.callSignature = callSignature;
    }

    @XmlAttribute
    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public boolean allGuarded() {
        return permissionStatusMap.values().stream().allMatch(guarded -> guarded);
    }

    /**
     * Map from permission to guarded status.
     */
    @XmlElement(name = "permission")
    public Map<String, Boolean> getPermissionStatusMap() {
        return permissionStatusMap;
    }

    public List<String> getPermDisplayStrings() {
        String prefix = "android.permission.";
        int prefixLen = prefix.length();
        return permissionStatusMap.keySet().stream().map(perm ->
                (perm.startsWith(prefix) ? perm.substring(prefixLen) : prefix)
                        + (permissionStatusMap.get(perm) ? "" : " (no check)"))
                .collect(Collectors.toList());
    }

    public void setPermissionStatusMap(Map<String, Boolean> permissionStatusMap) {
        this.permissionStatusMap = permissionStatusMap;
    }

    public List<String> getUncheckedPermissions() {
        return permissionStatusMap.keySet().stream().filter(perm -> !permissionStatusMap.get(perm))
                .collect(Collectors.toList());
    }
}
