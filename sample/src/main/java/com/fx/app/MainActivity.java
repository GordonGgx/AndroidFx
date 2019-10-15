package com.fx.app;


import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import similar.control.ListView;
import similar.core.Activity;
import similar.core.Layout;
import similar.data.Intent;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

@Layout("layout/main.fxml")
public class MainActivity extends Activity implements TestPreload.SharedScene {

    private Rectangle rectangle;
    private Parent parentNode;
//    @FXML
//    private Button btn;
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
        getWindow().setTitle("场景切换动画测试");
       // Button button=findViewById("btn");
//        btn.setOnAction(event -> {
//            startActivity(new Intent(this,SecondActivity.class));
//        });
//        clv.setOnAction(event -> {
//            Random random=new Random(System.currentTimeMillis());
//            clv.setBackgroundFill(Color.color(random.nextDouble(),random.nextDouble(),random.nextDouble()));
//        });
        System.out.println(ForkJoinPool.getCommonPoolParallelism());
        CompletableFuture.runAsync(()->{
            System.out.println("第一个执行");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("第一个执行结束");
        });
        CompletableFuture.runAsync(()->{
            System.out.println("第2个执行");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("第2个执行结束");
        });
        CompletableFuture.runAsync(()->{
            System.out.println("第3个执行");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("第3个执行结束");
        });
        CompletableFuture.runAsync(()->{
            System.out.println("第4个执行");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("第4个执行结束");
        });
        CompletableFuture.runAsync(()->{
            System.out.println("第5个执行");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("第5个执行结束");
        });
    }

    @Override
    public Parent getParentNode() {
        return parentNode;
    }
}
