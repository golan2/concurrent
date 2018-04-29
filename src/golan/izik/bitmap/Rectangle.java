package golan.izik.bitmap;

/**
* <pre>
* <B>Copyright:</B>   Izik Golan
* <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
* <B>Creation:</B>    22/11/13 12:12
* <B>Since:</B>       BSM 9.21
* <B>Description:</B>
*
* </pre>
*/
class Rectangle {
    private final Point topLeft;
    private final Point bottomRight;

    Rectangle(Point topLeft, Point bottomRight) {
        this.bottomRight = bottomRight;
        this.topLeft = topLeft;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    @Override
    public String toString() {
        return "{"+topLeft+","+bottomRight+'}';
    }
}
