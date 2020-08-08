package similar.core;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.util.Duration;

public abstract class Window {



    //无标题栏
    public  static int FLAG_NO_TITLE=1;
    //全屏
    public  static int FLAG_FULL_SCREEN=2;

    private int mFlags=0;

    public void addFlags(int flags){
        setFlags(flags,flags);
    }

    public void clearFlags(int flags){
        setFlags(0,flags);
    }

    public void setFlags(int flags,int mask){
        mFlags = (mFlags&~mask)|(flags&mask);
        onWindowAttributesChanged();
    }

    public boolean hasFlag(int flag){
        return (mFlags&flag)!=0;
    }

    public abstract double getWith();

    public abstract void setWith(double with);

    public abstract double getHeight();

    public abstract void setHeight(double height);

    public abstract String getTitle();

    public abstract void setTitle(String title);

    public abstract void setLifecycle(ILifecycle lifecycle);

    public abstract void attachToWindow(Activity activity);

    public abstract void closeWindow();

    public abstract boolean isShowing();

     abstract boolean isCloseButtonClicked();


    abstract void onWindowAttributesChanged();
}
