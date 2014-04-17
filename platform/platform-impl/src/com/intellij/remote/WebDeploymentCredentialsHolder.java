/*
 * Copyright 2000-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.remote;

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

/**
 * @author traff
 */
public class WebDeploymentCredentialsHolder {
  public static final String WEB_SERVER_CONFIG_ID = "WEB_SERVER_CONFIG_ID";

  private String myWebServerConfigId;
  private final RemoteCredentialsHolder myRemoteCredentials = new RemoteCredentialsHolder();


  public WebDeploymentCredentialsHolder() {
  }

  public WebDeploymentCredentialsHolder(@NotNull String webServerConfigId, @NotNull RemoteCredentials remoteCredentials) {
    myWebServerConfigId = webServerConfigId;
    myRemoteCredentials.copyFrom(remoteCredentials);
  }

  @NotNull
  public String getWebServerConfigId() {
    return myWebServerConfigId;
  }

  public void setWebServerConfigId(@NotNull String webServerConfigId) {
    myWebServerConfigId = webServerConfigId;
  }

  public void load(Element element) {
    myRemoteCredentials.load(element);
    setWebServerConfigId(element.getAttributeValue(WEB_SERVER_CONFIG_ID));
  }

  public void save(Element element) {
    element.setAttribute(WEB_SERVER_CONFIG_ID, getWebServerConfigId());

    myRemoteCredentials.save(element);
  }

  public RemoteCredentials getSshCredentials() {
    return myRemoteCredentials;
  }
}
=======
import com.intellij.openapi.util.text.StringUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

/**
 * @author traff
 */
public class WebDeploymentCredentialsHolder {
  public static final String WEB_SERVER_CONFIG_ID = "WEB_SERVER_CONFIG_ID";
  public static final String WEB_SERVER_CONFIG_NAME = "WEB_SERVER_CONFIG_NAME";


  private String myWebServerConfigId;
  private final RemoteCredentialsHolder myRemoteCredentials = new RemoteCredentialsHolder();
  private String myWebServerConfigName;


  public WebDeploymentCredentialsHolder() {
  }

  public WebDeploymentCredentialsHolder(@NotNull String webServerConfigId, String name, @NotNull RemoteCredentials remoteCredentials) {
    myWebServerConfigId = webServerConfigId;
    myWebServerConfigName = name;
    myRemoteCredentials.copyFrom(remoteCredentials);
  }

  @NotNull
  public String getWebServerConfigId() {
    return myWebServerConfigId;
  }

  public void setWebServerConfigId(@NotNull String webServerConfigId) {
    myWebServerConfigId = webServerConfigId;
  }

  public String getWebServerConfigName() {
    return myWebServerConfigName;
  }

  public void setWebServerConfigName(@NotNull String name) {
    myWebServerConfigName = name;
  }

  public void load(Element element) {
    myRemoteCredentials.load(element);
    setWebServerConfigId(element.getAttributeValue(WEB_SERVER_CONFIG_ID));
    setWebServerConfigName(StringUtil.notNullize(element.getAttributeValue(WEB_SERVER_CONFIG_NAME)));
  }

  public void save(Element element) {
    element.setAttribute(WEB_SERVER_CONFIG_ID, getWebServerConfigId());
    element.setAttribute(WEB_SERVER_CONFIG_NAME, getWebServerConfigName());

    myRemoteCredentials.save(element);
  }

  public RemoteCredentials getSshCredentials() {
    return myRemoteCredentials;
  }
}

>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
