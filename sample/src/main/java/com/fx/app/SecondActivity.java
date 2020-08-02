package com.fx.app;


import javafx.scene.control.Button;
import similar.core.Activity;
import similar.core.annotations.Layout;
import similar.data.Intent;

@Layout("layout/second.fxml")
public class SecondActivity extends Activity {

    @Override
    public void onCreated() {
        super.onCreated();
        System.out.println("Second Act onCreated");
        Button btn=findViewById("btn");
        btn.setOnAction(event -> {
            startActivity(new Intent(MainActivity.class));
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("Second act onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("Second act onResume");
    }

    @Override
    public void onPause() {
        super.onResume();
        System.out.println("Second act onPause");
    }


    @Override
    public void onStop() {
        super.onStop();
        System.out.println("Second act onStop");
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Second act onDestroy");
    }
}
