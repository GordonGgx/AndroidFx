package similar.core.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Log {

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    public static final Level test=new LogLevel("demo",801);

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;

    private Log() {
    }


    /**
     * Send a {@link #DEBUG} log message..
     * @param msg The message you would like logged.
     */
    public static void d(String msg) {
        println( DEBUG, msg);
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void d( String msg, Throwable tr) {
        println( DEBUG,msg + '\n' + getStackTraceString(tr));
    }

    /**
     * Send an {@link #INFO} log message.
     * @param msg The message you would like logged.
     */
    public static void i(String msg) {
         println(INFO, msg);
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void i( String msg, Throwable tr) {
         println( INFO, msg + '\n' + getStackTraceString(tr));
    }

    /**
     * Send a {@link #WARN} log message.
     * @param msg The message you would like logged.
     */
    public static void w( String msg) {
         println( WARN, msg);
    }

    /**
     * Send a {@link #WARN} log message and log the exception.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void w( String msg, Throwable tr) {
         println( WARN, msg + '\n' + getStackTraceString(tr));
    }

    /*
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log
     */
    public static void w( Throwable tr) {
         println( WARN, getStackTraceString(tr));
    }

    /**
     * Send an {@link #ERROR} log message.
     * @param msg The message you would like logged.
     */
    public static void e( String msg) {
         println( ERROR, msg);
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void e( String msg, Throwable tr) {
         println( ERROR, msg + '\n' + getStackTraceString(tr));
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     * @param tr An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    /**
     * Low-level logging call.
     * @param priority The priority/type of this log message
     * @param msg The message you would like logged.
     * @return The number of bytes written.
     */
    private static void println(int priority, String msg) {
        Logger logger=Logger.getGlobal();
        StackTraceElement[] mStacks = Thread.currentThread().getStackTrace();
        var ste=mStacks[3];
        logger.logp(Level.INFO,ste.getClassName(), ste.getMethodName(),"at "+ste+"\n"+msg);
    }
}
