import android.app.Activity;
class A extends Activity {
    void foo(){
        <selection>sensitive();
        boolean a = true;
        boolean b = false;
        sensitive();
        if(a == true){
            a = false;
        }</selection>
    }
}