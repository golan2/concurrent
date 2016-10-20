package golan.izik.canvas.recursion_trees;

import javax.swing.*;
import java.awt.*;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    22/11/13 13:50
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class AppletUI extends JApplet {

    private static final Color     DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
    private static final int       APPLET_WIDTH     = 1024;
    private static final int       APPLET_HEIGHT    =  800;
    private static final Dimension DEFAULT_SIZE     = new Dimension(APPLET_WIDTH, APPLET_HEIGHT);


    @Override
    public void init() {
        getContentPane().add( new DrawingCanvas() {} );
    }
    

    private static class DrawingCanvas extends Component {
        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(Color.RED);
            g.drawLine(0,100, 100,200);
        }
    }
}
