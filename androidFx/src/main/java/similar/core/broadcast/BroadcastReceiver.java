package similar.core.broadcast;

import similar.core.Context;
import similar.data.Intent;

public abstract class BroadcastReceiver {


    public abstract void onReceive(Context context,Intent intent);
}
