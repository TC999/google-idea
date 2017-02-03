import android.app.Activity;
class A extends Activity {
    void foo() {
        int i = 1;
        <selection>sensitive(i);</selection>
    }
}