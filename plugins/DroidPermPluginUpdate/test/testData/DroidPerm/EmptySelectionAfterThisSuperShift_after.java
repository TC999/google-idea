import android.app.Activity;
class A extends Activity {
    void foo() {
        super.foo();
        this.b = sensitive();
    }
}