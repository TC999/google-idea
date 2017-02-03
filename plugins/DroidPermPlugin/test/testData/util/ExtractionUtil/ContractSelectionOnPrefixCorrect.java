import android.app.Activity;
class A extends Activity {
    private boolean a;
    void foo(){
        <selection>this.a = false;
        sensitive();</selection>
    }
}