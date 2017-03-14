import android.app.Fragment;
class A extends Fragment {
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