package similar.core;

import javafx.application.Application;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class SimilarPreloader extends Preloader {

    private Stage stage;

    @Override
    public final void start(Stage primaryStage) throws Exception {
        stage=primaryStage;
        Scene scene=onCreateView();
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public abstract Scene onCreateView();

    /**
     * 应用程序启动状态通知处理
     * 应用启动顺序如下：
     * 1.BEFORE_LOAD 在Application 构造器被调用之前触发
     * 2.BEFORE_INIT 在Application init()方法被调用之前触发
     * 3.BEFORE_START 在Application start()方法被调用之前触发
     */
    @Override
    public final void handleStateChangeNotification(StateChangeNotification info) {
        Application app= info.getApplication();

        switch (info.getType()){
            case BEFORE_LOAD:
                onApplicationLoad(app);
                break;
            case BEFORE_INIT:
                onApplicationInit(app);
                break;
            case BEFORE_START:
                onApplicationStart(app);
                break;
        }

    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        if (info instanceof ApplicationNotification){
            ApplicationNotification notification= (ApplicationNotification) info;
            SimilarApplication sa=notification.getApplication();
            onActivityCreated(sa.getCurrentActivity());
        }
    }

    public void onApplicationLoad(Application app){

    }
    public void onApplicationInit(Application app){

    }

    public void onApplicationStart(Application app){

    }

    /**
     * 当主应用的第一个Activity被创建成功后调用
     */
    public void onActivityCreated(Activity activity){

    }

    protected void finish(){
        stage.close();
    }


}
