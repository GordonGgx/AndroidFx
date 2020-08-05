package com.fx.app;


import com.sun.javafx.menu.MenuBase;
import com.sun.javafx.menu.MenuItemBase;
import com.sun.javafx.scene.control.GlobalMenuAdapter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.input.KeyCombination;
import similar.core.AndroidApplication;
import similar.core.Environment;
import similar.core.LaunchMode;
import similar.core.annotations.Activity;
import similar.core.annotations.Application;
import similar.core.log.Log;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
        Log.d("开始启动");
        try {
            launch(new App(),args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
