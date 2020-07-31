package com.fx.app;


import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import similar.core.Activity;
import similar.core.annotations.Layout;
import similar.data.Intent;

import java.util.prefs.Preferences;

@Layout("layout/main.fxml")
public class MainActivity extends Activity {

    private Rectangle rectangle;
    private Parent parentNode;

    @FXML
    private Button btn;
//    @FXML
//    private ListView clv;

    @Override
    protected void onCreated() {
        super.onCreated();
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


    }
}
