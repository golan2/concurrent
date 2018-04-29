package golan.izik.concurrent.log;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    10/07/13 23:02
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class ConcurrentLogger {

    private long begin = System.currentTimeMillis();

    public synchronized void printToLog(String message) {
        System.out.println("{"+(1000+System.currentTimeMillis()-begin)+"} ["+Thread.currentThread().getId()+"] " + message);
        System.out.flush();
    }
}
