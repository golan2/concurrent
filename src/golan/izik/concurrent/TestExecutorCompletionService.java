package golan.izik.concurrent;

import golan.izik.concurrent.log.ConcurrentLogger;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    11/07/13 06:53
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class TestExecutorCompletionService {

    private static final ConcurrentLogger log = new ConcurrentLogger();
    private static final int THREAD_COUNT = 10;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
        ExecutorCompletionService<Long> completionService = new ExecutorCompletionService<>(threadPool);

        //Start all of my tasks which will return long values.
        log.printToLog("Submitting tasks...");
        for(int i=0; i < THREAD_COUNT; i++) {
            completionService.submit(new MyTask());
        }


        //Output the long values of all my tasks in order of completion.
        log.printToLog("Wait for finish...");
        for(int i=0; i < THREAD_COUNT; i++) {
            Future<Long> task = completionService.take();
            log.printToLog("Task " + task.toString() + " completed in " + task.get() + "ms" + System.getProperty("line.separator"));
        }

        threadPool.shutdown();
        log.printToLog("Done waiting!");
    }

    private static class MyTask implements Callable<Long> {
        @Override
        public Long call() throws Exception {
            long startTime = System.currentTimeMillis();
            log.printToLog("MyTask - call - BEGIN");

            for(int i=0; i < 100; i++) {
                Thread.sleep(50);
            }

            long endTime = System.currentTimeMillis();
            long result = endTime - startTime;
            log.printToLog("MyTask - call - END["+result+"]");
            return result;
        }
    }
}
