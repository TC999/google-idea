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
package com.intellij.internal.statistic.analytics;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.internal.statistic.StatisticsUploadAssistant;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.updateSettings.impl.UpdateChecker;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * This class mirrors some function of the Android plugin's UsageTracker class.
 * It is intended to be used from with the platform, which cannot have any dependencies on the android plugin.
 */
public class PlatformUsageTracker {
  // Tracking is enabled in internal builds
  private static final boolean DEBUG = ApplicationManager.getApplication().isInternal();

  @NonNls private static final String ANALYTICS_URL = "https://ssl.google-analytics.com/collect";
  @NonNls private static final String ANAYLTICS_ID = DEBUG ? "UA-44790371-1" : "UA-19996407-3";
  @NonNls private static final String ANALYTICS_APP = "Android Studio";

  private static final List<? extends NameValuePair> analyticsBaseData = ImmutableList
    .of(new BasicNameValuePair("v", "1"),
        new BasicNameValuePair("tid", ANAYLTICS_ID),
        new BasicNameValuePair("an", ANALYTICS_APP),
        new BasicNameValuePair("av", ApplicationInfo.getInstance().getFullVersion()),
        new BasicNameValuePair("cid", UpdateChecker.getInstallationUID(PropertiesComponent.getInstance())));

  public static boolean trackingEnabled() {
    return DEBUG || StatisticsUploadAssistant.isSendAllowed();
  }

  public static void trackException(Throwable t) {
    if (!DEBUG && !trackingEnabled()) {
      return;
    }

    t = getRootCause(t);
    post(ImmutableList.of(
      new BasicNameValuePair("t", "exception"),
      new BasicNameValuePair("exd", t.toString())));
  }

  private static void post(@NotNull final List<BasicNameValuePair> parameters) {
    ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
      @Override
      public void run() {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(ANALYTICS_URL);
        try {
          request.setEntity(new UrlEncodedFormEntity(Iterables.concat(analyticsBaseData, parameters)));
          HttpResponse response = client.execute(request);
          StatusLine status = response.getStatusLine();
          HttpEntity entity = response.getEntity(); // throw it away, don't care, not sure if we need to read in the response?
          if (status.getStatusCode() >= 300) {
            // something went wrong, fail quietly, we probably have to diagnose analytics errors on our side
            // usually analytics accepts ANY request and always returns 200
            // we don't want to call logging methods here
            if (DEBUG) {
              System.err.println("Error reporting to Analytics, return code: " + status.getStatusCode());
            }
          }
        }
        catch (IOException e) {
          // something went wrong, fail quietly
          // we don't want to call logging methods here
          if (DEBUG) {
            System.err.println("Error reporting to Analytics: " + e.getMessage());
          }
        }
        finally {
          HttpClientUtils.closeQuietly(client);
        }
      }
    });
  }

  // Similar to ExceptionUntil.getRootCause, but attempts to avoid infinite recursion
  private static Throwable getRootCause(Throwable e) {
    int depth = 0;
    while (depth++ < 20) {
      if (e.getCause() == null) return e;
      e = e.getCause();
    }
    return e;
  }
}
