package similar.control.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.inputmap.InputMap;
import similar.control.ListView;
//负责行为交互
public class ListViewBehavior extends BehaviorBase<ListView> {

    public ListViewBehavior(ListView node) {
        super(node);
    }

    @Override
    public InputMap<ListView> getInputMap() {
        return null;
    }


}
