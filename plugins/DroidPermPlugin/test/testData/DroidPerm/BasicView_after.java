import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.View;
class A extends View {
    private static final int ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE = 1;

    void foo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_CHECKIN_PROPERTIES)
                    == PackageManager.PERMISSION_GRANTED) {
                sensitive();
            }
        } else {
            sensitive();
        }

    }
}