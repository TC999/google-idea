class A {
    private static final int ACCESS_CHECKIN_PROPERTIES_REQUEST_CODE = 1;

    void foo() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (android.support.v4.app.ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_CHECKIN_PROPERTIES)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // permission not granted, consider calling ActivityCompat.requestPermissions()
            } else {
                sensitive();
            }
        } else {
            sensitive();
        }

    }
}