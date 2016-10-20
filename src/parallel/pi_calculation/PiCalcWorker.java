package parallel.pi_calculation;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    13/11/13 21:58
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * https://computing.llnl.gov/tutorials/parallel_comp/#ExamplesPI
 *
 * </pre>
 */
public class PiCalcWorker implements Callable<Double> {
    public static final DecimalFormat format = new DecimalFormat("0.###E0");
    private final ArrayList<Point> allPoints;
    private final int              left;               //index in allPoints
    private final int              right;              //index in allPoints
    private final Circle           circle;

    public PiCalcWorker(ArrayList<Point> allPoints, int left, int right, Circle circle) {
        this.allPoints = allPoints;
        this.left = left;
        this.right = right;
        this.circle = circle;

        Point center = circle.getCenter();
        float radius = circle.getRadius();
        System.out.println("PiCalcWorker ==> Index: [" +format.format(left) + "," + format.format(right) + "] Center: " + center + " Radius: [" + radius + "]");
    }

    @Override
    public Double call() throws Exception {
        return calculate();
    }


    private double calculate() {
        int counter = 0;
        Point center = circle.getCenter();
        float radius = circle.getRadius();

        System.out.println("calculate ==> Index: [" +format.format(left) + "," + format.format(right) + "] Center: "+ center + " Radius: ["+radius+"]");



        for (int i=left ; i<=right ; i++)  {
            final Point point = allPoints.get(i);
            if ( pointInsideCircle(point.getX(), point.getY(), center.getX(), center.getY(), radius)) {
                counter++;
            }
        }

        System.out.println("Total=["+(right-left+1)+"] Counter=["+counter+"] ");

        return (counter/(right-left+1.0))*4;
    }

    /**
     * Calculate if the point (x,y) is inside a circle with center at (center_x,center_y) and with a given radius
     *
     * http://stackoverflow.com/questions/481144/equation-for-testing-if-a-point-is-inside-a-circle
     *
     * @param x coordinate of the point
     * @param y coordinate of the point
     * @param center_x coordinate of the center of the circle
     * @param center_y coordinate of the center of the circle
     * @param radius of the circle
     * @return true iif the point is inside the circle
     */
    private static boolean pointInsideCircle(float x, float y, float center_x, float center_y, float radius) {
        // (x - center_x)^2 + (y - center_y)^2 < radius^2
        return Math.pow(x - center_x,2) + Math.pow(y - center_y,2) < Math.pow(radius,2);
    }

}
