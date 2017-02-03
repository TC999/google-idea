import android.app.Activity;
class A extends Activity {
    void foo() {
        <selection>sensitive();
        String myString = "My String";
        int myInt = 1;
        boolean myBool = true;
        sensitive();</selection>
    }
}