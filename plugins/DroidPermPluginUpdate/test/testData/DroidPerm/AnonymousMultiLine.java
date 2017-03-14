import android.app.Activity;
class A extends Activity {
    private int j;
    void foo(){
        Button btn = new Button();
        btn.setText("Run Sensitive");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                <selection>int i = sensitive();
                j = i+1;</selection>
            }
        });
    }
}