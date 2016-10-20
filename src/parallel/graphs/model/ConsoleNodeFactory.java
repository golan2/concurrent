package parallel.graphs.model;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    14/11/13 09:58
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class ConsoleNodeFactory implements MyNodeFactory {
    @Override
    public Node createNode(int id, int x, int y) {
        return new ConsoleNode(id, x, y);
    }

}
