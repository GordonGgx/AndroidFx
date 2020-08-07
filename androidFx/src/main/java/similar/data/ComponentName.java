package similar.data;

public final class ComponentName {

    private final String mPackage;
    private final String mClass;

    public ComponentName(String pkg,String cls){
        if (pkg == null) throw new NullPointerException("package name is null");
        if (cls == null) throw new NullPointerException("class name is null");
        mPackage = pkg;
        mClass = cls;
    }

    public ComponentName(Class<?> cls){
        mPackage=cls.getPackageName();
        mClass=cls.getSimpleName();
    }

    public String getPackage() {
        return mPackage;
    }

    public String getClassName() {
        return mClass;
    }

    public String getCompleteName(){
        return mPackage+"."+mClass;
    }


}
