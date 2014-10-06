/*
 * Copyright (C) 2014 The Android Open Source Project
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
package com.intellij.updater;

import com.sun.jna.Pointer;
import com.sun.jna.StringArray;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class NativeFileManager {

  private static final int MAX_PROCESSES = 10;

  public static class Process {
    public final int pid;
    public final String name;

    public Process(int pid, String name) {
      this.pid = pid;
      this.name = name;
    }

    public boolean terminate() {
      Kernel32.HANDLE process = Kernel32.INSTANCE.OpenProcess(WinNT.PROCESS_TERMINATE | WinNT.SYNCHRONIZE, false, pid);
      if (process.getPointer() == null) {
        Runner.logger.warn("Unable to find process " + name + "(" + pid + ")");
        return false;
      } else {
        Kernel32.INSTANCE.TerminateProcess(process, 1);
        int wait = Kernel32.INSTANCE.WaitForSingleObject(process, 1000);
        if (wait != WinBase.WAIT_OBJECT_0) {
          Runner.logger.warn("Timed out while waiting for process " + name + "(" + pid + ") to end");
          return false;
        }
        Kernel32.INSTANCE.CloseHandle(process);
        return true;
      }
    }
  }

  public static List<Process> getProcessesUsing(File file) {
    List<Process> processes = new LinkedList<Process>();
    try {
      IntByReference session = new IntByReference();
      char[] sessionKey = new char[RestartManager.CCH_RM_SESSION_KEY + 1];
      int error = RestartManager.INSTANCE.RmStartSession(session, 0, sessionKey);
      if (error != 0) {
        Runner.logger.warn("Unable to start restart manager session");
        return processes;
      }
      StringArray resources = new StringArray(new WString[]{new WString(file.toString())});
      error = RestartManager.INSTANCE.RmRegisterResources(session.getValue(), 1, resources, 0, Pointer.NULL, 0, null);
      if (error != 0) {
        Runner.logger.warn("Unable to register restart manager resource " + file.getAbsolutePath());
        return processes;
      }

      IntByReference procInfoNeeded = new IntByReference();
      RestartManager.RmProcessInfo info = new RestartManager.RmProcessInfo();
      RestartManager.RmProcessInfo[] infos = (RestartManager.RmProcessInfo[])info.toArray(MAX_PROCESSES);
      IntByReference procInfo = new IntByReference(infos.length);
      error = RestartManager.INSTANCE.RmGetList(session.getValue(), procInfoNeeded, procInfo, info, new LongByReference());
      if (error != 0) {
        Runner.logger.warn("Unable to get the list of processes using " + file.getAbsolutePath());
        return processes;
      }
      for (int i = 0; i < procInfo.getValue(); i++) {
        processes.add(new Process(infos[i].Process.dwProcessId, new String(infos[i].strAppName).trim()));
      }
      RestartManager.INSTANCE.RmEndSession(session.getValue());
    } catch (Throwable t) {
      // Best effort approach, if no DLL is found ignore.
    }
    return processes;
  }
}
