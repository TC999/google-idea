import android.app.Activity;
class A extends Activity {
    private boolean b;
    private boolean a;
    void foo() {
        <selection>super.foo();
        this.b = true;
        sensitive();</selection>
    }
}