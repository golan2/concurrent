package golan.izik.concurrent;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;


/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    07/07/13 23:47
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class TestShutdownHook {

    private ExecutorService executor = Executors.newCachedThreadPool(new WorkerThreadFactory());

    public static void main(String[] args) {
        TestShutdownHook d = new TestShutdownHook();
        d.run();
    }

    private void run() {
        final Timer timer = new Timer(true);
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("Task is running!");
                System.out.println(Thread.currentThread().isDaemon());
            }
        };

        Runnable worker = new Runnable() {
            @Override
            public void run() {
                timer.scheduleAtFixedRate(task, new Date(), 5 * 1000);
            }
        };

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Shutdown hook is being invoked!");

                try {
                    if(executor.awaitTermination(20, TimeUnit.SECONDS))
                        System.out.println("All workers shutdown properly.");
                    else {
                        System.out.println(String.format("Maximum time limit of %s reached " +
                                                             "when trying to shut down workers. Forcing shutdown.", 20));
                        executor.shutdownNow();
                    }
                } catch (InterruptedException interrupt) {
                    System.out.println("Shutdown hook interrupted by exception: " +
                                           interrupt.getMessage());
                }

                System.out.println("Shutdown hook is finished!");
            }
        });


        executor.submit(worker);

        System.out.println("Initializing shutdown...");
    }

    private class WorkerThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "Worker");
            t.setDaemon(true);
            return t;
        }
    }
}
