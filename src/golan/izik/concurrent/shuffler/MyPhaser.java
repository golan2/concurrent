package golan.izik.concurrent.shuffler;

import golan.izik.concurrent.log.Log;

import java.util.concurrent.Phaser;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    26/11/13 23:53
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class MyPhaser extends Phaser {
    private static final Log log = Log.createLog(MyPhaser.class);

    private final Statistics stats = new Statistics();


    @Override
    public int register() {
        log.debug("register"+this);
        return super.register();
    }

    @Override
    public int arrive() {
        log.debug("arrive"+this);
        return super.arrive();
    }

    @Override
    public int awaitAdvance(int phase) {
        log.debug("awaitAdvance phase=["+phase+"]"+this);
        return super.awaitAdvance(phase);
    }
    
    public int awaitAdvance() {
        return awaitAdvance(getPhase());
    }

    public Statistics getStats() {
        return stats;
    }
}
