import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

class A extends Activity {
    private int j;
    private int i;
    private Button btn;

    void foo(){
        btn = new Button();
        btn.setText("Run Sensitive");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            private static final int ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE = 1;

            @Override
            public void handle(ActionEvent event) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (A.this.checkSelfPermission(A.this, Manifest.permission.ACCESS_CHECKIN_PROPERTIES)
                            != PackageManager.PERMISSION_GRANTED) {
                        A.this.requestPermissions(this, new String[]{Manifest.permission.ACCESS_CHECKIN_PROPERTIES}, ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE);
                    } else {
                        guarded();
                    }
                } else {
                    guarded();
                }

            }
        });
    }

    private void guarded() {
        i = sensitive();
        j = i +1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                guarded();
            } else {
                Toast.makeText(this, "ACCESS_CHECKIN_PROPERTIES Permission Denied", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}