package golan.izik.bitmap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    22/11/13 12:17
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * The filter is applied this way:
 * B[i,j] = S[i, j] / ( (2r+1)^2 )
 *
 * Explanations here:
 * http://xn--80afqipfk7a.xn--p1ai/2007/proceedings/Papers/Paper_70.pdf
 *
 * </pre>
 */
public class SingleThreadedLinearSmoothing {
    private static final String SRC_FILE_NAME = "C:\\Users\\golaniz\\Documents\\Izik\\Java\\Projects\\concurrent\\static_content\\3.bmp";
    private static final float[][] FILTER = {
        {0.0f, 0.0f, 0.0f},
        {1.0f, 1.0f, 1.0f},
        {1.0f, 1.0f, 1.0f}
    };


    protected final BufferedImage source;
    protected final BufferedImage result;


    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new File(SRC_FILE_NAME));

        printImage(image);
        FilterRGB filterRGB = new FilterRGB(FILTER);

        BufferedImage result = runFilter(image, filterRGB);

        printImage(result);

    }

    private static BufferedImage runFilter(BufferedImage image, FilterRGB filter) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int y=0 ; y<image.getHeight() ; y++) {
            for (int x=0 ; x<image.getWidth() ; x++) {
                result.setRGB(x,y, filter.calculate(image,x,y));
            }
        }



        return result;

    }

    private static void printImage(BufferedImage image) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("{\n");
        for (int y=0 ; y<image.getHeight() ; y++) {
            buffer.append("{");
            for (int x=0 ; x<image.getWidth() ; x++) {
                int rgb = image.getRGB(x, y);
                Color c = new Color(rgb);
                buffer.append(String.format("%03d", c.getRed()));
                buffer.append(String.format("%03d", c.getGreen()));
                buffer.append(String.format("%03d", c.getBlue()));
                if (x<image.getWidth()-1) {
                    buffer.append(",");
                }
            }
            buffer.append("}\n");
        }
        buffer.append("}");

        System.out.println(buffer.toString());
    }

    public SingleThreadedLinearSmoothing(BufferedImage source) {
        this.source = source;
        this.result = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
    }

    //radius is a parameter to the LinearSmoothing algorithm
    //public void applyFilter(flaot radius) {
    //    for (int y = source.getMinY(); y < source.getHeight(); y++) {
    //        for (int x=source.getMinX(); x < source.getWidth(); x++) {
    //            float color = (float) source.getRGB(x, y);
    //            int rgb = Math.round(color / ((2 * radius + 1) ^ 2));
    //            result.setRGB(x,y, rgb);
    //        }
    //    }
    //}

    public BufferedImage getResult() {
        return result;
    }

    private static class FilterRGB {
        private final int offsetX;
        private final int offsetY;
        private final float[][] filter;

        private FilterRGB(float[][] filter) {
            this.filter = filter;

            int cols = filter[0].length;
            int rows = filter.length;

            this.offsetX = (int) Math.floor(cols/2.0);
            this.offsetY = (int) Math.floor(rows/2.0);
        }


        public int calculate(BufferedImage image, int x, int y) {
            int   counter  = 0;
            float newRed   = 0;
            float newGreen = 0;
            float newBlue  = 0;

            //iterate filter matrix
            for (int row = 0; row < filter.length; row++) {
                float[] arr = filter[row];
                for (int col = 0; col < arr.length; col++) {

                    //get filter current value
                    float val = arr[col];

                    //find the corresponding coordinates of the pixel in image
                    int xx = x - offsetX + row;
                    int yy = y - offsetY + col;

                    if (withinBoundaries(image, xx, yy)) {
                        Color c = new Color(image.getRGB(xx,yy));

                        newRed   += c.getRed()   * filter[row][col];
                        newGreen += c.getGreen() * filter[row][col];
                        newBlue  += c.getBlue()  * filter[row][col];

                        counter++;
                    }
                }
            }

            newRed   /= counter;
            newGreen /= counter;
            newBlue  /= counter;

            return new Color(newRed, newGreen, newBlue).getRGB();
        }

        private boolean withinBoundaries(BufferedImage image, int xx, int yy) {return xx>=image.getMinX() && xx<image.getMinX()+image.getWidth() && yy>=image.getMinY() && yy<image.getMinY()+image.getHeight();}
    }
}
