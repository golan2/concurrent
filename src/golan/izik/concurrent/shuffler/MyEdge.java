package golan.izik.concurrent.shuffler;

/**
* <pre>
* <B>Copyright:</B>   HP Software IL
* <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
* <B>Creation:</B>    27/11/13 08:38
* <B>Since:</B>       BSM 9.21
* <B>Description:</B>
*
* </pre>
*/
public class MyEdge {
    private static int counter = 0;
    private int index = ++counter;
    @Override
    public String toString() {
        return "["+index+"]";
    }
}
