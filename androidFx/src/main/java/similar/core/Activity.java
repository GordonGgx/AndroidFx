package similar.core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import similar.core.annotations.Layout;
import similar.data.Intent;
import similar.util.ErrorHandler;

import java.io.IOException;

public class Activity extends Context implements ILifecycle{

    private Window appWindow;

    private Intent intent;

    private Scene mScene;

    public Activity() {

    }


    /**
     * 当Activity被创建之后触发此方法
     */
    public void onCreated() {
        Class<?> clazz=getClass();
        if(clazz.isAnnotationPresent(Layout.class)){
            Layout layout=clazz.getAnnotation(Layout.class);
            String fxml=layout.value();
            setContentView(fxml);
        }else {
            setContentView(new StackPane());
        }

    }


    /**
     * 当Activity显示调用
     */
    public void onStart(){

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }


    /**
     * 当Activity隐藏后调用
     */
    @Override
    public void onStop(){

    }

    /**
     * 当Activity销毁时调用
     * @see #finish()
     */
    @Override
    public void onDestroy(){

    }


    protected Intent getIntent() {
        return intent;
    }

    void setIntent(Intent intent){
        this.intent = intent;
    }

    public void finish(){
        ActivityManager manager=ActivityManager.instance();
        manager.finishActivity(this);
    }

    protected  <T extends Parent>T findViewById(String id){
        if(mScene!=null){
            return (T)mScene.lookup("#"+id);
        }
        return null;
    }

    protected void setContentView(String fxml) {
        FXMLLoader loader=new FXMLLoader(getFxmlLocation(fxml));
        loader.setController(null);
        loader.setController(this);
        try {
            Parent parent = loader.load();
            Group topGroup=new Group();
            topGroup.getChildren().add(parent);
            mScene=new Scene(topGroup,Color.WHITE);
        } catch (IOException e) {
            ErrorHandler.get().show(e);
        }
    }

    protected void onNewIntent(Intent intent){

    }

    protected void setContentView(Parent parent){
        if(parent!=null){
            mScene =new Scene(parent,Color.WHITE);
        }else {
            ErrorHandler.get().show(new Exception("parent 不能为null"));
        }
    }


    void setWindow(Window manager){
        appWindow =manager;
        appWindow.setLifecycle(this);
    }

    public Window getWindow() {
        return appWindow;
    }

    /**
     * 判断Activity是否一样，这里只判断Activity的类签名是否一致
     * @param act
     * @return
     */
    boolean isSame(Class<? extends Activity> act){
        return getClass().getTypeName().equals(act.getTypeName());
    }

    public boolean isShow(){
        return appWindow.isShowing();
    }

    void show(){
        appWindow.attachToWindow(this);
    }

    void hidden(){
        appWindow.closeWindow();

    }

    public Scene getScene() {
        return mScene;
    }


//    static class ActionBar{
//        private Window
//    }

}
