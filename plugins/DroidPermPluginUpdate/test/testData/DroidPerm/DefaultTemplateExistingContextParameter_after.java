import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

class A {
    void foo(Context myContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_CHECKIN_PROPERTIES)
                    == PackageManager.PERMISSION_GRANTED) {
                sensitive();
            }
        } else {
            sensitive();
        }

    }
}