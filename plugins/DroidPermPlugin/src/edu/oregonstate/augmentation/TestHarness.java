package edu.oregonstate.augmentation;

import com.intellij.openapi.diff.impl.ComparisonPolicy;
import com.intellij.openapi.diff.impl.fragments.LineFragment;
import com.intellij.openapi.diff.impl.processing.TextCompareProcessor;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;
import com.intellij.util.diff.FilesTooBigForDiffException;

import java.awt.*;
import java.util.List;

/**
 * @author Nicholas Nelson <nelsonni@oregonstate.edu> Created on on 8/13/16.
 */
public class TestHarness {

    private int preLOC = 0;
    private int postLOC = 0;
    private String prevText = "";
    private int linesChanged = 0;
    public boolean innerClass = false;
    public boolean returnType = false;
    public String template = "";
    public boolean methodExtract = false;
    public boolean localVariableExtract = false;
    public boolean parameterExtract = false;
    public boolean guardInserted = false;
    public boolean callbackInserted = false;
    public boolean callbackExtendIf = false;
    public boolean callbackExtendSwitch = false;

    private TestHarness() {}

    public static TestHarness getInstance() {
        return new TestHarness();
    }

    public void calculateBeforeState(PsiFile file) {
        prevText = file.getText();
        preLOC = LineUtil.countLines(file);
    }

    public void calculateAfterState(PsiFile file) {
        TextCompareProcessor processor = new TextCompareProcessor(ComparisonPolicy.DEFAULT);
        List<LineFragment> lineFragments = null;
        try {
            lineFragments = processor.process(prevText, file.getText());
        } catch (FilesTooBigForDiffException e) {
            System.out.println("FilesTooBigForDiffException in '" + file.getName() + "'");
            e.printStackTrace();
        }
        if (lineFragments != null) linesChanged = lineFragments.size();
        postLOC = LineUtil.countLines(file);
    }

    public void showDialog(Component component) {
        StringBuilder sb = new StringBuilder();
        sb.append("Template:\t\t\t" + template + "\n");
        sb.append("Lines of Code (Before):\t" + preLOC + "\n");
        sb.append("Lines of Code (After):\t" + postLOC + "\n");
        sb.append("Lines Changed:\t\t" + linesChanged + "\n");
        sb.append("Inner Class:\t\t" + innerClass + "\n");
        sb.append("Return Type:\t\t" + returnType + "\n");
        sb.append("Method Extracted:\t\t" + methodExtract + "\n");
        sb.append("Local Variable(s) Extracted:\t" + localVariableExtract + "\n");
        sb.append("Parameter Refs Extracted:\t" + parameterExtract + "\n");
        sb.append("Permission Guard Inserted:\t" + guardInserted + "\n");
        sb.append("Callback Method Inserted:\t" + callbackInserted + "\n");
        sb.append("Callback extended If-Block:\t" + callbackExtendIf + "\n");
        sb.append("Callback extended Switch:\t" + callbackExtendSwitch + "\n");
        Messages.showInfoMessage(component, sb.toString(), "DroidPermPlugin: Post-Transformation Report");
    }

}
