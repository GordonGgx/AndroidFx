package com.fx.app;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import similar.core.Activity;
import similar.core.SimilarPreloader;

public class TestPreload extends SimilarPreloader {

    private Parent preloaderParent;
    private Group topGroup;


    @Override
    public void onActivityCreated(Activity activity) {
        super.onActivityCreated(activity);
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


    @Override
    public Scene onCreateView() {
        Rectangle r=new Rectangle(300,150);
        r.setFill(Color.GREEN);
        preloaderParent=new Group(r);
        topGroup=new Group(preloaderParent);
        return new Scene(topGroup,300,150);
    }


}
