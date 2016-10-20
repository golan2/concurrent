package golan.izik.concurrent.shuffler;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    27/11/13 09:41
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class ShufflerApplet extends JApplet {
    private static final Color     COLOR_BACKGROUND_VERTEX_DEFAULT = Color.ORANGE;
    private static final Color     COLOR_FREGROUND_VERTEX_DEFAULT  = Color.BLACK;
    private static final Color     COLOR_FOREGROUND_EDGE_DEFAULT   = Color.BLACK;
    private static final Integer   LINE_WIDTH_DEFAULT              =    1;
    private static final Font      vertexLabelFont                 = new Font("Arial", Font.PLAIN, 10);
    private static final Font      edgeLabelFont                   = new Font("Arial", Font.PLAIN, 10);
    private static final Color     DEFAULT_BG_COLOR                = Color.decode("#FAFBFF");
    private static final int       APPLET_WIDTH                    = 1024;
    private static final int       APPLET_HEIGHT                   =  800;
    private static final Dimension DEFAULT_SIZE                    = new Dimension(APPLET_WIDTH, APPLET_HEIGHT);
    private static final int ARRAY_SIZE = 100;


    private static int[] generateArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        return arr;
    }



    /**
     * @see java.applet.Applet#init().
     */
    public void init(  ) {

        //try {
        //    ParallelShuffler shuffler = new ParallelShuffler();
        //    int[] arr = generateArray(ARRAY_SIZE);
        //    shuffler.setArr(arr);
        //    shuffler.shuffle();
        //    Statistics stats = shuffler.getPhaser().getStats();
        //    stats.generateStats();
        //
        //
        //    int maxX = 0;
        //    int maxY = 0;
        //    for (Node node : nodes.values()) {
        //        if (node.getX()>maxX) maxX = node.getX();
        //        if (node.getY()>maxY) maxY = node.getY();
        //    }
        //
        //    double ratioX = (double)APPLET_WIDTH/maxX;
        //    double ratioY = (double)APPLET_HEIGHT/maxY;
        //
        //    for (Node node : nodes.values()) {
        //        positionNode(node, ratioX, ratioY);
        //    }
        //
        //    JGraph jgraph = new JGraph( m_jgAdapter );
        //    adjustDisplaySettings( jgraph );
        //    getContentPane(  ).add( jgraph );
        //    resize( DEFAULT_SIZE );
        //
        //
        //}
        //catch (IOException e) {
        //    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        //}


    }

}
