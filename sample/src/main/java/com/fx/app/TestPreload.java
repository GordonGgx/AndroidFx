package com.fx.app;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import similar.core.Activity;
import similar.core.SimilarPreloader;

public class TestPreload extends SimilarPreloader {

    private Parent preloaderParent;
    private Group topGroup;


    @Override
    public void onActivityCreated(Activity activity) {
        super.onActivityCreated(activity);
        SharedScene sharedScene= (SharedScene) activity;
        fadeInTo(sharedScene.getParentNode());
    }

    @Override
    public void onApplicationStart(Application app) {
        super.onApplicationStart(app);
        System.out.println("app start");
    }

    @Override
    public void onApplicationInit(Application app) {
        super.onApplicationInit(app);
        System.out.println("app init");
    }

    @Override
    public void onApplicationLoad(Application app) {
        super.onApplicationLoad(app);
        System.out.println("app load");
    }


    private void fadeInTo(Parent p) {
        //添加application的场景视图到preloader 视图组的后面
        topGroup.getChildren().add( 0,p);

        //设置preloader场景淡入淡出过渡
        FadeTransition ft = new FadeTransition(
                Duration.millis(5000),
            preloaderParent);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setOnFinished(t -> {
            App.completed();
            finish();

        });
        ft.play();
    }

    @Override
    public Scene onCreateView() {
        Rectangle r=new Rectangle(300,150);
        r.setFill(Color.GREEN);
        preloaderParent=new Group(r);
        topGroup=new Group(preloaderParent);
        return new Scene(topGroup,300,150);
    }

    public interface SharedScene{
        /* Parent node of the application */
        Parent getParentNode();
    }

}
