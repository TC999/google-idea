import android.app.Activity;
class A extends Activity {
    void foo(){
        <selection>btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                sensitive();
            }
        });</selection>
    }
}