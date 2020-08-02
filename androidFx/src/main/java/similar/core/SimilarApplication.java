package similar.core;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import similar.core.annotations.Preloader;
import similar.core.window.WindowManager;
import similar.data.Intent;
import similar.util.ErrorHandler;

public final class SimilarApplication extends javafx.application.Application {

    private final static BooleanProperty preloadReady=new SimpleBooleanProperty(false);

    private AndroidApplication application;

    @Override
    public final void init() throws Exception {
        application=AndroidApplication.instance();
        ActivityManager.instance().loadActivities(AndroidApplication.instance());
        application.onCreated();
    }

    @Override
    public final void start(Stage primaryStage) throws Exception {
        preloadReady.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                Platform.runLater(()->{
                    similar.core.annotations.Activity mainActivity=ActivityManager.instance().getMainActivity();
                    ActivityManager.instance().lunch(new WindowManager(primaryStage),
                            new Intent(mainActivity.name()));
                });
            }
        });

        similar.core.annotations.Activity mainActivity=ActivityManager.instance().getMainActivity();
        if(mainActivity==null){
            ErrorHandler.get().show(new Exception("缺少主Activity"));
        }else {
            boolean hasPreload=getClass().isAnnotationPresent(Preloader.class);
            if(hasPreload){
                notifyPreloader(new ApplicationNotification(this));
            }else {
                ActivityManager.instance().lunch(new WindowManager(primaryStage),
                        new Intent(mainActivity.name()));
                preloadReady.setValue(false);
            }

        }
    }

    @Override
    public final void stop() throws Exception {
        ActivityManager.clear();
        application.onFinished();
    }



    public static void completed(){
        preloadReady.setValue(true);
    }

    Activity getCurrentActivity() {
        return ActivityManager.instance().top();
    }


}
