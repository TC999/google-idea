import android.app.Activity;
class A extends Activity {
    void foo() {
        <selection>super.foo();
        this.b = sensitive();</selection>
    }
}