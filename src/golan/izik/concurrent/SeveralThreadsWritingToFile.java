package golan.izik.concurrent;

import golan.izik.concurrent.log.ConcurrentLogger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    10/07/13 18:56
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *http://codereview.stackexchange.com/questions/27201/reading-output-from-multiple-threads-and-writing-to-a-file
 * </pre>
 */
public class SeveralThreadsWritingToFile {

    private static final ConcurrentLogger log = new ConcurrentLogger();
    private static final int TASKS_COUNT = 35;
    private static final int THREAD_COUNT = 10;
    private static final String EMP_SUCCESS = "C:\\Users\\golaniz\\Documents\\Izik\\Java\\Projects\\concurrent\\Success.xml";
    private static final int EMPLOYEES_PER_TASK = 30;

    public static void main(String[] args) {
        try {

            final ArrayList<Future<String>> futureArray = new ArrayList<>(TASKS_COUNT);

            final ExecutorService execService = Executors.newCachedThreadPool();

            writeLineToFile("<A>");

            log.printToLog("Submitting tasks...");
            for (int i = 0; i < TASKS_COUNT; i++) {
                final Future<String> future = execService.submit(new EmployeeWriterTask(i));
                log.printToLog("Task["+i+"] submitted");
                futureArray.add(future);
            }

            log.printToLog("Waiting for results...");


            //waitForTasks1(futureArray);    //both wait methods work :-)
            waitForTasks2(execService);
            log.printToLog("Done waiting!");

            writeLineToFile("</A>");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static void waitForTasks2(ExecutorService execService) {
        boolean finished = false;
        execService.shutdown();
        int take = 1;
        while (!finished) {
            try {
                log.printToLog("Take #"+take++);
                finished = execService.awaitTermination(5, TimeUnit.SECONDS);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void waitForTasks1(ArrayList<Future<String>> futureArray) {
        final HashMap<Integer, String> resultMap = new HashMap<>();
        int take = 1;
        while (resultMap.size()<TASKS_COUNT) {
            log.printToLog("Take #"+take++);
            for (int i = 0; i < TASKS_COUNT; i++) {
                try {
                    if (!resultMap.containsKey(i)) {
                        Future<String> future = futureArray.get(i);
                        final String result = future.get(1, TimeUnit.SECONDS);
                        resultMap.put(i, result);
                        log.printToLog("Task["+i+"] result ["+result+"]");
                    }
                }
                catch (TimeoutException ignored) {}
                catch (ExecutionException | InterruptedException e1) {
                    e1.printStackTrace();
                }

            }
        }
    }

    private static void writeLineToFile(String line) throws IOException {BufferedWriter bufWriterSuccess = null;
        try {
            synchronized (EMP_SUCCESS) {
                final FileWriter writerSuccess = new FileWriter(EMP_SUCCESS, true);
                bufWriterSuccess = new BufferedWriter(writerSuccess);
                bufWriterSuccess.write(line);
                bufWriterSuccess.flush();
            }
        }
        finally {
            try {
                if (bufWriterSuccess!=null) {
                    synchronized (EMP_SUCCESS) {
                        bufWriterSuccess.flush();
                        bufWriterSuccess.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class EmployeeWriterTask implements Callable<String> {

        private final int taskId;

        public EmployeeWriterTask(int taskId) {
            this.taskId = taskId;
        }

        @Override
        public String call() throws Exception {
            log.printToLog("Task["+taskId+"] - call - BEGIN");
            final StringBuilder result = new StringBuilder();
            for (int i = 0; i < EMPLOYEES_PER_TASK; i++) {
                final int employeeId = taskId * EMPLOYEES_PER_TASK + i;
                writeLineToFile("<Employee id=\"" + employeeId + "\" taskId=\"" + taskId + "\"/>\n");
                result.append(employeeId).append(",");
                log.printToLog("Task[" + taskId + "] employeeId[" + employeeId + "]");
            }
            log.printToLog("Task["+taskId+"] - call - END - result="+result.toString());
            return result.toString();
        }

    }




}
