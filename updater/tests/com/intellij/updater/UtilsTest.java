/*
 * Copyright (C) 2013 The Android Open Source Project
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

import junit.framework.TestCase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class UtilsTest extends TestCase {

  private boolean mIsWindows;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    mIsWindows = System.getProperty("os.name").startsWith("Windows");
  }

  public void testDelete() throws Exception {
    File f = File.createTempFile("test", "tmp");
    assertTrue(f.exists());

    try {
      Utils.delete(f);
      assertFalse(f.exists());
    } finally {
      f.deleteOnExit();
    }
  }

  public void testDelete_LockedFile() throws Exception {
    File f = File.createTempFile("test", "tmp");
    assertTrue(f.exists());

    long millis = 0;
    FileWriter fw = new FileWriter(f);
    try {
      // This locks the file on Windows, preventing it from being deleted.
      // Utils.delete() will retry for about 100 ms.
      fw.write("test");
      millis = System.currentTimeMillis();

      Utils.delete(f);

    } catch (IOException e) {
      millis = System.currentTimeMillis() - millis;
      assertEquals("Cannot delete file " + f.getAbsolutePath(), e.getMessage());
      assertTrue("Utils.delete took " + millis + " ms, which is less than the expected 100 ms.", millis > 100);
      return;

    } finally {
      f.deleteOnExit();
      fw.close();
    }

    assertFalse("Utils.delete did not fail with the expected IOException on Windows.", mIsWindows);
  }

  public void testDeleteWithTimeout() throws Exception {
    File f = File.createTempFile("test", "tmp");
    assertTrue(f.exists());

    try {
      Utils.deleteWithTimeout(f, 1000);
      assertFalse(f.exists());
    } finally {
      f.deleteOnExit();
    }
  }

  public void testDeleteLockedFile_LockedFile() throws Exception {
    File f = File.createTempFile("test", "tmp");
    assertTrue(f.exists());

    long millis = 0;
    FileWriter fw = new FileWriter(f);
    try {
      // This locks the file on Windows, preventing it from being deleted.
      fw.write("test");
      millis = System.currentTimeMillis();
      Utils.deleteWithTimeout(f, 1000);

    } catch (IOException e) {
      millis = System.currentTimeMillis() - millis;
      String expected = String.format("Cannot delete file %s in %.1f s", f.getAbsolutePath(), millis / 1000.f);
      assertEquals(expected, e.getMessage());
      assertTrue("Utils.deleteWithTimeout took " + millis + " ms, which is less than the expected 1000 ms.", millis > 1000);
      return;

    } finally {
      f.deleteOnExit();
      fw.close();
    }

    assertFalse("Utils.deleteWithTimeout did not fail with the expected IOException on Windows.", mIsWindows);
  }

  public void testDeleteLockedFile_DelayedFile() throws Exception {
    File f = File.createTempFile("test", "tmp");
    assertTrue(f.exists());

    long millis = 0;
    final FileWriter fw = new FileWriter(f);
    try {
      // This locks the file on Windows, preventing it from being deleted.
      // Utils.delete() will retry for about 100 ms.
      fw.write("test");
      new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            Thread.sleep(500);
          } catch (InterruptedException ignore) {}

          try {
            fw.close();
          } catch (IOException ignore) {}
        }
      }).start();

      millis = System.currentTimeMillis();
      Utils.deleteWithTimeout(f, 1000);

    } finally {
      millis = System.currentTimeMillis() - millis;
      // On Windows we know this will lock for 500 ms. On Linux or Mac we don't make this assumption.
      if (mIsWindows) {
        assertTrue("Utils.deleteWithTimeout took " + millis + " ms, which is not in the expected 500-1000 ms range.",
                   millis > 500 && millis < 1000);
      } else {
        assertTrue("Utils.deleteWithTimeout took " + millis + " ms, which is more than the expected 500-1000 ms range.",
                   millis < 1000);
      }

      f.deleteOnExit();
      fw.close();
    }
  }

  public void testDeleteLockedFile_DelayedDirectory() throws Exception {
    List<File> files = new LinkedList<File>();

    File root = createTempDir(files, null);
    files.add(0, File.createTempFile("test", "tmp", root));
    files.add(0, File.createTempFile("test", "tmp", root));
    files.add(0, File.createTempFile("test", "tmp", root));

    File dir = createTempDir(files, root);
    files.add(0, File.createTempFile("test", "tmp", dir));

    dir = createTempDir(files, root);
    files.add(0, File.createTempFile("test", "tmp", dir));

    dir = createTempDir(files, root);
    files.add(0, File.createTempFile("test", "tmp", dir));
    dir = createTempDir(files, dir);
    File f = File.createTempFile("test", "tmp", dir);

    assertTrue(f.exists());

    long millis = 0;
    final FileWriter fw = new FileWriter(f);
    try {
      // This locks the file on Windows, preventing it from being deleted.
      // Utils.delete() will retry for about 100 ms.
      fw.write("test");
      new Thread(new Runnable() {
        @Override
        public void run() {
          try {
            Thread.sleep(500);
          } catch (InterruptedException ignore) {}

          try {
            fw.close();
          } catch (IOException ignore) {}
        }
      }).start();

      millis = System.currentTimeMillis();
      Utils.deleteWithTimeout(root, 1000);

    } finally {
      millis = System.currentTimeMillis() - millis;
      // On Windows we know this will lock for 500 ms. On Linux or Mac we don't make this assumption.
      if (mIsWindows) {
        assertTrue("Utils.deleteWithTimeout took " + millis + " ms, which is not in the expected 500-1000 ms range.",
                   millis > 500 && millis < 1000);
      } else {
        assertTrue("Utils.deleteWithTimeout took " + millis + " ms, which is more than the expected 500-1000 ms range.",
                   millis < 1000);
      }
    }
  }

  public static File createTempDir(List<File> files, File parent) throws IOException {
    File d = File.createTempFile("test", "dir", parent);
    if (!d.delete()) {
      throw new IOException("Failed to delete temp dir " + d.getAbsolutePath());
    }
    if (!d.mkdirs()) {
      throw new IOException("Failed to mkdir " + d.getAbsolutePath());
    }
    files.add(0, d);
    return d;
  }
}
