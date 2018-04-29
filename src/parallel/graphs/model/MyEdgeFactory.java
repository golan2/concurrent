package parallel.graphs.model;

import org.jgrapht.EdgeFactory;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    14/11/13 09:52
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public interface MyEdgeFactory extends EdgeFactory<Node, Edge> {
    @Override
    Edge createEdge(Node node1, Node node2);

    Edge createEdge(Node node1, Node node2, int distance);
}
