import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

class A extends Activity {
    private static final int ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE = 1;
    private static final int ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE1 = 2;

    void foo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(A.this, Manifest.permission.ACCESS_CHECKIN_PROPERTIES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_CHECKIN_PROPERTIES}, ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE);
            } else {
                sensitive();
            }
        } else {
            sensitive();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(A.this, Manifest.permission.ACCESS_CHECKIN_PROPERTIES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_CHECKIN_PROPERTIES}, ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE1);
            } else {
                sensitive1();
            }
        } else {
             sensitive1();
        }

    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        switch (requestCode) {<caret>
            case ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE1:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sensitive1();
                } else {
                    Toast.makeText(this, "ACCESS_CHECKIN_PROPERTIES Permission Denied", Toast.LENGTH_LONG).show();
                }
            case ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sensitive();
                } else {
                    Toast.makeText(this, "ACCESS_CHECKIN_PROPERTIES Permission Denied", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}