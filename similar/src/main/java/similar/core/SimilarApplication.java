package similar.core;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;
import similar.core.annotations.Launcher;
import similar.core.annotations.Preloader;
import similar.data.Intent;
import similar.util.ErrorHandler;
import similar.util.Utils;

public abstract class SimilarApplication extends Application {

    private static BooleanProperty ready=new SimpleBooleanProperty(false);

    private Stage primaryStage;

    @Override
    public final void init() throws Exception {
        ready.addListener((observable, oldValue, newValue) -> {
            if(newValue&&!primaryStage.isShowing()){
                Platform.runLater(()->{
                    primaryStage.centerOnScreen();
                    primaryStage.show();
                });
            }
        });
        ActivityManager.instance().initActivity(this);
        onCreated();
    }

    @Override
    public final void start(Stage primaryStage) throws Exception {
        this.primaryStage=primaryStage;
        ActivityManager.instance().setWindowManager(primaryStage);
        similar.core.annotations.Activity mainActivity=ActivityManager.instance().getMainActivity();
        if(mainActivity==null){
            ErrorHandler.get().show(new Exception("缺少启动类"));
        }else {
            ActivityManager.instance().lunch(new Intent(mainActivity.name()));
            boolean hasPreload=getClass().isAnnotationPresent(Preloader.class);
            if(hasPreload){
                notifyPreloader(new ApplicationNotification(this));
            }

            ready.setValue(!hasPreload);
        }
    }

    @Override
    public final void stop() throws Exception {
        ActivityManager.clear();
        onFinish();
    }

    /**
     * 当Application被创建后调用
     * @throws Exception
     */
    protected void onCreated() throws Exception{

    }

    /**
     * 当Application结束时触发
     * @throws Exception
     */
    protected void onFinish()throws Exception{

    }

    public static void launch(Class<? extends Application> app,
                             String[] args){
        if(Utils.jdkLT9()){
            if(app.isAnnotationPresent(Preloader.class)){
                Preloader preload=app.getAnnotation(Preloader.class);
                LauncherImpl.launchApplication(app,preload.value(),args);
            }else {
                Application.launch(app,args);
            }
        }else {
            if(app.isAnnotationPresent(Preloader.class)){
                Preloader preload=app.getAnnotation(Preloader.class);
                System.setProperty("javafx.preloader",preload.value().getCanonicalName());
            }
            Application.launch(app,args);

        }
    }

    public static void completed(){
        ready.setValue(true);
    }

    Activity getCurrentActivity() {
        return ActivityManager.instance().top();
    }


}
