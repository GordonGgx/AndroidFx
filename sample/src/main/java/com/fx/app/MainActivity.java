package com.fx.app;


import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;
import similar.core.Activity;
import similar.core.Layout;
import similar.function.Boxes;
import similar.util.concurrent.Scheme;
import similar.util.concurrent.Task;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

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
        Task.async(()->{
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "ggx";
        }).andThen(str->{
            System.out.println(Thread.currentThread().getName());
            System.out.println(str);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return str.toUpperCase();
        }).runAs(Scheme.ui()).andThen(str->{
            System.out.println(Thread.currentThread().getName());
            System.out.println(str);
            str.split("g")[5]="0";
            return str.toLowerCase();
        }).exceptionThen(throwable -> {
            System.out.println("发生了一场，");
            return "fixResult";
        }).runAs(Scheme.work()).andThen(str->{
            System.out.println(Thread.currentThread().getName());
            System.out.println(str);
            return str.split("g");
        }).runAs(Scheme.work()).andThen(strings -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println(Arrays.toString(strings));
            System.out.println(strings[7]);
        }).exceptionally(ex->{
            System.out.println(Thread.currentThread().getName());
            ex.printStackTrace();
        });

        Function<Integer,Function<Integer,Integer>> add=x->y->x+y;
        Boxes.box(add).ap(Boxes.box(1)).as(Boxes::box).ap(Boxes.box(2));

    }

    @Override
    public Parent getParentNode() {
        return parentNode;
    }
}
