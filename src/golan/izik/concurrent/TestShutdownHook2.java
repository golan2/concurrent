package golan.izik.concurrent;

import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    07/07/13 23:13
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class TestShutdownHook2 {



    private static long begin = System.currentTimeMillis();

    public static void main(String[] args) {
        printToLog("main - BEGIN");
        final ExecutorService executor = Executors.newCachedThreadPool(new MyTaskThreadFactory());
        Runtime.getRuntime().addShutdownHook(new ShutdownHook(executor));
        executor.submit(new MyTask(1));
        executor.submit(new MyTask(2));
        printToLog("main - END");
    }



    private static void printToLog(String message) {
        System.out.println("{" + (1000 + System.currentTimeMillis() - begin) + "} [" + Thread.currentThread().getId() + "] " + message);
    }

    private static class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            printToLog("MyTimerTask - run");
        }
    }

    private static class MyTask implements Runnable {
        private final int index;

        private MyTask(int index) {this.index = index;}

        @Override
        public void run() {
            printToLog("MyTask["+index+"] - run - BEGIN");
            //new Timer().scheduleAtFixedRate(new MyTimerTask(), new Date(), 5 * 1000);
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            printToLog("MyTask[" + index + "] - run - END");
        }
    }

    private static class ShutdownHook extends Thread {
        private final ExecutorService executor;

        public ShutdownHook(ExecutorService executor) {
            this.executor = executor;
        }

        @Override
        public void run() {
            printToLog("ShutdownHook - run - BEGIN");

            try {
                if(executor.awaitTermination(20, TimeUnit.SECONDS))
                    printToLog("All workers shutdown properly.");
                else {
                    printToLog(String.format("Maximum time limit of %s reached " +
                                                 "when trying to shut down workers. Forcing shutdown.", 20));
                    executor.shutdownNow();
                }
            } catch (InterruptedException interrupt) {
                printToLog("Shutdown hook interrupted by exception: " +
                               interrupt.getMessage());
            }

            printToLog("Shutdown hook is finished!");
        }
    }

    private static class MyTaskThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread result = new Thread(r, "MyTask");
            result.setDaemon(true);
            return result;
        }
    }
}
