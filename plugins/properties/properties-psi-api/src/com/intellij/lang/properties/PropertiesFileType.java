/*
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
 * Copyright 2000-2009 JetBrains s.r.o.
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
package com.intellij.lang.properties;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.encoding.EncodingRegistry;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.nio.charset.Charset;

/**
 * @author max
 */
public class PropertiesFileType extends LanguageFileType {
  public static final LanguageFileType INSTANCE = new PropertiesFileType();
  @NonNls public static final String DEFAULT_EXTENSION = "properties";
  @NonNls public static final String DOT_DEFAULT_EXTENSION = "."+DEFAULT_EXTENSION;

  private PropertiesFileType() {
    super(PropertiesLanguage.INSTANCE);
  }

  @Override
  @NotNull
  public String getName() {
    return "Properties";
  }

  @Override
  @NotNull
  public String getDescription() {
    return PropertiesBundle.message("properties.files.file.type.description");
  }

  @Override
  @NotNull
  public String getDefaultExtension() {
    return DEFAULT_EXTENSION;
  }

  @Override
  public Icon getIcon() {
    return AllIcons.FileTypes.Properties;
  }

  @Override
  public String getCharset(@NotNull VirtualFile file, final byte[] content) {
=======
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
package com.intellij.lang.properties;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.encoding.EncodingRegistry;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.nio.charset.Charset;

/**
 * @author max
 */
public class PropertiesFileType extends LanguageFileType {
  public static final LanguageFileType INSTANCE = new PropertiesFileType();
  @NonNls public static final String DEFAULT_EXTENSION = "properties";
  @NonNls public static final String DOT_DEFAULT_EXTENSION = "."+DEFAULT_EXTENSION;

  private PropertiesFileType() {
    super(PropertiesLanguage.INSTANCE);
  }

  @Override
  @NotNull
  public String getName() {
    return "Properties";
  }

  @Override
  @NotNull
  public String getDescription() {
    return PropertiesBundle.message("properties.files.file.type.description");
  }

  @Override
  @NotNull
  public String getDefaultExtension() {
    return DEFAULT_EXTENSION;
  }

  @Override
  public Icon getIcon() {
    return AllIcons.FileTypes.Properties;
  }

  @Override
  public String getCharset(@NotNull VirtualFile file, @NotNull final byte[] content) {
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    Charset charset = EncodingRegistry.getInstance().getDefaultCharsetForPropertiesFiles(file);
    if (charset == null) {
      charset = CharsetToolkit.getDefaultSystemCharset();
    }
    return charset != null ? charset.name() : null;
  }
}
