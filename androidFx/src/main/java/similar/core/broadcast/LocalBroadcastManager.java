package similar.core.broadcast;

import similar.data.Intent;

public class LocalBroadcastManager {

    private static volatile LocalBroadcastManager mInstance;

    public static LocalBroadcastManager getInstance(){
        if(mInstance==null){
            synchronized (LocalBroadcastManager.class){
                if(mInstance==null){
                    mInstance=new LocalBroadcastManager();
                }
            }
        }
        return mInstance;
    }

    private LocalBroadcastManager(){

    }

    public void sendBroadcast(Intent intent){

    }
}
