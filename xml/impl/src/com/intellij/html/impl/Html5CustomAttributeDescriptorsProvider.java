/*
 * Copyright 2000-2010 JetBrains s.r.o.
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
package com.intellij.html.impl;

import com.intellij.html.index.Html5CustomAttributesIndex;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.xml.XmlAttributeDescriptor;
import com.intellij.xml.XmlAttributeDescriptorsProvider;
import com.intellij.xml.impl.schema.AnyXmlAttributeDescriptor;
import com.intellij.xml.util.HtmlUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Eugene.Kudelevsky
 */
public class Html5CustomAttributeDescriptorsProvider implements XmlAttributeDescriptorsProvider {
  @Override
  public XmlAttributeDescriptor[] getAttributeDescriptors(XmlTag tag) {
    if (tag == null || !HtmlUtil.isHtml5Context(tag)) {
      return XmlAttributeDescriptor.EMPTY;
    }
    final List<String> currentAttrs = new ArrayList<String>();
    for (XmlAttribute attribute : tag.getAttributes()) {
      currentAttrs.add(attribute.getName());
    }
    final List<XmlAttributeDescriptor> result = new ArrayList<XmlAttributeDescriptor>();
    final GlobalSearchScope scope = GlobalSearchScope.allScope(tag.getProject());
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    FileBasedIndex.getInstance().processAllKeys(Html5CustomAttributesIndex.INDEX_ID, new Processor<String>() {
      @Override
      public boolean process(String s) {
        final boolean canProcessKey = !FileBasedIndex.getInstance().processValues(Html5CustomAttributesIndex.INDEX_ID, s,
                                                                                  null, new FileBasedIndex.ValueProcessor<Void>() {
            @Override
            public boolean process(VirtualFile file, Void value) {
              return false;
            }
          }, scope);
        if (!canProcessKey) return true;

        boolean add = true;
        for (String attr : currentAttrs) {
          if (attr.startsWith(s)) {
            add = false;
=======
    final Collection<String> keys = FileBasedIndex.getInstance().getAllKeys(Html5CustomAttributesIndex.INDEX_ID, tag.getProject());
    for (String key : keys) {
      final boolean canProcessKey = !FileBasedIndex.getInstance().processValues(Html5CustomAttributesIndex.INDEX_ID, key,
                                                                                null, new FileBasedIndex.ValueProcessor<Void>() {
          @Override
          public boolean process(VirtualFile file, Void value) {
            return false;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
          }
        }, scope);
      if (!canProcessKey) continue;

      boolean add = true;
      for (String attr : currentAttrs) {
        if (attr.startsWith(key)) {
          add = false;
        }
      }
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    }, scope, null);
=======
      if (add) {
        result.add(new AnyXmlAttributeDescriptor(key));
      }
    }
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)

    return result.toArray(new XmlAttributeDescriptor[result.size()]);
  }

  @Override
  public XmlAttributeDescriptor getAttributeDescriptor(String attributeName, XmlTag context) {
    if (context != null && HtmlUtil.isHtml5Context(context) && HtmlUtil.isCustomHtml5Attribute(attributeName)) {
      return new AnyXmlAttributeDescriptor(attributeName);
    }
    return null;
  }

}
