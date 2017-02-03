import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

class A extends Activity {
    private int i;
    private int j;
    private int k;
    private static final int ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE = 1;

    void foo() {
        i = 1;
        j = 2;
        k = 3;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(A.this, Manifest.permission.ACCESS_CHECKIN_PROPERTIES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_CHECKIN_PROPERTIES}, ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE);
            } else {
                sensitive(i, j, k);
            }
        } else {
            sensitive(i, j, k);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sensitive(i, j, k);
            } else {
                Toast.makeText(this, "ACCESS_CHECKIN_PROPERTIES Permission Denied", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}