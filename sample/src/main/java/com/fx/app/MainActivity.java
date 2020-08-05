package com.fx.app;


import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import similar.core.Activity;
import similar.core.annotations.Layout;
import similar.data.Intent;

@Layout("layout/main.fxml")
public class MainActivity extends Activity {

    private Rectangle rectangle;
    private Parent parentNode;

    @FXML
    private Button btn;
//    @FXML
//    private ListView clv;

    @Override
    public void onCreated() {
        super.onCreated();
        btn.setOnAction(event -> {
            startActivity(new Intent(SecondActivity.class));
            finish();
        });
//        Path path=new Path();
//        path.getElements().add(new MoveTo(20,20));
//        path.getElements().add(new CubicCurveTo(380,0,380,120,200,120));
//
//        rectangle=new Rectangle(0,0,40,40);
//        rectangle.setArcHeight(10);
//        rectangle.setArcWidth(10);
//        rectangle.setFill(Color.ORANGE);
//        parentNode=new Group(rectangle);
//        rectangle.setOnMouseClicked(event -> {
//            startActivity(new Intent(SecondActivity.class));
//        });
//        setContentView(parentNode);
//
//        PathTransition pathTransition=new PathTransition();
//        pathTransition.setDuration(Duration.millis(4000));
//        pathTransition.setPath(path);
//        pathTransition.setNode(rectangle);
//        pathTransition.setCycleCount(Timeline.INDEFINITE);
//        pathTransition.setAutoReverse(true);
//        pathTransition.play();
        System.out.println("Main act onCreated");

    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("Main act onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("Main act onResume");
    }

    @Override
    public void onPause() {
        super.onResume();
        System.out.println("Main act onPause");
    }


    @Override
    public void onStop() {
        super.onStop();
        System.out.println("Main act onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Main act onDestroy");
    }
}
