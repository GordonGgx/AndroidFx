package com.fx.app;


import javafx.scene.control.Button;
import javafx.stage.Stage;
import similar.core.Activity;
import similar.core.Layout;

@Layout("layout/second.fxml")
public class SecondActivity extends Activity {

    @Override
    protected void onCreated() {
        super.onCreated();
        Button btn=findViewById("btn");
        btn.setOnAction(event -> {
            finish();
        });

    }
}
