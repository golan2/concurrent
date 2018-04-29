package golan.izik.bitmap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    21/11/13 22:13
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class BitmapManipulations {

    private static final String SRC_FILE_NAME = "C:\\Users\\golaniz\\Documents\\Izik\\Java\\Projects\\concurrent\\static_content\\1.bmp";
    private static final String DST_FILE_NAME = "C:\\Users\\golaniz\\Documents\\Izik\\Java\\Projects\\concurrent\\static_content\\2.bmp";

    public static void main(String[] args) {

        smoothIt();
    }

    private static void smoothIt() {
        try {
            BufferedImage image = ImageIO.read(new File(SRC_FILE_NAME));
            SingleThreadedLinearSmoothing smoothing = new SingleThreadedLinearSmoothing(image);
            ImageIO.write(smoothing.getResult(), "bmp", new File(DST_FILE_NAME));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void forkJoinImageSquares(String fileName) {
        try {
            BufferedImage image = ImageIO.read(new File(fileName));
            ImageSquares imageSquares = new ImageSquares(image, new Rectangle(new Point(0,0), new Point(image.getWidth()-1,image.getHeight()-1)));
            ForkJoinPool pool = new ForkJoinPool();
            pool.submit(imageSquares);
            while (ImageProcessor.counter.get()>0) {
                Thread.sleep(1000);
            }
            System.out.println("Done!");
            ImageIO.write(image, "bmp", new File(fileName));

        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void forkJoinInterlaceColors(String fileName) {
        try {
            BufferedImage image = ImageIO.read(new File(fileName));
            ImageProcessor imageInterlace = new ImageInterlace(image, image.getMinY(), image.getHeight());
            ForkJoinPool pool = new ForkJoinPool();
            pool.submit(imageInterlace);
            while (!pool.isQuiescent()) {
                System.out.println(".");
                Thread.sleep(1000);
            }
            System.out.println("Done!");
            ImageIO.write(image, "bmp", new File(fileName));

        }
        catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void interlaceColors(String fileName) {
        try {
            BufferedImage image = ImageIO.read(new File(fileName));
            int color1 = image.getRGB(0, 0);
            int color2 = image.getRGB(1, 0);

            for (int x = image.getMinX() ; x<image.getWidth() ; x++) {
                for (int y = image.getMinY() ; y<image.getHeight() ; y++) {
                    if ((x*image.getWidth()+y)%2==0) {
                        image.setRGB(x,y,color1);
                    }
                    else {
                        image.setRGB(x,y,color2);
                    }
                }
            }
            ImageIO.write(image, "bmp", new File(fileName));

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
