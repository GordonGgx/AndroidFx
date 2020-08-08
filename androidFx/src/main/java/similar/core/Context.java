package similar.core;

import similar.core.broadcast.LocalBroadcastManager;
import similar.data.Intent;

import java.io.File;
import java.net.URL;
import java.util.Objects;

public abstract class Context {

    private static String FXML_PATH="layout"+ File.separator;

     URL getFxmlLocation(String name){
       return ClassLoader.getSystemResource(/*FXML_PATH+*/name);
     }


     public void startActivity(Intent intent){
         Objects.requireNonNull(intent.getComponentName(),"无法启动Activity，缺少组建信息");
         ActivityManager activityManager=ActivityManager.instance();
         activityManager.lunch(new AppWindow(),intent);
     }

     public Context getApplicationContext(){
         return AndroidApplication.instance();
     }

     public void sendBroadcast(Intent intent){
         LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
     }
}
