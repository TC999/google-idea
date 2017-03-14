import android.app.Activity;
class A extends Activity {
private int unTabbedField = 0;
    void foo() {
		<selection>int localVar = sensitive();</selection>
    }

void unTabbedMethodWithANSIBrackets()
    {
unTabbedField = 1;
    }
}