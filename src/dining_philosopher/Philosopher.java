package dining_philosopher;

import golan.izik.concurrent.log.ConcurrentLogger;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    11/07/13 08:23
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class Philosopher implements Runnable {
    private static final ConcurrentLogger log = new ConcurrentLogger();
    private ReentrantLock leftChopStick;
    private ReentrantLock rightChopStick;
    private int Id;

    public AtomicBoolean isTummyFull=new AtomicBoolean(false);

    //To randomize eat/Think time
    private Random randomGenerator = new Random();

    private int noOfTurnsToEat=0;

    public int getId(){
        return this.Id;
    }
    public int getNoOfTurnsToEat(){
        return noOfTurnsToEat;
    }

    /****
     *
     * @param id Philosopher number
     *
     * @param leftChopStick a
     * @param rightChopStick a
     */
    public Philosopher(int id, ReentrantLock leftChopStick, ReentrantLock rightChopStick) {
        this.Id = id;
        this.leftChopStick = leftChopStick;
        this.rightChopStick = rightChopStick;
    }

    @Override
    public void run() {

        while ( !isTummyFull.get()) {
            try {
                think();
                if (pickupLeftChopStick() && pickupRightChopStick()) {
                    eat();
                }
                putDownChopSticks();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void think() throws InterruptedException {
        log.printToLog(String.format("Philosopher %s is thinking", this.Id));
        Thread.sleep(randomGenerator.nextInt(1000));
    }

    private void eat() throws InterruptedException {
        log.printToLog(String.format("Philosopher %s is eating", this.Id));
        noOfTurnsToEat++;
        Thread.sleep(randomGenerator.nextInt(1000));
    }

    private boolean pickupLeftChopStick() throws InterruptedException {
        return pickupChopStick(leftChopStick, "Left");
        //if (leftChopStick.tryLock(10, TimeUnit.MILLISECONDS)) {
        //    log.printToLog(String.format(
        //        "Philosopher %s pickedup Left ChopStick", this.Id));
        //    return true;
        //}
        //return false;
    }

    private boolean pickupRightChopStick() throws InterruptedException {
        return pickupChopStick(rightChopStick , "Right");
        //if (rightChopStick.tryLock(10, TimeUnit.MILLISECONDS)) {
        //    log.printToLog(String.format(
        //        "Philosopher %s pickedup Right ChopStick", this.Id));
        //    return true;
        //}
        //return false;
    }

    private boolean pickupChopStick(ReentrantLock chopStick, String side) throws InterruptedException {
        if (chopStick.tryLock(10, TimeUnit.MILLISECONDS)) {
            log.printToLog(String.format("Philosopher %s pickedup "+side+" ChopStick", this.Id));
            return true;
        }
        return false;
    }

    private void putDownChopSticks() {
        if (leftChopStick.isHeldByCurrentThread()) {
            leftChopStick.unlock();
            log.printToLog(String.format("Philosopher %s putdown Left ChopStick", this.Id));
        }
        if (rightChopStick.isHeldByCurrentThread()) {
            rightChopStick.unlock();
            log.printToLog(String.format("Philosopher %s putdown Right ChopStick", this.Id));
        }
    }
}





