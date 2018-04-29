package parallel.graphs.ui;

import org.jgrapht.DirectedGraph;
import parallel.graphs.GraphBuilder;
import parallel.graphs.PersistencyHandler;
import parallel.graphs.model.ConsoleEdgeFactory;
import parallel.graphs.model.ConsoleNodeFactory;
import parallel.graphs.model.Edge;
import parallel.graphs.model.Node;

import java.io.IOException;
import java.util.Map;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    13/11/13 23:19
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class ConsoleUI {

    public static void main(String[] args) {

        try {
            final PersistencyHandler persistencyHandler = new PersistencyHandler(new ConsoleEdgeFactory(), new ConsoleNodeFactory());
            persistencyHandler.init();
            Map<Integer,Node> nodes = persistencyHandler.getNodes();
            Iterable<Edge> edges = persistencyHandler.getEdges();

            int count = 0;
            int sum = 0;
            for (Edge edge : edges) {
                sum += edge.getWeight();
                count++;
            }

            final double avg = sum / (double) count;
            double density = 0;
            for (Edge edge : edges) {
                if (edge.getWeight()<avg) density++;
            }
            density = density/count;


            System.out.println("AVG=["+avg+"] COUNT=["+count+"] EXPECTED DENSITY=["+density+"]");


            
            final DirectedGraph<Node, Edge> graph = GraphBuilder.build(nodes, edges);
            System.out.println(graph);
        }
        catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}
