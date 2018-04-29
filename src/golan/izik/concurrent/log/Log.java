package golan.izik.concurrent.log;


import golan.izik.concurrent.shuffler.ParallelShuffler;
import golan.izik.concurrent.shuffler.ShuffleTask;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    15/11/13 15:46
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class Log {
    public enum LOGLEVEL {DEBUG, INFO, ERROR}


    private static final Log logDebug = new Log(LOGLEVEL.DEBUG);
    private static final Log logInfo  = new Log(LOGLEVEL.INFO );
    private static final Log logError = new Log(LOGLEVEL.ERROR);


    private static final Class[]    debugClasses_ = {
        ParallelShuffler.class
        ,ShuffleTask.class
        //,MyPhaser.class
    };
    
    private static final String[] arrExcludedFileNames = {
        Log.class.getSimpleName()+".java"
        ,"Thread.java"
        ,"Method.java"
        ,"AppMain.java"
        ,"DelegatingMethodAccessorImpl.java"
        ,"NativeMethodAccessorImpl.java"
    };

    private static final Set<String> excludedFileNames = new HashSet<>(Arrays.asList(arrExcludedFileNames));
    private static final Set<Class>  debugClasses      = new HashSet<>(Arrays.asList(debugClasses_       ));


    private final boolean isDebugEnabled;
    private final boolean isInfoEnabled;

    public static Log createLog(Class clazz) {
        if (debugClasses.contains(clazz)) {
            return logDebug;
        }
        else {
            return logInfo;
        }
    }

    private Log(LOGLEVEL loglevel) {
        if (LOGLEVEL.DEBUG==loglevel) {
            isInfoEnabled = true;
            isDebugEnabled = true;
        }
        else if (LOGLEVEL.INFO==loglevel) {
            isInfoEnabled = true;
            isDebugEnabled = false;
        }
        else if (LOGLEVEL.ERROR==loglevel) {
            isInfoEnabled = false;
            isDebugEnabled = false;
        }
        else {
            throw new IllegalArgumentException("Unexpected value of loglevel {"+loglevel+"}");
        }
    }


    public void debug(String s) {
        if (isDebugEnabled) {
            System.out.println(generatePrefix() + " - " + s);
        }
    }

    public void error(String s) {
        System.out.println(generatePrefix() + " - " + s);
    }

    public void error(String s, Throwable e) {
        System.out.println(generatePrefix() + " - " + s);
        e.printStackTrace(System.out);
    }

    public void info(String s) {
        if (isInfoEnabled) {
            System.out.println(generatePrefix() + " - " + s);
        }
    }

    private String generatePrefix() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        StringBuilder buf = new StringBuilder();
        buf.append(System.currentTimeMillis());
        buf.append(" ~ [");
        buf.append(Thread.currentThread().getName());
        buf.append("]");
        int counter = 0;
        for (int i = stackTrace.length-1; i>=0 && counter<2 ; i--) {
            StackTraceElement ste = stackTrace[i];
            if (! excludedFileNames.contains(ste.getFileName()) ) {
                buf.append(" ~ (");
                buf.append(ste.getFileName());
                buf.append(":");
                buf.append(ste.getLineNumber());
                buf.append(")");
                counter++;
            }
        }

        return  buf.toString();
    }

}
