package parallel.graphs.model;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    14/11/13 09:49
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class AppletEdgeFactory implements MyEdgeFactory {
    @Override
    public Edge createEdge(Node node1, Node node2) {
        return new AppletEdge(node1, node2);
    }

    public Edge createEdge(Node node1, Node node2, int distance) {
        return new AppletEdge(node1, node2, distance);
    }
}
