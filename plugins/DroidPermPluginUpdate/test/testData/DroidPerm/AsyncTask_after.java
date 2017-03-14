import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

class A extends Activity {
    private TrendParser tp;
    private List<Integer> resultList;
    private static final int ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE = 1;

    void foo(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(A.this, Manifest.permission.ACCESS_CHECKIN_PROPERTIES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(A.this, new String[]{Manifest.permission.ACCESS_CHECKIN_PROPERTIES}, ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE);
            } else {
                guarded();
            }
        } else {
            guarded();
        }

    }

    private void guarded() {
        new AsyncTask<Void, Void, String>() {
            protected void onPreExecute() {
                setText("Please wait...");
            }

            protected String doInBackground(Void... params) {
                tp = new TrendParser("Trend");
                resultList = tp.getAndParse();
                Log.d(TAG, resultList.toString());
                return resultList.toString();
            }

            protected void onPostExecute(List<Integer> l) {
                setText(l.toString());
            }
        }.execute();
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