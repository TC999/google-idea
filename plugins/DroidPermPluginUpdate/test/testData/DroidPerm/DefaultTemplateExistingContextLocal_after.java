import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

class A {
    void foo() {
        boolean myBool = true;
        Context context = new Context();
        String myString = "My String";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_CHECKIN_PROPERTIES)
                    == PackageManager.PERMISSION_GRANTED) {
                sensitive();
            }
        } else {
            sensitive();
        }

        Context context2 = new Context();
        int myInt = 1;
    }
}