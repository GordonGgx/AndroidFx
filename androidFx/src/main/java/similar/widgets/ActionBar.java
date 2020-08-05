package similar.widgets;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.awt.*;

public class ActionBar extends BorderPane {

    private Label title;

    public ActionBar(){
        title=new Label("this is custom title");
        setCenter(title);
        SystemColor sc=SystemColor.window;
        Container container=new Container();
        Insets in=container.getInsets();
        System.out.println(in.bottom-in.top);

//        Color color=Color.color(sc.getRed(),sc.getGreen(),sc.getBlue());
//        setStyle("-fx-background-color:"+Color.web("#"+Integer.toHexString(sc.getRed())
//                +Integer.toHexString(sc.getGreen())+Integer.toHexString(sc.getBlue())));
        setStyle("-fx-background-color:red");
    }
}
