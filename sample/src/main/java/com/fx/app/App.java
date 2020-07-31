package com.fx.app;


import similar.core.AndroidApplication;
import similar.core.Environment;
import similar.core.LaunchMode;
import similar.core.annotations.Activity;
import similar.core.annotations.Application;

import java.io.File;
import java.util.prefs.Preferences;


@Activity(name = SecondActivity.class)
@Activity(name = MainActivity.class,mainActivity = true,lunchMode = LaunchMode.SIGNAL_TASK)
@Application(packageName = "com.fx.app" )
public class App extends AndroidApplication {

    @Override
    protected void onCreated() throws Exception {
        System.out.println("App onCreated");

    }

    @Override
    protected void onFinished() throws Exception {
        System.out.println("App destroy");
    }

    public static void main(String[] args)  {
        try {
            launch(new App(),args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
