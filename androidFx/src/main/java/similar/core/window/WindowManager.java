package similar.core.window;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import similar.core.Activity;
import similar.core.AndroidApplication;
import similar.core.ILifecycle;

/**
 * 窗口管理器
 * 用于创建窗口，显示 关闭
 */
public class WindowManager {

    private final Stage stage;


    private ILifecycle lifecycle;

    public WindowManager(Stage stage){
        AndroidApplication application = AndroidApplication.instance();
        this.stage=stage;
        this.stage.setTitle(application.getTitle());
        this.stage.getIcons().add(new Image(application.getIcon()));
        this.stage.sizeToScene();
        this.stage.setResizable(true);
        this.stage.centerOnScreen();
        this.stage.iconifiedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                lifecycle.onPause();
            }else {
                lifecycle.onResume();
            }
        });

        this.stage.setOnShowing(event -> lifecycle.onStart());
        stage.setOnShown(event -> lifecycle.onResume());
        stage.setOnHiding(event -> lifecycle.onPause());
        stage.setOnHidden(event -> lifecycle.onStop());



    }
    public WindowManager(){
        this(new Stage());
    }

    public double getWith() {
        return stage.getWidth();
    }

    public void setWith(double with) {
       stage.setWidth(with);
    }

    public double getHeight() {
        return stage.getHeight();
    }

    public void setHeight(double height) {
        stage.setHeight(height);
    }

    public String getTitle() {
        return stage.getTitle();
    }

    public void setTitle(String title) {
        stage.setTitle(title);
    }

    public void setLifecycle(ILifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    public void attachToWindow(Activity activity){
        stage.setScene(activity.getScene());
        stage.show();
        FadeTransition fade=new FadeTransition(Duration.millis(1000),activity.getScene().getRoot());
        fade.setInterpolator(Interpolator.EASE_IN);
        fade.setFromValue(0.1);
        fade.setToValue(1);
        fade.play();

    }

    public void closeWindow(){
        stage.close();
    }

    public boolean isShowing(){
        return stage.isShowing();
    }
}
