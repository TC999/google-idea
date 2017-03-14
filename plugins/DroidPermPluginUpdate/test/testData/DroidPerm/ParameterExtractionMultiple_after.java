import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

class A extends Activity {
    private boolean f_b;
    private int f_i;
    private String f_str;
    private static final int ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE = 1;

    void foo(boolean b, int i, String str) {
        this.f_str = str;
        this.f_i = i;
        this.f_b = b;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(A.this, Manifest.permission.ACCESS_CHECKIN_PROPERTIES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(A.this, new String[]{Manifest.permission.ACCESS_CHECKIN_PROPERTIES}, ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE);
            } else {
                sensitive(f_b, f_i, f_str);
            }
        } else {
            sensitive(f_b, f_i, f_str);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sensitive(f_b, f_i, f_str);
            } else {
                Toast.makeText(this, "ACCESS_CHECKIN_PROPERTIES Permission Denied", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}