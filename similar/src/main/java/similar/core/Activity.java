package similar.core;

import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import similar.data.Intent;
import similar.utils.ErrorHandler;

import java.io.IOException;

public class Activity extends Context{

    public static final int ACTIVITY_SINGLE_TASK=1;
    public static final int ACTIVITY_SINGLE_TOP=2;
    public static final int ACTIVITY_STANDARD=3;

    private Stage window;

    private Intent intent;

    private Scene mScene;

    private ReadOnlyBooleanWrapper showing=new ReadOnlyBooleanWrapper(){
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
                window.setScene(mScene);
                onStart();
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
    protected void onStart(){

    }


    /**
     * 当Activity隐藏后调用
     */
    protected void onStop(){

    }

    /**
     * 当Activity销毁时调用
     * @see #finish()
     */
    protected void onDestroy(){

    }


    protected Intent getIntent() {
        return intent;
    }

    void setIntent(Intent intent){
        this.intent = intent;
    }

    public void finish(){
        ActivityManager manager=ActivityManager.instance();
        Activity currentTop=manager.remove(this);
        if(currentTop!=null){
            currentTop.show();
            System.gc();
        }else {
            //当前栈内没有Activity，则退出应用
            window.close();
        }

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

    /**
     * 更具启动模式打开新的界面，默认启动标准模式
     * @param intent
     */
    public void startActivity(Intent intent){
        startActivity(ACTIVITY_STANDARD, intent);
    }

    public void startActivity(int mode, Intent intent){
        ActivityManager activityManager=ActivityManager.instance();
        if(mode==ACTIVITY_SINGLE_TASK){
            activityManager.pushActivityBySingleTask(intent);
        }else if(mode==ACTIVITY_SINGLE_TOP){
            activityManager.pushActivityBySingleTop(intent);
        }else {
            activityManager.pushActivityByStandard(intent);
        }
    }

    void setWindow(Stage stage){
        this.window=stage;
    }

    public Stage getWindow() {
        return window;
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
}
