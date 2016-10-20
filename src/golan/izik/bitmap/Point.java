package golan.izik.bitmap;

/**
* <pre>
* <B>Copyright:</B>   HP Software IL
* <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
* <B>Creation:</B>    22/11/13 12:12
* <B>Since:</B>       BSM 9.21
* <B>Description:</B>
*
* </pre>
*/
class Point {
    private final int x;
    private final int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "("+x+","+y+')';
    }
}
