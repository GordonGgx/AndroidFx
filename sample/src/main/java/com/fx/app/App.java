package com.fx.app;


import similar.core.LaunchMode;
import similar.core.annotations.Activity;
import similar.core.annotations.Preloader;
import similar.core.SimilarApplication;


@Activity(name = SecondActivity.class)
@Activity(name = MainActivity.class,mainActivity = true,lunchMode = LaunchMode.SIGNAL_TASK)
//@Preloader(TestPreload.class)
public class App extends SimilarApplication {

    @Override
    protected void onCreated() throws Exception {
        super.onCreated();
        System.out.println("App onCreated");
    }

    @Override
    protected void onFinish() throws Exception {
        super.onFinish();
        System.out.println("app destroy");
    }

    public static void main(String[] args) {
        launch(App.class,args);

    }
}
