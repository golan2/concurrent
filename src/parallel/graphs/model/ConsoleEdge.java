package parallel.graphs.model;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    14/11/13 00:12
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class ConsoleEdge extends AbsEdge {

    ConsoleEdge(Node node1, Node node2) {
        super(node1, node2);
    }

    ConsoleEdge(Node node1, Node node2, int weight) {
        super(node1, node2, weight);
    }

    @Override
    public String toString() {
        return "ConsoleEdge{" +
            "node1=" + node1 +
            ", node2=" + node2 +
            ", weight=" + weight +
            '}';
    }
}
