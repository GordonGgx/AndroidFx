package similar.core.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Log {

    private static final Level INFO=Level.INFO;
    private static final Level DEBUG=new LogLevel("Debug",802);
    private static final Level WARNING=Level.WARNING;
    private static final Level ERROR=new LogLevel("Error",804);


    private Log() {
    }


    /**
     * Send a {@link #DEBUG} log message..
     * @param msg The message you would like logged.
     */
    public static void d(Object msg) {
        println( DEBUG, msg);
    }

    /**
     * Send a {@link #DEBUG} log message and log the exception.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void d( Object msg, Throwable tr) {
        println( DEBUG,msg + "\n" + getStackTraceString(tr));
    }

    /**
     * Send an {@link #INFO} log message.
     * @param msg The message you would like logged.
     */
    public static void i(Object msg) {
         println(INFO, msg);
    }

    /**
     * Send a {@link #INFO} log message and log the exception.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void i( Object msg, Throwable tr) {
         println( INFO, msg + "\n" + getStackTraceString(tr));
    }

    /**
     * Send a {@link #WARNING} log message.
     * @param msg The message you would like logged.
     */
    public static void w( Object msg) {
         println( WARNING, msg);
    }

    /**
     * Send a {@link #WARNING} log message and log the exception.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void w( Object msg, Throwable tr) {
         println( WARNING, msg + "\n" + getStackTraceString(tr));
    }

    /*
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param tr An exception to log
     */
    public static void w( Throwable tr) {
         println( WARNING, getStackTraceString(tr));
    }

    /**
     * Send an {@link #ERROR} log message.
     * @param msg The message you would like logged.
     */
    public static void e( Object msg) {
         println( ERROR, msg);
    }

    /**
     * Send a {@link #ERROR} log message and log the exception.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
    public static void e( Object msg, Throwable tr) {
         println( ERROR, msg + "\n" + getStackTraceString(tr));
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
    private static void println(Level priority, Object msg) {
        Logger logger=Logger.getGlobal();
        StackTraceElement[] mStacks = Thread.currentThread().getStackTrace();
        var ste=mStacks[3];
        logger.logp(priority,ste.getClassName(), ste.getMethodName(),"at "+ste+"\n"+(msg==null?"null":msg.toString()));
    }
}
