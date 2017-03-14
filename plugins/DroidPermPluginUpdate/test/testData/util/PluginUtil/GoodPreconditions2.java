import android.app.Activity;
class A extends Activity {
    private boolean b;
    void foo(){
        int <caret>i = sensitive();
    }
}