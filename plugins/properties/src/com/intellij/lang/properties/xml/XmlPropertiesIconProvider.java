package com.intellij.lang.properties.xml;

import com.intellij.ide.IconProvider;
import com.intellij.ide.highlighter.XmlFileType;
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
=======
import com.intellij.lang.properties.PropertiesImplUtil;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlFile;
import icons.PropertiesIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Dmitry Avdeev
 *         Date: 7/29/11
 */
public class XmlPropertiesIconProvider extends IconProvider {

  @Override
  public Icon getIcon(@NotNull PsiElement element, int flags) {
    return element instanceof XmlFile &&
           ((XmlFile)element).getFileType() == XmlFileType.INSTANCE &&
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
           XmlPropertiesFile.getPropertiesFile((XmlFile)element) != null ? PropertiesIcons.XmlProperties : null;
=======
           PropertiesImplUtil.getPropertiesFile((XmlFile)element) != null ? PropertiesIcons.XmlProperties : null;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }
}
