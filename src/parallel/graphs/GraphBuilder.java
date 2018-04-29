package parallel.graphs;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import parallel.graphs.model.Edge;
import parallel.graphs.model.ConsoleEdgeFactory;
import parallel.graphs.model.Node;

import java.io.IOException;
import java.util.Map;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    14/11/13 09:30
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class GraphBuilder {
    public static DirectedGraph<Node, Edge> build(Map<Integer,Node> nodes, Iterable<Edge> edges) throws IOException {
        final DirectedGraph<Node, Edge> graph = new DefaultDirectedGraph<Node, Edge>(new ConsoleEdgeFactory());

        for (Node node : nodes.values()) {
            graph.addVertex(node);
        }

        for (Edge edge : edges) {
            if (edge.getWeight()>=0) {
                graph.addEdge(edge.getNode1(), edge.getNode2(), edge);
            }
        }
        return graph;
    }
}
