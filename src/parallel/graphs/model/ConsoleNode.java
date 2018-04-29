package parallel.graphs.model;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    13/11/13 23:35
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class ConsoleNode extends AbsNode {

    ConsoleNode(int id, int x, int y) {
        super(id, x, y);
    }



    @Override
    public String toString() {
        return "Node{" +
            "id=" + id +
            ", x=" + x +
            ", y=" + y +
            '}';
    }
}
