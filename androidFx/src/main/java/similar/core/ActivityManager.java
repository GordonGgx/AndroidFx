package similar.core;

import similar.data.ComponentName;
import similar.data.Intent;
import similar.util.ErrorHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 管理和启动Activity
 */
class ActivityManager {

    private volatile static ActivityManager instance;

    private final ActivityLoader activityLoader=new ActivityLoader();
    //记录应用中所有Activity的信息
    private final List<similar.core.annotations.Activity> activityInfo;

    private final Stack<Activity> activityStack;

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

    public void loadActivities(AndroidApplication application){
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
                if(activity.isShow()){
                    activity.onStop();
                }
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
     * 默认启动模式
     * 该模式每次都会创建一个新的Activity
     * 将activity压入栈中
     * 如果存在上一个Activity则隐藏上一个Activity
     */
    private void pushActivityByStandard(WindowManager manager,Intent intent){
        ComponentName componentName=intent.getComponentName();
        Activity current = activityLoader.newActivity(componentName);
        current.setWindow(manager);
        current.setIntent(intent);
        current.onCreated();
//        Activity before=top();
////        if(before!=null){
////            before.hidden();
////        }
        activityStack.push(current);
        current.show();

    }

    public void lunch(WindowManager manager,Intent intent){
        similar.core.annotations.Activity info=findActivityInfo(intent);
        ComponentName componentName=intent.getComponentName();
        if(info==null){
            throw new RuntimeException("can not found this activity info at "+componentName.getCompleteName());
        }
        if(info.lunchMode()==LaunchMode.SIGNAL_TOP){
            pushActivityBySingleTop(manager,intent);
        }else if(info.lunchMode()==LaunchMode.SIGNAL_TASK){
            pushActivityBySingleTask(manager,intent);
        }else {
            pushActivityByStandard(manager,intent);
        }
    }

    /**
     * 单例启动模式，该模式下如果栈内存在要启动的Activity则将该Activity之上的所有Activity通通出栈，
     * 在显示Activity,若不存在则转为标准启动模式
     */
    private void pushActivityBySingleTask(WindowManager manager,Intent intent){
        Class<? extends Activity> activityClass= activityLoader.loadActivity(intent.getComponentName());
        int index=findActivity(activityClass);
        if(index==-1){
            pushActivityByStandard(manager,intent);
        }else {
            //从栈顶的Activity界面移出然后新的界面加入
            for (int i=activityStack.size()-1;i>index;i--)
                pop();
            Activity activity=activityStack.get(index);
            if(!activity.isShow())
                activity.show();
            activity.onNewIntent(intent);

        }
    }


    /**
     * 栈顶启动模式，该模式下若要启动的Activity存在于栈顶则，则显示此Activity
     * 若不存在则转为标准模式
     */
    private void pushActivityBySingleTop(WindowManager manager,Intent intent){
        Class<? extends Activity> activityClass= activityLoader.loadActivity(intent.getComponentName());
        Activity topActivity=top();
        if(topActivity.isSame(activityClass)){
            topActivity.onNewIntent(intent);
        }else {
            pushActivityByStandard(manager,intent);
        }
    }

    private void pop(){
        if(activityStack.isEmpty()){
            return;
        }
        Activity activity=activityStack.pop();
        if(activity.isShow())
            activity.hidden();
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

    public void finishActivity(Activity activity){
        //当前正在显示的Activity
        Activity current=activityStack.peek();
        if(current==activity){
            //出栈
            activityStack.pop();
            activity.hidden();
            if(!activity.getWindowManager().isCloseButtonClicked())
                activity.onDestroy();
            //获取新的Activity显示
            Activity top=top();
            if(top!=null&&!top.isShow()){
                top.show();
            }
        }else {
            boolean success=activityStack.removeElement(activity);
            if(success){
                if(activity.isShow())
                    activity.hidden();
                activity.onDestroy();
            }
        }

    }

    public similar.core.annotations.Activity findActivityInfo(Intent intent){
        for (var act:activityInfo){
            if(act.name().getTypeName().equals(intent.getComponentName().getCompleteName())){
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
