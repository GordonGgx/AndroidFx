package similar.control;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import similar.control.skin.ListViewSkin;

//持有组件的属性，并且作为主的class
public class ListView extends Control {

    private static String LIST_VIEW_CLASS="list-view-class";

    public ListView(){
        //给控件设置样式类名
       getStyleClass().add(LIST_VIEW_CLASS);
    }

    /*************************************
     *
     *  重写公共API
     * ***********************************/
     /**/
    @Override
    protected Skin<?> createDefaultSkin() {
        return new ListViewSkin(this);
    }


}
