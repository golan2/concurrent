package golan.izik.concurrent;

import golan.izik.concurrent.log.ConcurrentLogger;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    07/07/13 20:47
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class TestIllegalMonitorStateException {

    private static final ConcurrentLogger log = new ConcurrentLogger();

    public static void main(String[] args) {

        log.printToLog("1.. ");
        synchronized (Thread.currentThread()) {

            log.printToLog("2..");

            final Thread notifier = new Thread(new Notifier(Thread.currentThread()));
            notifier.start();

            try {
                Thread.currentThread().wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            log.printToLog("3..");
        }

    }


    private static class Notifier implements Runnable {

        private final Thread thread;

        public Notifier(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {

            log.printToLog("Notifier 1");
            synchronized (this.thread) {
                log.printToLog("Notifier 2");
                try {
                    Thread.sleep(5000);
                    this.thread.notify();
                    log.printToLog("Notifier => Notify..");
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.printToLog("Notifier 3");
        }
    }


}
