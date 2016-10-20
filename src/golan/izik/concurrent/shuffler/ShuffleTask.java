package golan.izik.concurrent.shuffler;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.RecursiveAction;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    26/11/13 23:50
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class ShuffleTask extends RecursiveAction implements LeftRightBoundaries {
    //private static final Log log      = Log.createLog(ShuffleTask.class);
    public  static final int MIN_SIZE = 10;

    private final ArrayList<Integer> array;
    private final int                 left;
    private final int                 right;
    private final int                 segmentSize;
    private final MyPhaser            phaser;
    private       LeftRightBoundaries parent      = null;
    private       int                 generation  = 0;
    private       boolean             finished    = false;


    public ShuffleTask(ArrayList<Integer> array, int left, int right, MyPhaser phaser) {
        this.array = array;
        this.left = left;
        this.right = right;
        this.phaser = phaser;
        this.segmentSize = right - left + 1;
        this.phaser.register();
        this.phaser.getStats().add(this);
        //log.debug("ShuffleTask created ("+left+","+right+")");
    }

    @Override
    public int getLeft() {
        return left;
    }

    @Override
    public int getRight() {
        return right;
    }

    @Override
    public int getSegmentSize() {
        return segmentSize;
    }

    @Override
    public void setGeneration(int generation) {
        this.generation = generation;
        if (parent!=null) {
            parent.setGeneration(this.getGeneration()+1);
        }
    }

    @Override
    public int getGeneration() {
        return generation;
    }

    @Override
    public void setParent(LeftRightBoundaries parent) {
        this.parent = parent;
        if (parent!=null) {
            parent.setGeneration(this.getGeneration()+1);
        }
    }

    @Override
    public LeftRightBoundaries getParent() {
        return parent;
    }

    @Override
    protected void compute() {
        //log.debug("compute - BEGIN -  ("+left+","+right+")");

        if (segmentSize < MIN_SIZE) {

            // Implementing Fisher–Yates shuffle
            Random rnd = new Random();
            for (int i = 0 ; i< segmentSize; i++)
            {
                int index = rnd.nextInt(segmentSize);
                swap(left+i, left+index);
            }
        }
        else {
            int left1 = left;
            int right1 = left + segmentSize/2 - 1;
            int left2 = right1+1;
            int right2 = right;

            ShuffleTask t1 = new ShuffleTask(array, left1, right1, phaser);
            ShuffleTask t2 = new ShuffleTask(array, left2, right2, phaser);
            invokeAll(t1, t2);
        }

        this.phaser.arrive();
        this.finished = true;
        //log.debug("compute - END -  ("+left+","+right+")");
    }

    private void swap(int index1, int index2) {
        if (index1<left) throw new IllegalArgumentException("left=["+left+"];index1=["+index1+"]");
        if (right<index1) throw new IllegalArgumentException("right=["+right+"];index1=["+index1+"]");
        if (index2<left) throw new IllegalArgumentException("left=["+left+"];index2=["+index2+"]");
        if (right<index2) throw new IllegalArgumentException("right=["+right+"];index2=["+index2+"]");
        int temp = array.get(index2);
        array.set(index2, array.get(index1));
        array.set(index1, temp);
    }

    @Override
    public String toString() {
        return "G"+generation+"{" + left + "," + right + "}";
    }

    public boolean isFinished() {
        return finished;
    }


    @Override
    public int compareTo(LeftRightBoundaries rhs) {
        if (this.getGeneration()==rhs.getGeneration()) {
            return Integer.compare(this.getLeft(), rhs.getLeft());
        }
        else {
            return (-1) * Integer.compare(this.generation, rhs.getGeneration());        //G0 will be last
        }
    }
}
