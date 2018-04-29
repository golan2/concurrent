package golan.izik.bitmap;

import java.awt.image.BufferedImage;

/**
* <pre>
* <B>Copyright:</B>   Izik Golan
* <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
* <B>Creation:</B>    22/11/13 12:13
* <B>Since:</B>       BSM 9.21
* <B>Description:</B>
*
* </pre>
*/
class ImageSquares extends ImageProcessor {
    private final Rectangle boundingBox;

    public ImageSquares(BufferedImage image, Rectangle boundingBox) {
        super(image);
        this.boundingBox = boundingBox;
    }

    @Override
    protected void compute() {
        int width  = boundingBox.getBottomRight().getX()-boundingBox.getTopLeft().getX()+1;
        int height = boundingBox.getBottomRight().getY()-boundingBox.getTopLeft().getY()+1;

        System.out.println("compute ["+id+"]:" + boundingBox);

        if (width<10 && height<10) {
            drawRectBorder();
        }
        else {
            Rectangle[] rectangles = split();
            ImageSquares[] subTasks = new ImageSquares[rectangles.length];
            for (int i = 0; i < rectangles.length; i++) {
                subTasks[i] = new ImageSquares(image, rectangles[i]);
            }
            invokeAll(subTasks);
        }
        counter.decrementAndGet();
    }

    private Rectangle[] split() {
        Rectangle[] result = new Rectangle[2];
        int width  = boundingBox.getBottomRight().getX()-boundingBox.getTopLeft().getX()+1;
        int height = boundingBox.getBottomRight().getY()-boundingBox.getTopLeft().getY()+1;

        int maxX = boundingBox.getBottomRight().getX();
        int minX = boundingBox.getTopLeft().getX();
        int minY = boundingBox.getTopLeft().getY();
        int maxY = boundingBox.getBottomRight().getY();

        if (height>width) {
            //split into 2 rectangles: upper and lower

            int newHeight = height/2;
            Point upperRectBottomRight = new Point(maxX, minY + newHeight - 1);
            Rectangle upperRect = new Rectangle(boundingBox.getTopLeft(), upperRectBottomRight);
            Point lowerRectTopLeft = new Point(minX, minY + newHeight);
            Rectangle lowerRect = new Rectangle(lowerRectTopLeft, boundingBox.getBottomRight());
            result[0] = upperRect;
            result[1] = lowerRect;
        }
        else {
            //split into 2 rectangles: left and right

            int newWidth = width/2;
            Point leftRectBottomRight = new Point(minX+newWidth-1, maxY);
            Rectangle leftRect = new Rectangle(boundingBox.getTopLeft(), leftRectBottomRight);
            Point rightRectTopLeft = new Point(minX+newWidth, minY);
            Rectangle rightRect = new Rectangle(rightRectTopLeft, boundingBox.getBottomRight());
            result[0] = leftRect;
            result[1] = rightRect;
        }
        return result;
    }

    private void drawRectBorder() {
        System.out.println("drawRectBorder ["+id+"]");
        image.setRGB(this.boundingBox.getTopLeft().getX(), this.boundingBox.getTopLeft().getY(), 255);
    }
}
