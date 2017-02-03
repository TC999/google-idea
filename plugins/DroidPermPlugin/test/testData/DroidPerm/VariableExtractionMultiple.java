import android.app.Activity;
class A extends Activity {
    void foo() {
		int i = 1;
        int j = 2;
        int k = 3;
        <selection>sensitive(i, j, k);</selection>
    }
}