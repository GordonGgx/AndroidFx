package similar.core;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import similar.core.annotations.Preloader;
import similar.data.Intent;
import similar.util.ErrorHandler;

public final class SimilarApplication extends javafx.application.Application {

    private final static BooleanProperty ready=new SimpleBooleanProperty(false);

    private AndroidApplication application;

    @Override
    public final void init() throws Exception {
        application=AndroidApplication.instance();
        ActivityManager.instance().initActivity(AndroidApplication.instance());
        application.onCreated();
    }

    @Override
    public final void start(Stage primaryStage) throws Exception {
        ready.addListener((observable, oldValue, newValue) -> {
            if(newValue&&!primaryStage.isShowing()){
                Platform.runLater(primaryStage::show);
            }
        });
        primaryStage.setTitle(application.getTitle());
        primaryStage.setResizable(true);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.centerOnScreen();
        primaryStage.getIcons().add(new Image(application.getIcon()));
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
        application.onFinished();
    }



    public static void completed(){
        ready.setValue(true);
    }

    Activity getCurrentActivity() {
        return ActivityManager.instance().top();
    }


}
