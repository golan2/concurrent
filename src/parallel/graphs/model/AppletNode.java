package parallel.graphs.model;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    14/11/13 09:59
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class AppletNode extends AbsNode {
    AppletNode(int id, int x, int y) {
        super(id, x, y);
    }

    @Override
    public String toString() {
        return "{"+id+"}";
    }
}
