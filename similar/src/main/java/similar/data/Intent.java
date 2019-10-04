package similar.data;

import similar.core.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Intent {


    private Map<String,Object> mMap=null;

    private Class<? extends Activity> aClass;

    public Intent(Class<? extends Activity> clazz){
        this.aClass=clazz;
    }

    public Intent putStringExtra(String key, String value){
        isNullCreated();
        mMap.put(key,value);
        return this;
    }
    public Intent putBooleanExtra(String key, boolean value){
        isNullCreated();
        mMap.put(key,value);
        return this;
    }

    public Intent putIntExtra(String key, int value){
        isNullCreated();
        mMap.put(key,value);
        return this;
    }

    public Intent putFloatExtra(String key, float value){
        isNullCreated();
        mMap.put(key,value);
        return this;
    }

    public Intent putDoubleExtra(String key, double value){
        isNullCreated();
        mMap.put(key,value);
        return this;
    }

    public Intent putShortExtra(String key, short value){
        isNullCreated();
        mMap.put(key,value);
        return this;
    }

    public Intent putCharExtra(String key, char value){
        isNullCreated();
        mMap.put(key,value);
        return this;
    }

    public Intent putStringArrayListExtra(String key, ArrayList<String> value){
        isNullCreated();
        mMap.put(key,value);
        return this;
    }

    public Intent putObject(String key, Object value){
        isNullCreated();
        mMap.put(key,value);
        return this;
    }

    public Class<? extends Activity> getActivityClass(){
        return aClass;
    }


    private void isNullCreated(){
        if (mMap==null){
            mMap=new HashMap<>(10);
        }
    }

}
