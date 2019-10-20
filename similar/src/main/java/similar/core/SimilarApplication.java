package similar.core;

import com.sun.javafx.application.LauncherImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;
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
        onCreated();
    }

    @Override
    public final void start(Stage primaryStage) throws Exception {
        this.primaryStage=primaryStage;
        ActivityManager.instance().init(primaryStage);
        Class<?> clazz=getClass();
        if(clazz.isAnnotationPresent(Launcher.class)){
            Launcher launcher =getClass().getAnnotation(Launcher.class);
            Class<? extends Activity> activityClass= launcher.value();
            ActivityManager.instance().pushActivityByStandard(new Intent(null,activityClass));
            notifyPreloader(new ApplicationNotification(this));
            ready.setValue(!clazz.isAnnotationPresent(similar.core.Preloader.class));
        }else {
            ErrorHandler.get().show(new Exception("缺少启动类"));
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
