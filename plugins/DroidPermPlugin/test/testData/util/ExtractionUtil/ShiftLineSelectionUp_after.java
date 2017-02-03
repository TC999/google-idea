import android.app.Activity;
class A extends Activity {
    void foo(){
<selection>        boolean b = true;
        if(b){
            sensitive();</selection>
        }
    }
}