import android.app.Activity;
class A extends Activity {
    void foo(){
        Button btn = new Button();
        btn.setText("Run Sensitive");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                <selection>sensitive();</selection>
            }
        });
    }
}