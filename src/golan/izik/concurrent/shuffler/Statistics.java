package golan.izik.concurrent.shuffler;

import golan.izik.concurrent.log.Log;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * <pre>
 * <B>Copyright:</B>   Izik Golan
 * <B>Owner:</B>       <a href="mailto:golan2@hotmail.com">Izik Golan</a>
 * <B>Creation:</B>    27/11/13 00:34
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class Statistics {
    private final static Log log = Log.createLog(Statistics.class);

    private LinkedList<LeftRightBoundaries>                   segments = new LinkedList<>();
    private DefaultDirectedGraph<LeftRightBoundaries, MyEdge> graph;

    public void add(LeftRightBoundaries b) {
        segments.add(b);
    }

    public void sort() {
        Collections.sort(segments, new Comparator<LeftRightBoundaries>() {
            @Override
            public int compare(LeftRightBoundaries o1, LeftRightBoundaries o2) {
                if (o1.getSegmentSize()==o2.getSegmentSize()) {
                    return Integer.valueOf( o1.getLeft() ).compareTo( o2.getLeft() );
                }
                else {
                    return (-1)* Integer.valueOf( o1.getSegmentSize() ).compareTo( o2.getSegmentSize() );
                }
            }
        });
    }

    public void generateStats() {

        this.graph = new DefaultDirectedGraph<>(MyEdge.class);

        for (LeftRightBoundaries segment : segments) {
            graph.addVertex(segment);
        }

        boolean thereAreMore = true;
        while (thereAreMore)  {
            thereAreMore = false;      //if we won't find any {v1,v2,parent} then we know we are done
            for (LeftRightBoundaries v1 : graph.vertexSet()) {
                if (v1.getParent()==null) {
                    LeftRightBoundaries v2 = null;
                    LeftRightBoundaries parent = null;
                    Collection<LeftRightBoundaries> candidates = findPairCandiates(v1);
                    for (LeftRightBoundaries candidate : candidates) {
                        LeftRightBoundaries p = findParent(v1,candidate);
                        v2 = candidate;
                        if (p!=null) {
                            parent = p;
                        }
                    }

                    if (parent!=null) {
                        thereAreMore = true;
                        v1.setParent(parent);
                        v2.setParent(parent);
                        graph.addEdge(v1,parent);
                        graph.addEdge(v2,parent);

                        //throw new RuntimeException("The set")
                    }
                }
            }
        }

    }

    public LinkedList<LeftRightBoundaries> getSegments() {
        return segments;
    }

    public DefaultDirectedGraph<LeftRightBoundaries, MyEdge> getGraph() {
        return graph;
    }

    private LeftRightBoundaries findParent(LeftRightBoundaries v1, LeftRightBoundaries v2) {
        int minLeft = Math.min(v1.getLeft(), v2.getLeft());
        int maxRight = Math.max(v1.getRight(), v2.getRight());

        for (LeftRightBoundaries v : graph.vertexSet()) {
            if (v.getLeft()==minLeft && v.getRight()==maxRight) {
                return v;
            }
        }
        return null;
    }

    private Collection<LeftRightBoundaries> findPairCandiates(LeftRightBoundaries v1) {
        Collection<LeftRightBoundaries> result = new LinkedList<>();

        for (LeftRightBoundaries v2 : graph.vertexSet()) {
            if (sameGeneration(v1,v2) && sameSize(v1, v2) && adjacent(v1, v2)) {
                result.add(v2);
            }
        }
        return result;
    }

    private boolean sameGeneration(LeftRightBoundaries v1, LeftRightBoundaries v2) {
        return v1.getGeneration()==v2.getGeneration();
    }

    //same size is +1 / -1  because if segment size is odd (99) we divide it into 2 segments with different sizes (49,50)
    private static boolean sameSize(LeftRightBoundaries v1, LeftRightBoundaries v2) {
        return Math.abs(v2.getSegmentSize()-v1.getSegmentSize())<2 ;
    }

    private static boolean adjacent(LeftRightBoundaries v1, LeftRightBoundaries v2) {
        if (v1.getLeft()<v2.getLeft()) {
            return ( v1.getRight() == v2.getLeft()-1 );
        }
        else {
            return ( v2.getRight() == v1.getLeft()-1 );
        }
    }


    @Override
    public String toString() {
        return "Statistics { " + "\n" +
            "segments=" + segments + "\n" +
            "graph=" + graph + "\n" +
            '}';
    }

}
