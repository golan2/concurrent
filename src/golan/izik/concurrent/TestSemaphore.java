package golan.izik.concurrent;

import golan.izik.concurrent.log.ConcurrentLogger;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    07/07/13 21:15
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class TestSemaphore {

    private static final ConcurrentLogger log = new ConcurrentLogger();

    public static void main(String[] args) {

        Semaphore semaphore = new Semaphore(3);

        final ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < 6; i++) {
            Thread t = new Thread(new Task(semaphore));
            threads.add(t);
        }


        for (Thread thread : threads) {
            thread.start();
        }

    }

    private static class Task implements Runnable{

        private final Semaphore semaphore;

        public Task(Semaphore semaphore) {
            this.semaphore=semaphore;
        }

        @Override
        public void run() {
            log.printToLog("run - waiting...");
            try {
                semaphore.acquire();
                log.printToLog("run - acquired");
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                semaphore.release();
                log.printToLog("run - released");
            }
            log.printToLog("run - END");
        }
    }

}
