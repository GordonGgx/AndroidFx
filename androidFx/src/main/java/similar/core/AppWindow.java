package similar.core;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * 窗口管理器
 * 用于创建窗口，显示 关闭
 */
class AppWindow extends Window{

    private final Stage stage;


    private ILifecycle lifecycle;

    private boolean isCloseButtonClicked;

    public AppWindow(Stage stage){
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

        stage.setOnCloseRequest(event -> {
            isCloseButtonClicked=true;
        });

        this.stage.setOnShowing(event -> lifecycle.onStart());
        stage.setOnShown(event -> lifecycle.onResume());
        stage.setOnHiding(event -> lifecycle.onPause());
        stage.setOnHidden(event -> {
            lifecycle.onStop();
            if(isCloseButtonClicked){
                lifecycle.onDestroy();
            }
        });
        stage.fullScreenProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                clearFlags(Window.FLAG_FULL_SCREEN);
            }
        });

    }
    public AppWindow(){
        this(new Stage());
    }

    public double getWith() {
        return stage.getWidth();
    }

    public void setWith(double with) {
       stage.setWidth(with);
    }

    @Override
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
        FadeTransition fade=new FadeTransition(Duration.millis(500),activity.getScene().getRoot());
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

    public boolean isCloseButtonClicked() {
        return isCloseButtonClicked;
    }

    @Override
    void onWindowAttributesChanged() {
        if(hasFlag(Window.FLAG_NO_TITLE)&&!isShowing()){
            stage.initStyle(StageStyle.UNDECORATED);
        }
        if(hasFlag(Window.FLAG_FULL_SCREEN)&&!stage.isFullScreen()){
            stage.setFullScreen(true);
        }else if(!hasFlag(Window.FLAG_FULL_SCREEN)&&stage.isFullScreen()) {
            stage.setFullScreen(false);
        }
    }
}
