package golan.izik.concurrent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    18/11/2010 11:31:06
 * <B>Since:</B>       BSM 9.1
 * <B>Description:</B>
 * <p/>
 * </pre>
 */
public class TestSemiBoundedLinkedBlockingQueue {
    private static final int TASKS_COUNT = 10;

    public static void main(String[] args) {
        try {
            new TestSemiBoundedLinkedBlockingQueue().testMyThreadPoolExecutor();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void testMyThreadPoolExecutor() throws InterruptedException {
        final MyThreadPoolExecutor executorService = new MyThreadPoolExecutor();

        ArrayList<MyTask> tasks = new ArrayList<MyTask>();

        for (int i=0 ; i< TASKS_COUNT; i++) {

            MyTask task = new MyTask();
            tasks.add(task);
            executorService.submit(task);
        }

        while (executorService.getActiveCount()>0) {
            Thread.sleep(100);
        }

        int finishedCount = 0;
        for (MyTask task : tasks) {
            if (task.isFinished) finishedCount++;
            System.out.println(task);
        }

        System.out.println( "Game Over" );
    }

    private static class MyThreadPoolExecutor extends ThreadPoolExecutor {

        public MyThreadPoolExecutor() {
            //super(1,3,5000, TimeUnit.MILLISECONDS, new MyArrayBlockingQueue(3), new MyDiscardPolicy());
            super(1,3,5000, TimeUnit.MILLISECONDS, new SemiBoundedLinkedBlockingQueue(3), new MyDiscardPolicy());
        }

        @Override
        public Future<?> submit(Runnable task) {
            System.out.println("submit - task=["+task+"] poolSize=["+this.getPoolSize()+"]");
            return super.submit(task);    //To change body of overridden methods use File | Settings | File Templates.
        }

    }

    private static class Util {

        /**
         * Assuming that "r" is running a task of type {@link MyTask}
         * Dig into "r" with reflection and get MyTask out of it
         * @param r a
         * @return MyTask
         */
        public static String getMyTaskFromRunnable(Runnable r) {
            try {
                Field syncField = r.getClass().getDeclaredField("sync");
                syncField.setAccessible(true);
                Object task =  syncField.get(r);

                Field callableField = task.getClass().getDeclaredField("callable");
                callableField.setAccessible(true);
                Object callable = callableField.get(task);

                if (callable.getClass().getName().equals("java.util.concurrent.Executors$RunnableAdapter")) {
                    Field taskField = callable.getClass().getDeclaredField("task");
                    taskField.setAccessible(true);
                    return taskField.get(callable).toString();
                }
                else {
                    final MyTask result = (MyTask) callable;
                    return result.toString();

                }

            }
            catch (ClassCastException e) {
                return r.toString() + " ERR: " + e.getMessage();
            }
            catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
            catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
            return "UNKNOWN ("+r+")";
        }
    }

    private static class MyDiscardPolicy extends ThreadPoolExecutor.DiscardPolicy {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            System.out.println("rejectedExecution task=["+Util.getMyTaskFromRunnable(r)+"]");
            super.rejectedExecution(r, e);    //To change body of overridden methods use File | Settings | File Templates.
        }
    }

    private static class MyTask implements Runnable {
        public static AtomicInteger counter = new AtomicInteger(0);
        private final int id = counter.incrementAndGet();

        private boolean isStarted = false;
        private boolean isFinished = false;
        private static final int TASKS_COUNT = 10;

        @Override
        public void run() {
            isStarted = true;
            System.out.println("run - id=["+id+"] - BEGIN");
            try {
                for (int iteration =0 ; iteration < TASKS_COUNT; iteration++) {
                    //System.out.println("run - id=["+id+"] iteration=["+ iteration +"]");
                    Thread.sleep(100);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("run - id=["+id+"] - END");
            isFinished = true;
        }

        @Override
        public String toString() {
            return "MyTask{" +
                "id=" + id +
                ", isStarted=" + isStarted +
                ", isFinished=" + isFinished +
                '}';
        }
    }

    private static class MyArrayBlockingQueue extends ArrayBlockingQueue<Runnable> {

        public MyArrayBlockingQueue(int capacity) {
            super(capacity, true);
        }

        @Override
        public boolean offer(Runnable runnable) {
            final boolean result = super.offer(runnable);
            System.out.println("offer - runnable=["+Util.getMyTaskFromRunnable(runnable)+"] result=["+result+"]");
            return result;
        }
    }

    private static class SemiBoundedLinkedBlockingQueue extends LinkedBlockingQueue<Runnable> {
        private final int rejectCapacity;

        public SemiBoundedLinkedBlockingQueue(int capacity) {
            super();
            this.rejectCapacity = capacity;
        }

        @Override
        public boolean offer(Runnable runnable) {
            boolean result = super.offer(runnable);
            if (result && this.size()>= rejectCapacity) {
                System.out.println("offer - runnable=["+Util.getMyTaskFromRunnable(runnable)+"] result=["+result+"] - but saved in queue");
                return false;
            }
            System.out.println("offer - runnable=["+Util.getMyTaskFromRunnable(runnable)+"] result=["+result+"]");
            return result;
        }
    }


}
