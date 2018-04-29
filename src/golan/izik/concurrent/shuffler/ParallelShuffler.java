package golan.izik.concurrent.shuffler;

import golan.izik.concurrent.log.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.ForkJoinPool;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    26/11/13 08:34
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class ParallelShuffler {
    private static final Log log = Log.createLog(ParallelShuffler.class);
    private static final int ARR_SIZE = 50;

    private final MyPhaser           phaser = new MyPhaser();
    private       ArrayList<Integer> arr    = null;


    public static void main(String[] args) {
        ParallelShuffler shuffler = new ParallelShuffler();
        ArrayList<Integer> arr = generateIntArray(ARR_SIZE);
        shuffler.setArr(arr);
        shuffler.shuffle();
        Statistics stats = shuffler.getPhaser().getStats();
        stats.generateStats();
        stats.sort();
        LinkedList<LeftRightBoundaries> segments = shuffler.getPhaser().getStats().getSegments();
        Collections.sort(segments);
        log.debug("Segments: " + Arrays.toString(segments.toArray()));
        log.debug(stats.toString());
        log.debug("main - END");
    }


    public void shuffle() {
        if (arr==null) return;
        ForkJoinPool pool = new ForkJoinPool();
        pool.submit(new ShuffleTask(arr, 0, ARR_SIZE-1, phaser));
        this.phaser.awaitAdvance();
    }

    private static ArrayList<Integer> generateIntArray(int size) {
        ArrayList<Integer> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(i);

        }
        return result;
    }

    public MyPhaser getPhaser() {
        return phaser;
    }

    public void setArr(ArrayList<Integer> arr) {
        this.arr = arr;
    }
}


