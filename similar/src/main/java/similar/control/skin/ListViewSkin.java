package similar.control.skin;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.SkinBase;
import javafx.scene.paint.Color;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import similar.control.ListView;

//在此类中展示和布局子节点
public class ListViewSkin extends SkinBase<ListView> {
    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public ListViewSkin(ListView control) {
        super(control);
        control.widthProperty().addListener(observable ->
                invalidListView = true);
        control.heightProperty().addListener(observable ->
                invalidListView = true);
        control.backgroundFillProperty().addListener(observable ->
                updateTriangleColor());
    }


    public void updateTriangleColor() {
        if(path != null) {
            path.setFill(getSkinnable().getBackgroundFill());
            getSkinnable().requestLayout();
        }
    }

    private Path path;
    private boolean invalidListView = true;

    public void updateListView(double width,double height){
        if(path!=null){
            getChildren().removeAll(path);
        }
        path=new Path();
        path.getElements().add(new MoveTo(width / 2, 0));
        path.getElements().add(new LineTo(width, height));
        path.getElements().add(new LineTo(0, height));
        path.getElements().addAll(new ClosePath());
        path.setStroke(Color.BLACK);
        path.setFill(getSkinnable().getBackgroundFill());
        path.setOnMouseClicked(event -> {
            getSkinnable().fireEvent(new ActionEvent());
        });
        getChildren().add(path);

    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
//        super.layoutChildren(contentX, contentY, contentWidth, contentHeight);
        if(invalidListView){
            System.out.println("layoutChildren");
            updateListView(contentWidth,contentHeight);
            invalidListView=false;
        }
        layoutInArea(path,contentX,contentY,contentWidth,contentHeight,-1, HPos.CENTER, VPos.CENTER);
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return topInset+bottomInset+200;
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return leftInset+rightInset+200;
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 20+topInset+bottomInset;
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 20+leftInset+rightInset;
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefHeight(width,topInset,rightInset,bottomInset,leftInset);
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefWidth(height,topInset,rightInset,bottomInset,leftInset);
    }
}
