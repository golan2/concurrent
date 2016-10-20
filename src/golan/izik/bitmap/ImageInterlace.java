package golan.izik.bitmap;

import java.awt.image.BufferedImage;

/**
* <pre>
* <B>Copyright:</B>   HP Software IL
* <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
* <B>Creation:</B>    22/11/13 12:11
* <B>Since:</B>       BSM 9.21
* <B>Description:</B>
*
* </pre>
*/
class ImageInterlace extends ImageProcessor {
    private final int           firstRow;
    private final int           lastRow;
    private final int           color1;
    private final int           color2;

    ImageInterlace(BufferedImage image, int firstRow, int lastRow) {
        super(image);
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.color1  = image.getRGB(0, 0);
        this.color2  = image.getRGB(1, 0);
    }

    @Override
    protected void compute() {
        if (firstRow==lastRow) {
            handleSingleRow();
        }
        else {
            //EX: {0..99} ==>  {0,49}{50,99}  ==>  {0..24}{24..49}{50..74}{75..99}
            int oneFirstRow = firstRow;
            int rowCount = (lastRow - firstRow) + 1;
            int oneLastRow = oneFirstRow + rowCount/2 - 1;
            int twoFirstRow = oneLastRow+1;
            int twoLastRow = lastRow;
            invokeAll(new ImageInterlace(image, oneFirstRow, oneLastRow), new ImageInterlace(image, twoFirstRow, twoLastRow));
        }

    }

    private void handleSingleRow() {
        System.out.println("computeDirectly row {"+this.firstRow+"}");
        int y = this.firstRow;
        for (int x = 0; x < image.getWidth(); x++) {
            if (y%2==0 && x%2==0 || y%2==1 && x%2==1) {
                image.setRGB(x,y,color1);
            }
            else {
                image.setRGB(x,y,color2);
            }
        }
    }

    @Override
    public String toString() {
        return "{" +
            "firstRow=" + firstRow +
            ", lastRow=" + lastRow +
            '}';
    }
}
