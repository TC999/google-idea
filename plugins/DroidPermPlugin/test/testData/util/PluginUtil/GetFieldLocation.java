import android.app.Activity;
class A extends Activity {

    private boolean a = true;
    private int b = 1;
    private String lastField = "I'm the last field";

    void foo(){
        sens<caret>itive();
    }
}