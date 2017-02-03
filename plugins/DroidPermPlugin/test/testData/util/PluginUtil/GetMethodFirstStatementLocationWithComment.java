import android.app.Activity;
class A extends Activity {
    private boolean b;
    private boolean a;
    void foo(){
        <selection>

        //Comment Before First Statement

        this.a = true;
        b = true;
        sensitive();</selection>
    }
}