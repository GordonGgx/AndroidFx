package similar.core;

import javafx.animation.FadeTransition;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import similar.core.annotations.Layout;
import similar.core.window.WindowManager;
import similar.data.Intent;
import similar.util.ErrorHandler;

import java.io.IOException;

public class Activity extends Context{

    private WindowManager windowManager;

    private Intent intent;

    private Scene mScene;

    private final ReadOnlyBooleanWrapper showing=new ReadOnlyBooleanWrapper(false){
        private boolean oldVisible;
        @Override
        protected void invalidated() {
            final boolean newVisible=get();
            if(oldVisible==newVisible){
                return;
            }
            oldVisible=newVisible;
            if(newVisible){
                //显示当前的Activity
                windowManager.attachToWindow(Activity.this);
            }else {
                onStop();
            }

        }

        @Override
        public Object getBean() {
            return Activity.this;
        }

        @Override
        public String getName() {
            return "showing";
        }
    };
    public Activity() {

    }


    /**
     * 当Activity被创建之后触发此方法
     */
    protected void onCreated() {
        Class<?> clazz=getClass();
        if(clazz.isAnnotationPresent(Layout.class)){
            Layout layout=clazz.getAnnotation(Layout.class);
            String fxml=layout.value();
            setContentView(fxml);
        }else {
            setContentView(new Group());
        }

    }


    /**
     * 当Activity显示调用
     */
    public void onStart(){

    }


    /**
     * 当Activity隐藏后调用
     */
    public void onStop(){

    }

    /**
     * 当Activity销毁时调用
     * @see #finish()
     */
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
        manager.remove(this);
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


    void setWindow(WindowManager manager){
        windowManager=manager;
    }

    public WindowManager getWindowManager() {
        return windowManager;
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
        return showing.get();
    }

    void show(){
        showing.setValue(true);
    }

    void hidden(){
        showing.setValue(false);
    }

    public Scene getScene() {
        return mScene;
    }
}
