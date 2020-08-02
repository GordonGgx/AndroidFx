package similar.core;

import similar.core.annotations.Activity;
import similar.core.window.WindowManager;
import similar.data.Intent;

import java.io.File;
import java.net.URL;

public abstract class Context {

    private static String FXML_PATH="layout"+ File.separator;

     URL getFxmlLocation(String name){
       return ClassLoader.getSystemResource(/*FXML_PATH+*/name);
     }


     public void startActivity(Intent intent){
         ActivityManager activityManager=ActivityManager.instance();
         activityManager.lunch(new WindowManager(),intent);
     }
}
