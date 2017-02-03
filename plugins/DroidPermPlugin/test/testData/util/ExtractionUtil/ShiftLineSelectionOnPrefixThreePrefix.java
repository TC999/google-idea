import android.app.Activity;
class A extends Activity {
    boolean a;
    int b;
    int c;
    void foo(){
<selection>        this.a = true;
        this.b = 2;
        this.c =  1;
        sensitive();</selection>
    }
}