/*
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
 * Copyright 2003-2013 Dave Griffith, Bas Leijdekkers
=======
 * Copyright 2003-2014 Dave Griffith, Bas Leijdekkers
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
package com.siyeh.ig.numeric;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ui.SingleCheckboxOptionsPanel;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.ConstantExpressionUtil;
import com.intellij.psi.util.PsiUtil;
import com.siyeh.InspectionGadgetsBundle;
import com.siyeh.ig.BaseInspection;
import com.siyeh.ig.BaseInspectionVisitor;
import com.siyeh.ig.InspectionGadgetsFix;
import com.siyeh.ig.PsiReplacementUtil;
import com.siyeh.ig.psiutils.EquivalenceChecker;
import com.siyeh.ig.psiutils.ExpressionUtils;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Set;

public class PointlessArithmeticExpressionInspection
  extends BaseInspection {

  private static final Set<IElementType> arithmeticTokens =
    new THashSet<IElementType>(9);

  static {
    arithmeticTokens.add(JavaTokenType.PLUS);
    arithmeticTokens.add(JavaTokenType.MINUS);
    arithmeticTokens.add(JavaTokenType.ASTERISK);
    arithmeticTokens.add(JavaTokenType.DIV);
    arithmeticTokens.add(JavaTokenType.PERC);
    arithmeticTokens.add(JavaTokenType.GT);
    arithmeticTokens.add(JavaTokenType.LT);
    arithmeticTokens.add(JavaTokenType.LE);
    arithmeticTokens.add(JavaTokenType.GE);
  }

  /**
   * @noinspection PublicField
   */
  public boolean m_ignoreExpressionsContainingConstants = true;

  @Override
  public JComponent createOptionsPanel() {
    return new SingleCheckboxOptionsPanel(
      InspectionGadgetsBundle.message(
        "pointless.boolean.expression.ignore.option"),
      this, "m_ignoreExpressionsContainingConstants");
  }

  @Override
  @NotNull
  public String getDisplayName() {
    return InspectionGadgetsBundle.message(
      "pointless.arithmetic.expression.display.name");
  }

  @Override
  public boolean isEnabledByDefault() {
    return true;
  }

  @Override
  @NotNull
  public String buildErrorString(Object... infos) {
    return InspectionGadgetsBundle.message(
      "expression.can.be.replaced.problem.descriptor",
      calculateReplacementExpression((PsiExpression)infos[0]));
  }

  @NonNls
  String calculateReplacementExpression(PsiExpression expression) {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    final PsiBinaryExpression exp = (PsiBinaryExpression)expression;
    final PsiExpression lhs = exp.getLOperand();
    final PsiExpression rhs = exp.getROperand();
    assert rhs != null;
    final IElementType tokenType = exp.getOperationTokenType();
    if (tokenType.equals(JavaTokenType.PLUS)) {
      if (isZero(lhs)) {
        return rhs.getText();
=======
    final PsiPolyadicExpression polyadicExpression = (PsiPolyadicExpression)expression;
    final PsiExpression[] operands = polyadicExpression.getOperands();
    final IElementType tokenType = polyadicExpression.getOperationTokenType();
    PsiElement fromTarget = null;
    PsiElement untilTarget = null;
    PsiExpression previousOperand = null;
    @NonNls String replacement = "";
    for (int i = 0, length = operands.length; i < length; i++) {
      final PsiExpression operand = operands[i];
      if (tokenType.equals(JavaTokenType.PLUS) && isZero(operand) ||
        tokenType.equals(JavaTokenType.MINUS) && isZero(operand) && i > 0 ||
        tokenType.equals(JavaTokenType.ASTERISK) && isOne(operand) ||
        tokenType.equals(JavaTokenType.DIV) && isOne(operand) && i > 0) {
        fromTarget = (i == length - 1) ? polyadicExpression.getTokenBeforeOperand(operand) : operand;
        break;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
      }
      else if ((tokenType.equals(JavaTokenType.MINUS) || tokenType.equals(JavaTokenType.DIV)) &&
               EquivalenceChecker.expressionsAreEquivalent(previousOperand, operand)) {
        fromTarget = previousOperand;
        untilTarget = operand;
        replacement = PsiType.LONG.equals(polyadicExpression.getType())
                      ? tokenType.equals(JavaTokenType.DIV) ? "1L" : "0L"
                      : tokenType.equals(JavaTokenType.DIV) ? "1" : "0";
        break;
      }
      else if (tokenType.equals(JavaTokenType.ASTERISK) && isZero(operand) ||
        tokenType.equals(JavaTokenType.PERC) && (isOne(operand) || EquivalenceChecker.expressionsAreEquivalent(previousOperand, operand))) {
        return PsiType.LONG.equals(polyadicExpression.getType()) ? "0L" : "0";
      }
      else if (tokenType.equals(JavaTokenType.LE) || tokenType.equals(JavaTokenType.GE) ||
               tokenType.equals(JavaTokenType.LT) || tokenType.equals(JavaTokenType.GT)) {
        return (tokenType.equals(JavaTokenType.LT) || tokenType.equals(JavaTokenType.GT)) ? "false" : "true";
      }

      previousOperand = operand;
    }
    return buildReplacementExpression(polyadicExpression, fromTarget, untilTarget, replacement).trim();
  }

  public static String buildReplacementExpression(PsiPolyadicExpression expression, PsiElement fromTarget, PsiElement untilTarget,
                                                  String replacement) {
    final StringBuilder result = new StringBuilder();
    boolean stop = false;
    for (PsiElement child : expression.getChildren()) {
      if (child == fromTarget) {
        stop = true;
        result.append(replacement);
      }
      else if (child == untilTarget) {
        stop = false;
      }
      else if (child instanceof PsiComment || !stop) {
        result.append(child.getText());
      }
      else if (child instanceof PsiJavaToken && untilTarget == null) {
        stop = false;
      }
    }
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    else if (tokenType.equals(JavaTokenType.MINUS)) {
      if (isZero(rhs)) {
        return lhs.getText();
      }
      return "0";
    }
    else if (tokenType.equals(JavaTokenType.ASTERISK)) {
      if (isOne(lhs)) {
        return rhs.getText();
      }
      else if (isOne(rhs)) {
        return lhs.getText();
      }
      else {
        return "0";
      }
    }
    else if (tokenType.equals(JavaTokenType.DIV)) {
      if (ExpressionUtils.isOne(expression)) {
        return lhs.getText();
      }
      return "1";
    }
    else if (tokenType.equals(JavaTokenType.PERC)) {
      return "0";
    }
    else if (tokenType.equals(JavaTokenType.LE) ||
             tokenType.equals(JavaTokenType.GE)) {
      return "true";
    }
    else if (tokenType.equals(JavaTokenType.LT) ||
             tokenType.equals(JavaTokenType.GT)) {
      return "false";
    }
    else {
      return "";
    }
=======
    return result.toString();
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
  }

  @Override
  public InspectionGadgetsFix buildFix(Object... infos) {
    return new PointlessArithmeticFix();
  }

  private class PointlessArithmeticFix extends InspectionGadgetsFix {
    @Override
    @NotNull
    public String getFamilyName() {
      return getName();
    }

    @Override
    @NotNull
    public String getName() {
      return InspectionGadgetsBundle.message(
        "constant.conditional.expression.simplify.quickfix");
    }

    @Override
    public void doFix(Project project, ProblemDescriptor descriptor) {
      final PsiExpression expression =
        (PsiExpression)descriptor.getPsiElement();
      final String newExpression =
        calculateReplacementExpression(expression);
      PsiReplacementUtil.replaceExpression(expression, newExpression);
    }
  }

  @Override
  public BaseInspectionVisitor buildVisitor() {
    return new PointlessArithmeticVisitor();
  }

  private class PointlessArithmeticVisitor extends BaseInspectionVisitor {

    @Override
    public void visitPolyadicExpression(@NotNull PsiPolyadicExpression expression) {
      super.visitPolyadicExpression(expression);
      final PsiType expressionType = expression.getType();
      if (expressionType == null ||
          PsiType.DOUBLE.equals(expressionType) ||
          PsiType.FLOAT.equals(expressionType)) {
        return;
      }
      if (!arithmeticTokens.contains(expression.getOperationTokenType())) {
        return;
      }
      if (ExpressionUtils.hasStringType(expression)) {
        return;
      }
      final PsiExpression[] operands = expression.getOperands();
      if (operands.length < 2) {
        return;
      }
      final IElementType tokenType = expression.getOperationTokenType();
      final boolean isPointless;
      if (tokenType.equals(JavaTokenType.PLUS)) {
        isPointless = additionExpressionIsPointless(operands);
      }
      else if (tokenType.equals(JavaTokenType.MINUS)) {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
        isPointless = subtractionExpressionIsPointless(lhs, rhs);
=======
        isPointless = subtractionExpressionIsPointless(operands);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
      }
      else if (tokenType.equals(JavaTokenType.ASTERISK)) {
        isPointless = multiplyExpressionIsPointless(operands);
      }
      else if (tokenType.equals(JavaTokenType.DIV)) {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
        isPointless = divideExpressionIsPointless(lhs, rhs);
=======
        isPointless = divideExpressionIsPointless(operands);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
      }
      else if (tokenType.equals(JavaTokenType.PERC)) {
<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
        isPointless = modExpressionIsPointless(lhs, rhs);
=======
        isPointless = modExpressionIsPointless(operands);
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
      }
      else if (tokenType.equals(JavaTokenType.LE) ||
               tokenType.equals(JavaTokenType.GE) ||
               tokenType.equals(JavaTokenType.GT) ||
               tokenType.equals(JavaTokenType.LT)) {
        final PsiExpression lhs = operands[0];
        final PsiExpression rhs = operands[1];
        isPointless = comparisonExpressionIsPointless(lhs, rhs, tokenType);
      }
      else {
        isPointless = false;
      }
      if (!isPointless) {
        return;
      }
      registerError(expression, expression);
    }

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    private boolean subtractionExpressionIsPointless(PsiExpression lhs, PsiExpression rhs) {
      if (PsiType.INT.equals(lhs.getType()) && EquivalenceChecker.expressionsAreEquivalent(lhs, rhs)) {
        return true;
      }
      return isZero(rhs);
=======
    private boolean subtractionExpressionIsPointless(PsiExpression[] expressions) {
      PsiExpression previousExpression = null;
      for (PsiExpression expression : expressions) {
        if (previousExpression != null &&
            (isZero(expression) || EquivalenceChecker.expressionsAreEquivalent(previousExpression, expression))) {
          return true;
        }
        previousExpression = expression;
      }
      return false;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    }

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    private boolean additionExpressionIsPointless(PsiExpression lhs, PsiExpression rhs) {
      return isZero(lhs) || isZero(rhs);
=======
    private boolean additionExpressionIsPointless(PsiExpression[] expressions) {
      for (PsiExpression expression : expressions) {
        if (isZero(expression)) {
          return true;
        }
      }
      return false;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    }

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    private boolean multiplyExpressionIsPointless(PsiExpression lhs, PsiExpression rhs) {
      return isZero(lhs) || isZero(rhs) || isOne(lhs) || isOne(rhs);
=======
    private boolean multiplyExpressionIsPointless(PsiExpression[] expressions) {
      for (PsiExpression expression : expressions) {
        if (isZero(expression) || isOne(expression)) {
          return true;
        }
      }
      return false;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    }

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    private boolean divideExpressionIsPointless(PsiExpression lhs, PsiExpression rhs) {
      if (PsiType.INT.equals(lhs.getType()) && EquivalenceChecker.expressionsAreEquivalent(lhs, rhs)) {
        return true;
      }
      return isOne(rhs);
=======
    private boolean divideExpressionIsPointless(PsiExpression[] expressions) {
      PsiExpression previousExpression = null;
      for (PsiExpression expression : expressions) {
        if (previousExpression != null &&
            (isOne(expression) || EquivalenceChecker.expressionsAreEquivalent(previousExpression, expression))) {
          return true;
        }
        previousExpression = expression;
      }
      return false;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    }

<<<<<<< HEAD   (675888 Merge "Remove unused cloud tools templates")
    private boolean modExpressionIsPointless(PsiExpression lhs, PsiExpression rhs) {
      if (PsiType.INT.equals(lhs.getType()) && EquivalenceChecker.expressionsAreEquivalent(lhs, rhs)) {
        return true;
      }
      return PsiType.INT.equals(rhs.getType()) && isOne(rhs);
=======
    private boolean modExpressionIsPointless(PsiExpression[] expressions) {
      PsiExpression previousExpression = null;
      for (PsiExpression expression : expressions) {
        if (previousExpression != null &&
            (isOne(expression) || EquivalenceChecker.expressionsAreEquivalent(previousExpression, expression))) {
          return true;
        }
        previousExpression = expression;
      }
      return false;
>>>>>>> BRANCH (925846 Snapshot 117b3dbedca758fa08dd37d4a36cf4a2320fae03 from idea/)
    }

    private boolean comparisonExpressionIsPointless(
      PsiExpression lhs, PsiExpression rhs, IElementType comparison) {
      if (PsiType.INT.equals(lhs.getType()) &&
          PsiType.INT.equals(rhs.getType())) {
        return intComparisonIsPointless(lhs, rhs, comparison);
      }
      else if (PsiType.LONG.equals(lhs.getType()) &&
               PsiType.LONG.equals(rhs.getType())) {
        return longComparisonIsPointless(lhs, rhs, comparison);
      }
      return false;
    }

    private boolean intComparisonIsPointless(
      PsiExpression lhs, PsiExpression rhs, IElementType comparison) {
      if (isMaxInt(lhs) || isMinInt(rhs)) {
        return JavaTokenType.GE.equals(comparison) ||
               JavaTokenType.LT.equals(comparison);
      }
      if (isMinInt(lhs) || isMaxInt(rhs)) {
        return JavaTokenType.LE.equals(comparison) ||
               JavaTokenType.GT.equals(comparison);
      }
      return false;
    }

    private boolean longComparisonIsPointless(
      PsiExpression lhs, PsiExpression rhs, IElementType comparison) {
      if (isMaxLong(lhs) || isMinLong(rhs)) {
        return JavaTokenType.GE.equals(comparison) ||
               JavaTokenType.LT.equals(comparison);
      }
      if (isMinLong(lhs) || isMaxLong(rhs)) {
        return JavaTokenType.LE.equals(comparison) ||
               JavaTokenType.GT.equals(comparison);
      }
      return false;
    }
  }

  boolean isZero(PsiExpression expression) {
    if (m_ignoreExpressionsContainingConstants && PsiUtil.deparenthesizeExpression(expression) instanceof PsiReferenceExpression) {
      return false;
    }
    return ExpressionUtils.isZero(expression);
  }

  boolean isOne(PsiExpression expression) {
    if (m_ignoreExpressionsContainingConstants && PsiUtil.deparenthesizeExpression(expression) instanceof PsiReferenceExpression) {
      return false;
    }
    return ExpressionUtils.isOne(expression);
  }

  private static boolean isMinInt(PsiExpression expression) {
    final Integer value = (Integer)
      ConstantExpressionUtil.computeCastTo(
        expression, PsiType.INT);
    return value != null && value.intValue() == Integer.MIN_VALUE;
  }

  private static boolean isMaxInt(PsiExpression expression) {
    final Integer value = (Integer)
      ConstantExpressionUtil.computeCastTo(
        expression, PsiType.INT);
    return value != null && value.intValue() == Integer.MAX_VALUE;
  }

  private static boolean isMinLong(PsiExpression expression) {
    final Long value = (Long)
      ConstantExpressionUtil.computeCastTo(
        expression, PsiType.LONG);
    return value != null && value.longValue() == Long.MIN_VALUE;
  }

  private static boolean isMaxLong(PsiExpression expression) {
    final Long value = (Long)
      ConstantExpressionUtil.computeCastTo(
        expression, PsiType.LONG);
    return value != null && value.longValue() == Long.MAX_VALUE;
  }
}
