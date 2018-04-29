package golan.izik.concurrent.shuffler;

import java.util.Arrays;
import java.util.Random;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    09/07/13 09:15
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
@Deprecated
public class ShufflingArray {
    static void shuffle(int[] array) {
        try {
            Thread t1 = new ShufflingThread(array, 0, array.length / 2);
            Thread t2 = new ShufflingThread(array, array.length / 2 + 1, array.length-1);
            t1.start();
            t2.start();
            t1.join();
            t2.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int[] array = generateBigSortedArray();
        System.out.println(Arrays.toString(array));
        shuffle(array);
        System.out.println(Arrays.toString(array));
        for (int i = 0; i < array.length; i++) {
            if (i<array.length/2) {
                if (array[i]>array.length) {
                    throw new IllegalArgumentException("array["+i+"]="+array[i]);
                }
            }

        }

    }

    private static int[] generateBigSortedArray() {
        final int[] result = new int[10000];
        for (int i = 0; i < result.length; i++) {
            result[i] = i+1;
        }
        return result;
    }

    private static class ShufflingThread extends Thread {
        private final int[] array;
        private int left;
        private int right;

        public ShufflingThread(int[] array, int left, int right) {
            this.array = array;
            this.left = left;
            this.right = right;
        }

        @Override
        public void run() {
            // Implementing Fisher–Yates shuffle
            Random rnd = new Random();
            for (int i = 0 ; i< right-left+1 ; i++)
            {
                int index = rnd.nextInt(right-left+1);
                swap(left+i, left+index);
            }
        }

        private void swap(int index1, int index2) {
            if (index1<left) throw new IllegalArgumentException("left=["+left+"];index1=["+index1+"]");
            if (right<index1) throw new IllegalArgumentException("right=["+right+"];index1=["+index1+"]");
            if (index2<left) throw new IllegalArgumentException("left=["+left+"];index2=["+index2+"]");
            if (right<index2) throw new IllegalArgumentException("right=["+right+"];index2=["+index2+"]");
            int temp = array[index2];
            array[index2] = array[index1];
            array[index1] = temp;
        }
    }
}
