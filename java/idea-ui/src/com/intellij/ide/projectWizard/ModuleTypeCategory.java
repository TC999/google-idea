package com.intellij.ide.projectWizard;

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.ModuleType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dmitry Avdeev
 *         Date: 20.09.13
 */
public class ModuleTypeCategory extends ProjectCategory {

  private final ModuleType myModuleType;

  public ModuleTypeCategory(ModuleType moduleType) {
    myModuleType = moduleType;
  }

  @NotNull
  @Override
  public final ModuleBuilder createModuleBuilder() {
    return myModuleType.createModuleBuilder();
  }

  public static class Java extends ModuleTypeCategory {

    public Java() {
      super(JavaModuleType.getModuleType());
=======
import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.ModuleType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Dmitry Avdeev
 *         Date: 20.09.13
 */
public class ModuleTypeCategory extends ProjectCategory {

  private final ModuleType myModuleType;

  public ModuleTypeCategory(ModuleType moduleType) {
    myModuleType = moduleType;
  }

  @NotNull
  @Override
  public final ModuleBuilder createModuleBuilder() {
    return myModuleType.createModuleBuilder();
  }

  public static class Java extends ModuleTypeCategory {

    public Java() {
      super(JavaModuleType.getModuleType());
    }
  }

  public static class JavaSE extends Java {
    @Override
    public int getWeight() {
      return JavaModuleBuilder.JAVA_WEIGHT;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    }
  }
}
