package similar.core;

import javafx.stage.Stage;
import similar.core.window.WindowManager;
import similar.data.Intent;
import similar.util.ErrorHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * 管理和启动Activity
 */
class ActivityManager {

    private volatile static ActivityManager instance;

    private List<similar.core.annotations.Activity> activityInfo;

    private Stack<Activity> activityStack;

    private WindowManager windowManager;

    private ActivityManager(){
        activityInfo=new ArrayList<>();
        activityStack=new Stack<>();
    }

    public static ActivityManager instance(){
        if(instance==null){
            synchronized (ActivityManager.class){
                if(instance==null){
                    instance=new ActivityManager();
                }
            }
        }
        return instance;
    }

    public void setWindowManager(Stage stage){
        windowManager =new WindowManager(stage);
    }

    public void initActivity(SimilarApplication application){
        Class<?> clazz=application.getClass();
        similar.core.annotations.Activity[] activities=clazz.getAnnotationsByType(similar.core.annotations.Activity.class);
        if(activities.length==0){
            ErrorHandler.get().show(new Exception("缺少Activity信息"));
        }else {
            for (var act:activities){
                if(act.mainActivity()){
                    activityInfo.add(0,act);
                }else {
                    activityInfo.add(act);
                }
            }
        }
    }

    public static void clear(){
        if(instance!=null&&!instance.activityStack.empty()){
            Activity activity=instance.activityStack.pop();
            while (activity!=null){
                activity.onStop();
                activity.onDestroy();
                if(!instance.activityStack.empty()){
                    activity=instance.activityStack.pop();
                }else {
                    break;
                }
            }
        }
        instance=null;
    }

    /**
     * 将activity压入栈中
     * @param intent
     */
    private void pushActivityByStandard(Intent intent){
        try {
            Activity activity = intent.getActivityClass().getConstructor().newInstance();
            activity.setWindow(windowManager);
            activity.setIntent(intent);
            activity.onCreated();
            Activity before=top();
            activityStack.push(activity);
            activity.show();
            if(before!=null){
                before.hidden();
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            ErrorHandler.get().show(e);
        }


    }

    public void lunch(Intent intent){
        similar.core.annotations.Activity info=findActivityInfo(intent);
        if(info==null){
            throw new RuntimeException("can not found this activity "+intent.getActivityClass());
        }
        if(info.lunchMode()==LaunchMode.SIGNAL_TOP){
            pushActivityBySingleTop(intent);
        }else if(info.lunchMode()==LaunchMode.SIGNAL_TASK){
            pushActivityBySingleTask(intent);
        }else {
            pushActivityByStandard(intent);
        }
    }

    private void pushActivityBySingleTask(Intent intent){
        Class<? extends Activity> activityClass= intent.getActivityClass();
        int index=findActivity(activityClass);
        if(index==-1){
            pushActivityByStandard(intent);
        }else {
            //从当前Activity界面移出然后新的界面加入
            for (int i=activityStack.size()-1;i>index;i--)pop();
            Activity activity=activityStack.get(index);
            activity.onNewIntent(intent);
        }
    }

    private void pushActivityBySingleTop(Intent intent){
        Class<? extends Activity> activityClass= intent.getActivityClass();
        Activity topActivity=top();
        if(topActivity.isSame(activityClass)){
            topActivity.onNewIntent(intent);
        }else {
            pushActivityByStandard(intent);
        }
    }

    public void pop(){
        if(activityStack.isEmpty()){
            return;
        }
        Activity activity=activityStack.pop();
        activity.onStop();
        activity.onDestroy();
    }


    public Activity top(){
        if (activityStack.isEmpty()){
            return null;
        }
        return activityStack.peek();
    }


    public int findActivity(Class<? extends Activity> clazz){
        for (int index=activityStack.size()-1;index>=0;index--){
            Activity act=activityStack.get(index);
            if(act.isSame(clazz)){
                return index;
            }
        }
        return -1;
    }

    public void remove(Activity activity){
        activityStack.removeElement(activity);
        activity.onStop();
        activity.onDestroy();
        Activity top=top();
        if(top==null){
            windowManager.closeWindow();
        }else {
            top.show();

        }
    }

    public similar.core.annotations.Activity findActivityInfo(Intent intent){
        for (var act:activityInfo){
            if(act.name().getTypeName().equals(intent.getActivityClass().getTypeName())){
                return act;
            }
        }
        return null;
    }

    public similar.core.annotations.Activity getMainActivity(){
        if(activityInfo.isEmpty()){
            return null;
        }
        similar.core.annotations.Activity  main=activityInfo.get(0);
        if(main.mainActivity()){
            return main;
        }
        return null;
    }

}
