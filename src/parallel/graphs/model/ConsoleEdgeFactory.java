package parallel.graphs.model;

/**
* <pre>
* <B>Copyright:</B>   Izik Golan
* <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
* <B>Creation:</B>    14/11/13 09:23
* <B>Since:</B>       BSM 9.21
* <B>Description:</B>
*
* </pre>
*/
public class ConsoleEdgeFactory implements MyEdgeFactory {
    @Override
    public Edge createEdge(Node node1, Node node2) {
        return new ConsoleEdge(node1, node2);
    }

    @Override
    public Edge createEdge(Node node1, Node node2, int distance) {
        return new ConsoleEdge(node1, node2, distance);
    }
}
