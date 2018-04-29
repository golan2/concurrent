package parallel.graphs.model;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    14/11/13 09:46
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class AppletEdge extends AbsEdge {
    public AppletEdge(Node node2, Node node1, int weight) {
        super(node1, node2, weight);
    }

    public AppletEdge(Node node1, Node node2) {
        super(node1, node2);
    }

    @Override
    public String toString() {
        return "<"+weight+">";
    }
}
