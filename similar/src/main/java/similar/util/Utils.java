package similar.util;

public class Utils {

    /**
     * 判断当前使用的JDK版本是否小于9
     *
     * @return true or false
     */
    public static boolean jdkLT9(){
        String version=System.getProperty("java.version");
        int code=Integer.parseInt(version.split("\\.")[0]);
        return code<9;
    }
}
