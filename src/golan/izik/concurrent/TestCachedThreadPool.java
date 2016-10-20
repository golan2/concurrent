package golan.izik.concurrent;

import golan.izik.concurrent.log.ConcurrentLogger;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    07/07/13 21:53
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class TestCachedThreadPool {
    private static final ConcurrentLogger log = new ConcurrentLogger();

    public static void main(String argv[]) throws Exception
    {
        log.printToLog("main - BEGIN");
        ExecutorService es = Executors.newCachedThreadPool(new DaemonFactory());

        log.printToLog("submit");
        final ArrayList<Task> tasks = new ArrayList<Task>();
        for (int i = 0; i < 6; i++) {
            final Task task = new Task();
            tasks.add(task);
            final Future<?> future = es.submit(task);
        }

        // Without this, JVM will terminate before the daemon thread prints the
        // message, because JVM doesn't wait for daemon threads when
        // terminating:
        log.printToLog("waiting");
        es.awaitTermination(5, TimeUnit.SECONDS);
        for (Task task : tasks) {

        }


        log.printToLog("main - END");
    }

    private static class Task implements Runnable {

        @Override
        public void run() {
            try {
                log.printToLog("BEGIN");
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(1000);
                }
                log.printToLog("END");
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class DaemonFactory implements ThreadFactory
    {
        @Override
        public Thread newThread(Runnable r) {
            log.printToLog("create Demon");
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        }
    }



}
