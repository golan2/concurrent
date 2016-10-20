package parallel.pi_calculation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    13/11/13 21:58
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class PiCalculationMain {
    public static final float RADIUS       =  100;
    public static final int   POINTS_COUNT = 25000000;
    public static final int   CORE = 5;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final ArrayList<Point> allPoints = generatePoints();
        ExecutorService pool = Executors.newFixedThreadPool(CORE);
        final Set<Future<Double>> futures = new HashSet<Future<Double>>();
        final int chunkSize = POINTS_COUNT / CORE;
        for (int i=0 ; i<CORE ; i++) {
            int left = i * chunkSize;
            int right = left + chunkSize - 1;
            final Future<Double> future = pool.submit(new PiCalcWorker(allPoints, left, right, generateCircle()));
            futures.add(future);
        }

        double sum = 0;
        for (Future<Double> future : futures) {
            sum += future.get();
        }

        System.out.println(sum/CORE);


    }

    private static Circle generateCircle() {
        return new Circle(new Point(RADIUS,RADIUS), RADIUS);
    }

    private static ArrayList<Point> generatePoints() {
        final ArrayList<Point> points = new ArrayList<Point>(POINTS_COUNT);
        for (int i=0 ; i<POINTS_COUNT ; i++) {
            points.add(new Point(randomValue(),randomValue()));
        }
        return points;
    }

    private static float randomValue() {return (float) Math.random()*RADIUS*2;}


}
