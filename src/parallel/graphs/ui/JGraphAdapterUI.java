package parallel.graphs.ui;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.util.ParallelEdgeRouter;
import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import parallel.graphs.GraphBuilder;
import parallel.graphs.PersistencyHandler;
import parallel.graphs.model.AppletEdgeFactory;
import parallel.graphs.model.AppletNodeFactory;
import parallel.graphs.model.Edge;
import parallel.graphs.model.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    14/11/13 09:15
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class JGraphAdapterUI extends JApplet {

    private static final Color     COLOR_BACKGROUND_VERTEX_DEFAULT = Color.ORANGE;
    private static final Color     COLOR_FOREGROUND_VERTEX_DEFAULT = Color.BLACK;
    private static final Color     COLOR_FOREGROUND_EDGE_DEFAULT   = Color.BLACK;
    private static final Integer   LINE_WIDTH_DEFAULT              =    1;
    private static final Font      vertexLabelFont                 = new Font("Arial", Font.PLAIN, 10);
    private static final Font      edgeLabelFont                   = new Font("Arial", Font.PLAIN, 10);
    private static final Color     DEFAULT_BG_COLOR                = Color.decode("#FAFBFF");
    private static final int       APPLET_WIDTH                    = 1024;
    private static final int       APPLET_HEIGHT                   =  800;
    private static final Dimension DEFAULT_SIZE                    = new Dimension(APPLET_WIDTH, APPLET_HEIGHT);

    private JGraphModelAdapter m_jgAdapter;
    private DirectedGraph<Node,Edge> graph;

    /**
     * @see java.applet.Applet#init().
     */
    public void init(  ) {

        try {
            PersistencyHandler persistencyHandler = new PersistencyHandler(new AppletEdgeFactory(), new AppletNodeFactory());
            persistencyHandler.init();
            Map<Integer,Node> nodes = persistencyHandler.getNodes();
            Iterable<Edge> edges = persistencyHandler.getEdges(.1, PersistencyHandler.DensityFilterStrategy.NON_ZERO_SMALLEST);


            graph = GraphBuilder.build(nodes, edges);

            createJGraphModelAdapter();

            int maxX = 0;
            int maxY = 0;
            for (Node node : nodes.values()) {
                if (node.getX()>maxX) maxX = node.getX();
                if (node.getY()>maxY) maxY = node.getY();
            }

            double ratioX = (double)APPLET_WIDTH/maxX;
            double ratioY = (double)APPLET_HEIGHT/maxY;

            for (Node node : nodes.values()) {
                positionNode(node, ratioX, ratioY);
            }

            JGraph jgraph = new JGraph( m_jgAdapter );
            adjustDisplaySettings( jgraph );
            getContentPane(  ).add( jgraph );
            resize( DEFAULT_SIZE );


        }
        catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    private void createJGraphModelAdapter() {

        //EdgeAttributes
        AttributeMap defaultEdgeAttributes = JGraphModelAdapter.createDefaultEdgeAttributes(graph);
        GraphConstants.setForeground(defaultEdgeAttributes, COLOR_FOREGROUND_EDGE_DEFAULT);
        GraphConstants.setLineColor(defaultEdgeAttributes,COLOR_FOREGROUND_EDGE_DEFAULT);
        GraphConstants.setLineWidth(defaultEdgeAttributes, LINE_WIDTH_DEFAULT);
        GraphConstants.setLabelAlongEdge(defaultEdgeAttributes, true);
        GraphConstants.setRouting(defaultEdgeAttributes, ParallelEdgeRouter.getSharedInstance());
        GraphConstants.setFont(defaultEdgeAttributes, edgeLabelFont);
        GraphConstants.setEndSize(defaultEdgeAttributes, 6);

        //VertexAttributes
        AttributeMap defaultVertexAttributes = JGraphModelAdapter.createDefaultVertexAttributes();
        GraphConstants.setBackground(defaultVertexAttributes, COLOR_BACKGROUND_VERTEX_DEFAULT);
        GraphConstants.setForeground(defaultVertexAttributes, COLOR_FOREGROUND_VERTEX_DEFAULT);
        GraphConstants.setFont(defaultVertexAttributes, vertexLabelFont);
        GraphConstants.setAutoSize(defaultVertexAttributes, false);

        m_jgAdapter = new JGraphModelAdapter<Node,Edge>(graph, defaultVertexAttributes, defaultEdgeAttributes);
    }

    private void printAttributeMap(AttributeMap defaultEdgeAttributes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object key : defaultEdgeAttributes.keySet()) {
            Object value = defaultEdgeAttributes.get(key);
            stringBuilder.append("("+key+","+value+") ");
        }
        System.out.println(stringBuilder.toString());
    }

    private void positionNode(Node node, double ratioX, double ratioY) {
        DefaultGraphCell cell = m_jgAdapter.getVertexCell( node );
        Map              attr = cell.getAttributes(  );
        Rectangle2D b    = GraphConstants.getBounds(attr);

        int x = (int) (node.getX()*ratioX);
        int y = (int) (node.getY()*ratioY);

        //GraphConstants.setBounds( attr, new Rectangle( x, y, (int)b.getWidth(), (int)b.getHeight()) );
        GraphConstants.setBounds( attr, new Rectangle( x, y, 40, 20 ) );

        Map cellAttr = new HashMap(  );
        cellAttr.put( cell, attr );
        m_jgAdapter.edit( cellAttr, null, null, null);
    }

    private void adjustDisplaySettings( JGraph jg ) {
        jg.setPreferredSize( DEFAULT_SIZE );

        Color  c        = DEFAULT_BG_COLOR;
        String colorStr = null;

        try {
            colorStr = getParameter( "bgcolor" );
        }
        catch( Exception e ) {}

        if( colorStr != null ) {
            c = Color.decode( colorStr );
        }

        jg.setBackground( c );

        //AttributeMap defaultEdgeAttributes = m_jgAdapter.getDefaultEdgeAttributes();
        //Font edgeLabelFont = new Font("Arial", Font.PLAIN, 18);
        //GraphConstants.setFont(defaultEdgeAttributes, edgeLabelFont);


    }


    private void positionVertexAt( Object vertex, int x, int y ) {
        DefaultGraphCell cell = m_jgAdapter.getVertexCell( vertex );
        Map              attr = cell.getAttributes(  );
        Rectangle2D b    = GraphConstants.getBounds(attr);

        GraphConstants.setBounds( attr, new Rectangle( x, y, (int)b.getWidth(), (int)b.getHeight()) );

        Map cellAttr = new HashMap(  );
        cellAttr.put( cell, attr );
        m_jgAdapter.edit( cellAttr, null, null, null);
    }
}
