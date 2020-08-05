package similar.core.broadcast;

import java.util.ArrayList;
import java.util.Iterator;

public class IntentFilter {

    private final ArrayList<String> mActions;

    public IntentFilter() {
        mActions=new ArrayList<>();
    }

    public IntentFilter(String action) {
        mActions=new ArrayList<>();
        addAction(action);
    }

    /**
     * Add a new Intent action to match against.  If any actions are included
     * in the filter, then an Intent's action must be one of those values for
     * it to match.  If no actions are included, the Intent action is ignored.
     *
     * @param action Name of the action to match, such as Intent.ACTION_VIEW.
     */
    public final void addAction(String action) {
        if (!mActions.contains(action)) {
            mActions.add(action.intern());
        }
    }

    /**
     * Return the number of actions in the filter.
     */
    public final int countActions() {
        return mActions.size();
    }

    /**
     * Return an action in the filter.
     */
    public final String getAction(int index) {
        return mActions.get(index);
    }

    /**
     * Is the given action included in the filter?  Note that if the filter
     * does not include any actions, false will <em>always</em> be returned.
     *
     * @param action The action to look for.
     *
     * @return True if the action is explicitly mentioned in the filter.
     */
    public final boolean hasAction(String action) {
        return action != null && mActions.contains(action);
    }

    /**
     * Match this filter against an Intent's action.  If the filter does not
     * specify any actions, the match will always fail.
     *
     * @param action The desired action to look for.
     *
     * @return True if the action is listed in the filter.
     */
    public final boolean matchAction(String action) {
        return hasAction(action);
    }

    /**
     * Return an iterator over the filter's actions.  If there are no actions,
     * returns null.
     */
    public final Iterator<String> actionsIterator() {
        return mActions != null ? mActions.iterator() : null;
    }
}
