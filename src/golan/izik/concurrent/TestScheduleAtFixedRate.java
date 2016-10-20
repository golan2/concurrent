package golan.izik.concurrent;

import golan.izik.concurrent.log.ConcurrentLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    07/07/13 15:51
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class TestScheduleAtFixedRate {

    private static final ConcurrentLogger log = new ConcurrentLogger();

    private static int counter = 0;
    private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public static void main(String[] args) {
        Runnable myTask = new Runnable() {
            @Override
            public void run() {
                final ArrayList<String> strings = new ArrayList<String>();

                int threadCount = counter++;
                int sleepTime = new Random(System.currentTimeMillis()).nextInt(100)+1;

                log.printToLog("T=[" + threadCount + "] S=[" + sleepTime + "] - Begin");

                for (int i = 0; i < sleepTime*10000; i++) {
                    strings.add(java.util.UUID.randomUUID().toString());
                }
                Collections.sort(strings);


                //try {
                //    String hostName = "";
                //    for (int i = 0; i < sleepTime; i++) {
                //        final InetAddress byName = InetAddress.getByName("198.252.206.16");
                //        hostName = byName.getHostName();
                //
                //    }
                //    System.out.println("{" + System.currentTimeMillis() + "} H=[" + hostName + "] - End");
                //}
                ////catch (InterruptedException e) {
                ////    e.printStackTrace();
                ////}
                //catch (UnknownHostException e) {
                //    e.printStackTrace();
                //}
                log.printToLog("T=[" + threadCount + "] S=[" + sleepTime + "] - End");

            }
        };

        log.printToLog("Main Begin");

        final ScheduledFuture<?> future = executor.scheduleAtFixedRate(myTask, 0, 1, TimeUnit.MILLISECONDS);

        while (!future.isDone()) {
            try {
                Thread.sleep(5000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.printToLog("Not done...");
        }

        log.printToLog("Main End");
    }

}
