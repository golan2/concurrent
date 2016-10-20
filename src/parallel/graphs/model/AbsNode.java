package parallel.graphs.model;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    14/11/13 09:53
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class AbsNode implements Node {
    protected final int id;
    protected final int x;
    protected final int y;

    AbsNode(int id, int x, int y) {
        this.y = y;
        this.id = id;
        this.x = x;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
