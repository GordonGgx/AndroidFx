package com.fx.app;


import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import similar.control.skin.VirtualFlow;
import similar.core.Activity;
import similar.core.Layout;

@Layout("layout/main.fxml")
public class MainActivity extends Activity implements TestPreload.SharedScene {

    private Rectangle rectangle;
    private Parent parentNode;

    @FXML
    private Group gp;

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
       // Button button=findViewById("btn");
//        btn.setOnAction(event -> {
//            startActivity(new Intent(this,SecondActivity.class));
//        });
//        clv.setOnAction(event -> {
//            Random random=new Random(System.currentTimeMillis());
//            clv.setBackgroundFill(Color.color(random.nextDouble(),random.nextDouble(),random.nextDouble()));
//        });
//        VBox vBox=new VBox();
//        vBox.setPrefWidth(50);
//        vBox.setPrefHeight(100);
//        vBox.getChildren().add(new Label("啊哈哈好"));
//        gp.getChildren().add(vBox);
        VirtualFlow vf=new VirtualFlow();
        vf.setPrefHeight(200);
        vf.setPrefWidth(200);
        gp.getChildren().add(vf);

    }

    @Override
    public Parent getParentNode() {
        return parentNode;
    }
}
