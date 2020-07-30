package similar.core.window;

import javafx.stage.Stage;
import similar.core.Activity;

/**
 * 窗口管理器
 * 用于创建窗口，显示 关闭
 */
public class WindowManager {

    private Stage stage;

    public WindowManager(Stage stage){
        this.stage=stage;
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

    public void attachToWindow(Activity activity){
        stage.setScene(activity.getScene());
        stage.iconifiedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
        });
        stage.centerOnScreen();
        activity.onStart();
    }

    public void detachToWindow(Activity activity){
        stage.setScene(null);
    }

    public void closeWindow(){
        stage.close();
    }
}
