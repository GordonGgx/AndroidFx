package com.fx.app;


import javafx.scene.control.Button;
import similar.core.Activity;
import similar.core.annotations.Layout;

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
