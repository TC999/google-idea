/*
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
 * Copyright 2000-2013 JetBrains s.r.o.
=======
 * Copyright 2000-2014 JetBrains s.r.o.
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
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

// Generated on Wed Nov 07 17:26:02 MSK 2007
// DTD/Schema  :    plugin.dtd

package org.jetbrains.idea.devkit.dom;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.devkit.dom.impl.ExtensionNsConverter;

import java.util.List;

public interface Extensions extends DomElement {

  @NotNull
  @Attribute("defaultExtensionNs")
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  @Convert(value=ExtensionNsConverter.class, soft=true)
=======
  @Convert(value = ExtensionNsConverter.class, soft = true)
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  @Stubbed
  GenericAttributeValue<IdeaPlugin> getDefaultExtensionNs();

  @NotNull
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
  @Convert(value=ExtensionNsConverter.class, soft=true)
=======
  @Convert(value = ExtensionNsConverter.class, soft = true)
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  @Stubbed
  GenericAttributeValue<IdeaPlugin> getXmlns();

  List<Extension> getExtensions();

  Extension addExtension();

  Extension addExtension(String name);

  @NotNull
  String getEpPrefix();
}
