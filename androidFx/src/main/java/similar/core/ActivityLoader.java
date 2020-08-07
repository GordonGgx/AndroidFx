package similar.core;

import similar.data.ComponentName;

import java.lang.reflect.InvocationTargetException;

class ActivityLoader {

    public Activity newActivity(ComponentName component){
        try {
            return (Activity) ClassLoader.getSystemClassLoader().loadClass(component.getCompleteName()).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("无法创建"+component.getCompleteName());
        }
    }

    public Class<? extends Activity> loadActivity(ComponentName component){
        try {
           return (Class<? extends Activity>) Class.forName(component.getCompleteName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
