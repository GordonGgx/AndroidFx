package com.fx.app;


import similar.core.Launcher;
import similar.core.SimilarApplication;

@Launcher(MainActivity.class)
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
