package similar.core;

import javafx.stage.Stage;
import similar.data.Intent;
import similar.util.ErrorHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

class ActivityManager {

    private volatile static ActivityManager instance;

    private Stack<Activity> activityStack;

    private Stage window;

    private ActivityManager(){
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

    public void init(Stage stage){
        window=stage;
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
    public void pushActivityByStandard(Intent intent){
        try {
            Activity activity = intent.getActivityClass().getConstructor().newInstance();
            activity.setWindow(window);
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

    public void pushActivityBySingleTask(Intent intent){
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

    public void pushActivityBySingleTop(Intent intent){
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

    public Activity remove(Activity activity){
        activityStack.removeElement(activity);
        activity.onStop();
        activity.onDestroy();
        return top();
    }

}
