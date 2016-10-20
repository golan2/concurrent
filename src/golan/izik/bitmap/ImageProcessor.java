package golan.izik.bitmap;

import java.awt.image.BufferedImage;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    22/11/13 00:34
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public abstract class ImageProcessor extends RecursiveAction {
    protected static       AtomicInteger counter = new AtomicInteger(0);
    protected        final BufferedImage image;
    protected        final int           id;

    public ImageProcessor(BufferedImage image) {
        this.image    = image;
        this.id = counter.incrementAndGet() ;
        System.out.println("ImageSquares ["+id+"]");
    }

}
