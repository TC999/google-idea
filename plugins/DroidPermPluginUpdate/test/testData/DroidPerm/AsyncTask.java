import android.app.Activity;
class A extends Activity {
    void foo(){
        <selection>new AsyncTask<Void, Void, String>() {
            protected void onPreExecute() {
                setText("Please wait...");
            }

            protected String doInBackground(Void... params) {
                TrendParser tp = new TrendParser("Trend");
                List<Integer> resultList = tp.getAndParse();
                Log.d(TAG, resultList.toString());
                return resultList.toString();
            }

            protected void onPostExecute(List<Integer> l) {
                setText(l.toString());
            }
        }.execute();</selection>
    }
}