package similar.control;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.css.*;
import javafx.css.converter.PaintConverter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import similar.control.skin.ListViewSkin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//持有组件的属性，并且作为主的class
public class ListView extends Control {

    private static String LIST_VIEW_CLASS = "list-view-class";


    public ListView() {
        //给控件设置样式类名
        getStyleClass().add(LIST_VIEW_CLASS);
    }

    /*************************************
     *
     *  重写公共API
     * ***********************************/
    /*
    * 定制控件的外观
    * */
//    @Override
//    protected Skin<?> createDefaultSkin() {
//        return new ListViewSkin(this);
//    }

    @Override
    protected List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {

        return getClassCssMetaData();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    private StyleableObjectProperty<Paint> backgroundFill;

    public Paint getBackgroundFill() {
        return backgroundFill==null?Color.DARKGRAY:backgroundFill.get();
    }

    public StyleableObjectProperty<Paint> backgroundFillProperty() {
        if(backgroundFill==null){
            backgroundFill=new SimpleStyleableObjectProperty<>(
              StyleableProperties.BACKGROUND_FILL, ListView.this,"backgroundFill"
                    ,Color.DARKGRAY);
        }
        return backgroundFill;
    }

    public void setBackgroundFill(Color backgroundFill) {
        this.backgroundFill.set(backgroundFill);
    }



    private static class StyleableProperties{

        private static final CssMetaData<ListView,Paint> BACKGROUND_FILL=
                new CssMetaData<ListView,Paint>("-fx-custome-list-view-fill",
                        PaintConverter.getInstance(), Color.BLACK) {
                    @Override
                    public boolean isSettable(ListView styleable) {
                        return styleable.backgroundFill == null || !styleable.backgroundFill.isBound();
                    }

                    @Override
                    public StyleableProperty<Paint> getStyleableProperty(ListView styleable) {
                        return styleable.backgroundFillProperty();
                    }
                };
        private static final List<CssMetaData<? extends Styleable,?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable,?>> styleables=
                    new ArrayList<>(Control.getClassCssMetaData());
            Collections.addAll(styleables,BACKGROUND_FILL);
            STYLEABLES=Collections.unmodifiableList(styleables);
        }
    }














    private ObjectProperty<EventHandler<ActionEvent>> onAction = new ObjectPropertyBase<EventHandler<ActionEvent>>() {
        @Override
        public Object getBean() {
            return ListView.this;
        }

        @Override
        public String getName() {
            return "onAction";
        }

        @Override
        protected void invalidated() {
            setEventHandler(ActionEvent.ACTION, get());
        }
    };

    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return onAction;
    }

    public final EventHandler<ActionEvent> getOnAction() {
        return onActionProperty().
                get();
    }

    public final void setOnAction(EventHandler<ActionEvent> value) {
        onActionProperty().set(value);
    }


}
