package com.fx.app;


import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import similar.core.Activity;
import similar.core.Layout;
import similar.data.Intent;

@Layout("layout/main.fxml")
public class MainActivity extends Activity implements TestPreload.SharedScene {

    private Rectangle rectangle;
    private Parent parentNode;

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
        getWindow().setTitle("场景切换动画测试");
        Button button=findViewById("btn");
        button.setOnAction(event -> {
            startActivity(new Intent(SecondActivity.class));
        });

    }

    @Override
    public Parent getParentNode() {
        return parentNode;
    }
}
