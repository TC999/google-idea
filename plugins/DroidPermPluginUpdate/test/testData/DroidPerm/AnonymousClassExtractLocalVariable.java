import android.app.Activity;
class A extends Activity {
    void foo(){
        Button btn = new Button();
        btn.setText("Run Sensitive");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Intent newIntent = new Intent();
                <selection>sensitive(newIntent);</selection>
            }
        });
    }
}