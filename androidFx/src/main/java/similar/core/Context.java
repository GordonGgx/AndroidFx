package similar.core;

import similar.core.broadcast.LocalBroadcastManager;
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

     public Context getApplicationContext(){
         return AndroidApplication.instance();
     }

     public void sendBroadcast(Intent intent){
         LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
     }
}
