import android.app.Activity;
class A extends Activity {
    private boolean a;
    private boolean b;
    void foo() {
        <selection>sensitive();
		a = true;
        b = false;</selection>
    }
}