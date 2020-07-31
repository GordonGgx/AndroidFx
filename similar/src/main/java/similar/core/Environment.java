package similar.core;

import java.io.File;

public class Environment {

    private static final String USER_HOME=System.getProperty("user.home");
    private static final String APP_DIR=System.getProperty("user.dir","");
    private static final String TEMP_DIR=System.getProperty("java.io.tmpdir")+File.separator+"."+AndroidApplication.instance().getPackageName();;
    private static final String CACHE_DATA_DIR=USER_HOME+File.separator+"."+AndroidApplication.instance().getPackageName();

    public static File getInstallDir(){
        return new File(APP_DIR);
    }

    public static File getCacheDataDir(){
        File cacheDataDir=new File(CACHE_DATA_DIR);
        if(!cacheDataDir.exists()){
            cacheDataDir.mkdirs();
        }
        return cacheDataDir;
    }

    public static File getTempDir(){
        File tempDir=new File(TEMP_DIR);
        if(!tempDir.exists()){
            tempDir.mkdirs();
        }
        return tempDir;
    }

}
