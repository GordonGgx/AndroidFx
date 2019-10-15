package similar.control.skin;

import com.sun.javafx.scene.control.Properties;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventDispatcher;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import similar.control.VirtualScrollBar;

import java.util.BitSet;

public class VirtualFlow<T extends Node> extends Region {

    /**
     * Scroll events may request to scroll about a number of "lines". We first
     * decide how big one "line" is - for fixed cell size it's clear,
     * for variable cell size we settle on a single number so that the scrolling
     * speed is consistent. Now if the line is so big that
     * MIN_SCROLLING_LINES_PER_PAGE of them don't fit into one page, we make
     * them smaller to prevent the scrolling step to be too big (perhaps
     * even more than one page).
     */
    private static final int MIN_SCROLLING_LINES_PER_PAGE = 8;

    /**
     * 表示一个新的cell被创建 我们需要调用 processCSS 来处理它.
     */
    private static final String NEW_CELL = "newcell";

    private static final double GOLDEN_RATIO_MULTIPLIER = 0.618033987;

    private boolean touchDetected = false;
    private boolean mouseDown = false;

    /**
     * The width of the VirtualFlow the last time it was laid out. We
     * use this information for several fast paths during the layout pass.
     */
    double lastWidth = -1;

    /**
     * The height of the VirtualFlow the last time it was laid out. We
     * use this information for several fast paths during the layout pass.
     */
    double lastHeight = -1;

    /**
     * The number of "virtual" cells in the flow the last time it was laid out.
     * For example, there may have been 1000 virtual cells, but only 20 actual
     * cells created and in use. In that case, lastCellCount would be 1000.
     */
    int lastCellCount = 0;

    /**
     * We remember the last value for vertical the last time we laid out the
     * flow. If vertical has changed, we will want to change the max & value
     * for the different scroll bars. Since we do all the scroll bar update
     * work in the layoutChildren function, we need to know what the old value for
     * vertical was.
     */
    boolean lastVertical;

    /**
     * The position last time we laid out. If none of the lastXXX vars have
     * changed respective to their values in layoutChildren, then we can just punt
     * out of the method (I hope...)
     */
    double lastPosition;

    /**
     * The breadth of the first visible cell last time we laid out.
     */
    double lastCellBreadth = -1;

    /**
     * The length of the first visible cell last time we laid out.
     */
    double lastCellLength = -1;

    /**
     * The list of cells representing those cells which actually make up the
     * current view. The cells are ordered such that the first cell in this
     * list is the first in the view, and the last cell is the last in the
     * view. When pixel scrolling, the list is simply shifted and items drop
     * off the beginning or the end, depending on the order of scrolling.
     * <p>
     * This is package private ONLY FOR TESTING
     */
    final javafx.scene.control.skin.VirtualFlow.ArrayLinkedList<T> cells = new javafx.scene.control.skin.VirtualFlow.ArrayLinkedList<T>();

    /**
     * A structure containing cells that can be reused later. These are cells
     * that at one time were needed to populate the view, but now are no longer
     * needed. We keep them here until they are needed again.
     * <p>
     * This is package private ONLY FOR TESTING
     */
    final javafx.scene.control.skin.VirtualFlow.ArrayLinkedList<T> pile = new javafx.scene.control.skin.VirtualFlow.ArrayLinkedList<T>();

    /**
     * A special cell used to accumulate bounds, such that we reduce object
     * churn. This cell must be recreated whenever the cell factory function
     * changes. This has package access ONLY for testing.
     */
    T accumCell;

    /**
     * This group is used for holding the 'accumCell'. 'accumCell' must
     * be added to the skin for it to be styled. Otherwise, it doesn't
     * report the correct width/height leading to issues when scrolling
     * the flow
     */
    Group accumCellParent;

    /**
     * 该Group持有具体的列表试图
     */
    private Group sheet;
    private ObservableList<Node> sheetChildren;

    /**
     * 水平和垂直滚动条控件
     */
    private VirtualScrollBar hbar = new VirtualScrollBar(this);
    private VirtualScrollBar vbar = new VirtualScrollBar(this);

    /**
     * 用来构建一个视区，显示控件，viewportBreadth 和 viewportLength
     * 只是它的两个维度
     */
    private ClippedContainer clipView;

    /**
     * 当水平和垂直滚动条都可见时，必须要填充两个相交的右下角，因此
     * 该属性用来填充这块
     */
    private StackPane corner;

    private double lastX;
    private double lastY;
    private boolean isPanning = false;
    private boolean fixedCellSizeEnabled = false;

    private boolean needsReconfigureCells = false; // when cell contents are the same
    private boolean needsRecreateCells = false; // when cell factory changed
    private boolean needsRebuildCells = false; // when cell contents have changed
    private boolean needsCellsLayout = false;
    private boolean sizeChanged = false;

    private final BitSet dirtyCells = new BitSet();

    Timeline sbTouchTimeline;
    KeyFrame sbTouchKF1;
    KeyFrame sbTouchKF2;

    private boolean needBreadthBar;
    private boolean needLengthBar;
    private boolean tempVisibility = false;



    public VirtualFlow(){
        getStyleClass().add("virtual-flow");
        setId("virtual-flow");

        // 初始化内容
        //...sheet
        sheet=new Group();
        sheet.getStyleClass().add("sheet");
        sheet.setAutoSizeChildren(false);
        sheetChildren=sheet.getChildren();

        // --- clipView
        clipView = new ClippedContainer();
        clipView.setNode(sheet);
        getChildren().add(clipView);

        // --- accumCellParent
        accumCellParent = new Group();
        accumCellParent.setVisible(false);
        getChildren().add(accumCellParent);

        //禁用scrollBar的ScrollEvent管理
        //阻止事件传递给子级
        final EventDispatcher blockEventDispatcher=(event, tail) -> event;
        final EventDispatcher oldHsbEventDispatcher = hbar.getEventDispatcher();
        hbar.setEventDispatcher((event, tail) -> {
            if (event.getEventType() == ScrollEvent.SCROLL &&
                    !((ScrollEvent)event).isDirect()) {
                tail = tail.prepend(blockEventDispatcher);
                tail = tail.prepend(oldHsbEventDispatcher);
                return tail.dispatchEvent(event);
            }
            return oldHsbEventDispatcher.dispatchEvent(event, tail);
        });
        //阻止事件传递给子级
        final EventDispatcher oldVsbEventDispatcher = vbar.getEventDispatcher();
        vbar.setEventDispatcher((event, tail) -> {
            if (event.getEventType() == ScrollEvent.SCROLL &&
                    !((ScrollEvent)event).isDirect()) {
                tail = tail.prepend(blockEventDispatcher);
                tail = tail.prepend(oldVsbEventDispatcher);
                return tail.dispatchEvent(event);
            }
            return oldVsbEventDispatcher.dispatchEvent(event, tail);
        });
        //监听VirtualFlow区域的ScrollEvent
        setOnScroll(event -> {
            if (Properties.IS_TOUCH_SUPPORTED) {
                if (touchDetected == false &&  mouseDown == false ) {
                    //Scrollbar释放动画
                    startSBReleasedAnimation();
                }
                //计算滑动方向的增量
                double virtualDelta = 0.0;
                if (isVertical()){
                    switch (event.getTextDeltaYUnits()){
                        case PAGES:
                            break;
                    }
                }
            }
        });
    }

    // --- vertical
    /**
     * Indicates the primary direction of virtualization. If true, then the
     * primary direction of virtualization is vertical, meaning that cells will
     * stack vertically on top of each other. If false, then they will stack
     * horizontally next to each other.
     */

    public final BooleanProperty verticalProperty() {
        if (vertical == null) {
            vertical = new BooleanPropertyBase(true) {
                @Override protected void invalidated() {
                    pile.clear();
                    sheetChildren.clear();
                    cells.clear();
                    lastWidth = lastHeight = -1;
                    setMaxPrefBreadth(-1);
                    setViewportBreadth(0);
                    setViewportLength(0);
                    lastPosition = 0;
                    hbar.setValue(0);
                    vbar.setValue(0);
                    setPosition(0.0f);
                    setNeedsLayout(true);
                    requestLayout();
                }

                @Override
                public Object getBean() {
                    return javafx.scene.control.skin.VirtualFlow.this;
                }

                @Override
                public String getName() {
                    return "vertical";
                }
            };
        }
        return vertical;
    }

    private BooleanProperty vertical;
    public final void setVertical(boolean value) {
        verticalProperty().set(value);
    }

    public final boolean isVertical() {
        return vertical == null ? true : vertical.get();
    }

    static class ClippedContainer extends Region{
        private final Rectangle clipRect;
        private Node node;

        public ClippedContainer(){
            getStyleClass().add("clipped-container");
            clipRect = new Rectangle();
            clipRect.setSmooth(false);
            setClip(clipRect);

            super.widthProperty().addListener(valueModel -> {
                clipRect.setWidth(getWidth());
            });
            super.heightProperty().addListener(valueModel -> {
                clipRect.setHeight(getHeight());
            });
        }

        public Node getNode() { return this.node; }

        public void setNode(Node n) {
            this.node = n;
            getChildren().clear();
            getChildren().add(node);
        }

        public void setClipX(double clipX) {
            setLayoutX(-clipX);
            clipRect.setLayoutX(clipX);
        }

        public void setClipY(double clipY) {
            setLayoutY(-clipY);
            clipRect.setLayoutY(clipY);
        }
    }
}
