package similar.core;

import com.sun.javafx.application.LauncherImpl;
import similar.core.annotations.Application;
import similar.core.annotations.Preloader;
import similar.util.Utils;

/**
 * 应用程序接口
 * 每一个应用程序都应该实现此接口
 */
public abstract class AndroidApplication extends Context{

    private static AndroidApplication app;
    private String icon="icon/ic_launcher_round.png";
    private String title="app_Name";
    private String packageName;

    /**
     * 当Application被创建后调用
     * @throws Exception
     */
   protected abstract void onCreated() throws Exception;

    /**
     * 当Application被创建后调用
     * @throws Exception
     */
    protected abstract void onFinished() throws Exception;

     public String getIcon() {
        return icon;
    }

     public String getTitle() {
        return title;
    }

     public String getPackageName() {
        return packageName;
    }

    public static AndroidApplication instance(){
        return app;
    }

    public static void launch(AndroidApplication app,
                              String[] args) throws Exception {

        Class<?> clazz=app.getClass();
        if(!clazz.isAnnotationPresent(Application.class)){
            throw new Exception("不是一个标准的应用程序，请在类型上标明@Application");
        }
        Application appInfo=clazz.getAnnotation(Application.class);
        app.icon=appInfo.icon();
        app.packageName= appInfo.packageName();
        app.title= appInfo.label();
        AndroidApplication.app=app;
        if(Utils.jdkLT9()){
            if(clazz.isAnnotationPresent(Preloader.class)){
                Preloader preload=clazz.getAnnotation(Preloader.class);
                LauncherImpl.launchApplication(SimilarApplication.class,preload.value(),args);
            }else {
                javafx.application.Application.launch(SimilarApplication.class,args);
            }
        }else {
            if(clazz.isAnnotationPresent(Preloader.class)){
                Preloader preload=clazz.getAnnotation(Preloader.class);
                System.setProperty("javafx.preloader",preload.value().getCanonicalName());
            }
            javafx.application.Application.launch(SimilarApplication.class,args);

        }
    }
}
