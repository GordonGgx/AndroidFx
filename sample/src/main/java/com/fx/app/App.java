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
        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("java.io.tmpdir"));
        System.out.println(Environment.getCacheDataDir().getAbsolutePath());
        Preferences.userRoot().putInt("ggx",20);
        System.out.println(Preferences.userRoot().getInt("ggx",30));
        System.out.println(Preferences.userRoot().name());
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
