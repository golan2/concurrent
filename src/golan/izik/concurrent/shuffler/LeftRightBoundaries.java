package golan.izik.concurrent.shuffler;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    27/11/13 00:35
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public interface LeftRightBoundaries extends Comparable<LeftRightBoundaries>{
    int getLeft();

    int getRight();

    int getSegmentSize();
    
    void setGeneration(int generation);

    int getGeneration();

    void setParent(LeftRightBoundaries parent);

    LeftRightBoundaries getParent();
}
