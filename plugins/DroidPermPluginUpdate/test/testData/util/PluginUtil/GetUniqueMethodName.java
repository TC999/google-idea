import android.app.Activity;
class A extends Activity {

    void guarded(){
		int a = 1;
    }

    void foo(int a){
        sensitiv<caret>e();
    }

}