import android.app.Activity;
class A extends Activity {
    void foo() {
        guarded();
    }

    private void guarded() {
        sensitive();
        int a = 1;
        int b = 2;
    }
}