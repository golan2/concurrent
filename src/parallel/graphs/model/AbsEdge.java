package parallel.graphs.model;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    14/11/13 09:41
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class AbsEdge implements Edge {
    protected static final int DEFAULT_WEIGHT = 1;
    protected final Node node1;
    protected final Node node2;
    protected final int weight;

    public AbsEdge(Node node1, Node node2) {
        this(node2, node1, DEFAULT_WEIGHT);
    }

    public AbsEdge(Node node1, Node node2, int weight) {
        this.node2 = node2;
        this.weight = weight;
        this.node1 = node1;
    }

    @Override
    public Node getNode1() {
        return node1;
    }

    @Override
    public Node getNode2() {
        return node2;
    }

    @Override
    public int getWeight() {
        return weight;
    }
}
