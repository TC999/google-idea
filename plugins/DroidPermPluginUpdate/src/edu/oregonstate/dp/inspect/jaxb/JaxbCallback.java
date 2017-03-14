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
import java.util.ArrayList;
import java.util.List;

/**
 * DP-Detect side of DroidPerm - Unused
 * We may use DP-Detect in the future but for now it is not mature enough to be used with DP-Transform
 */

@XmlRootElement(name = "callback")
public class JaxbCallback {

    private String declaringClass;
    private String signature;
    private List<JaxbStmt> stmts = new ArrayList<>();

    public JaxbCallback() {
    }

    @XmlAttribute
    public String getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(String declaringClass) {
        this.declaringClass = declaringClass;
    }

    @XmlAttribute
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @XmlElement(name = "statement")
    public List<JaxbStmt> getStmts() {
        return stmts;
    }

    @Override
    public String toString() {
        return declaringClass + ": " + signature;
    }
}
