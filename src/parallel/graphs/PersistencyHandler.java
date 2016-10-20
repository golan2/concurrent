package parallel.graphs;

import parallel.graphs.model.Edge;
import parallel.graphs.model.MyEdgeFactory;
import parallel.graphs.model.MyNodeFactory;
import parallel.graphs.model.Node;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * <B>Copyright:</B>   HP Software IL
 * <B>Owner:</B>       <a href="mailto:izik.golan@hp.com">Izik Golan</a>
 * <B>Creation:</B>    14/11/13 09:24
 * <B>Since:</B>       BSM 9.21
 * <B>Description:</B>
 *
 * </pre>
 */
public class PersistencyHandler {

    private static final File att48_xy = new File("C:\\Users\\golaniz\\Documents\\Izik\\Java\\Projects\\123Test\\static_content\\att48_xy.txt");
    private static final File att48_d  = new File("C:\\Users\\golaniz\\Documents\\Izik\\Java\\Projects\\123Test\\static_content\\att48_d.txt" );

    boolean successfullyInitialized = false;
    private Map<Integer,Node> nodes = new HashMap<Integer, Node>();
    private ArrayList<Edge> edges = new ArrayList<Edge>();
    private final MyEdgeFactory edgeFactory;
    private final MyNodeFactory nodeFactory;

    public PersistencyHandler(MyEdgeFactory edgeFactory, MyNodeFactory nodeFactory) {
        this.edgeFactory = edgeFactory;
        this.nodeFactory = nodeFactory;
    }

    public void init() throws IOException {
        successfullyInitialized = false;
        readNodesFromFile();
        readEdgesFromFile();
        successfullyInitialized = true;
    }

    private void readNodesFromFile() throws IOException {

        DataInputStream in = null;
        try{
            // Open the file that is the first
            // command line parameter
            FileInputStream fstream = new FileInputStream(att48_xy);
            // Get the object of DataInputStream
            in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int index = 0;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null)   {
                strLine = strLine.trim();
                if (strLine.length()>0) {
                    index ++;
                    final String[] split = strLine.split("[ ]+");
                    final int x = Integer.parseInt(split[0]);
                    final int y = Integer.parseInt(split[1]);
                    final Node node = nodeFactory.createNode(index, x, y);
                    nodes.put(index, node);
                }
            }
        }
        finally {
            if (in!=null) in.close();
        }
    }

    private void readEdgesFromFile() throws IOException {

        DataInputStream in = null;
        try{
            // Open the file that is the first
            // command line parameter
            FileInputStream fstream = new FileInputStream(att48_d);
            // Get the object of DataInputStream
            in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            int node1 = 0;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null)   {
                strLine = strLine.trim();
                if (strLine.length()>0) {
                    node1 ++;
                    final String[] split = strLine.split("[ ]+");
                    int node2 = 0;
                    for (String s : split) {
                        if (s.trim().length()>0) {
                            node2++;
                            final int distance = Integer.parseInt(s);
                            final Edge edge = edgeFactory.createEdge(nodes.get(node1), nodes.get(node2), distance);
                            edges.add(edge);
                        }
                    }
                }
            }
        }
        finally {
            if (in!=null) in.close();
        }
    }

    public Map<Integer, Node> getNodes() {
        return nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    /**
     * Create a new List with only a subList of edges.
     * We decide which subList to take and the size of subList according to {@link DensityFilterStrategy}
     * The edges are chosen by their weight.
     * [1] Copy all edges to result
     * [2] Sort result by weight (Ascending/Descending depends on {@link DensityFilterStrategy#smallestValues})
     * [3] Skip leading zeroes ({@link DensityFilterStrategy#ignoreZeroes})
     *
     * @param desiredDensity the density is PCT of edges you want from the total we have. value of 1 will bring all. value of 0 will bring none.
     * @param strategy how to choose the subList? smaller values? larger values? do you want zeroes? maybe random? use constants!!
     * @return
     */
    public List<Edge> getEdges(double desiredDensity, DensityFilterStrategy strategy) {
        ArrayList<Edge> result = new ArrayList<Edge>(edges);

        if (desiredDensity>1) throw new IllegalArgumentException("Density cannot be ["+desiredDensity+"]. It is expected in range [0,1]");
        if (desiredDensity<0) throw new IllegalArgumentException("Density cannot be ["+desiredDensity+"]. It is expected in range [0,1]");

        if (!DensityFilterStrategy.RANDOM.equals(strategy)) {
            Collections.sort(result, new EdgeWeightComparator(strategy.smallestValues));
        }

        int howManyEdgesWeWant = (int) Math.round(result.size() * desiredDensity);
        //fix rounding problems
        if (howManyEdgesWeWant>result.size()) howManyEdgesWeWant = result.size();
        if (howManyEdgesWeWant<0) howManyEdgesWeWant = 0;

        int startIndex = 0;
        if (strategy.ignoreZeroes) {
            while(result.get(startIndex).getWeight()==0) {
                startIndex++;
            }
        }

        int endIndex = startIndex+howManyEdgesWeWant-1;
        return result.subList(startIndex, endIndex);

    }

    private void calcDensity(int count, double avg) {
        double density = 0;
        for (Edge edge : edges) {
            if (edge.getWeight()<avg) density++;
        }
        density = density/count;
    }

    private double calcAvg(double count) {
        int sum = 0;
        for (Edge edge : edges) {
            sum += edge.getWeight();
        }
        return sum / (double) count;
    }


    public enum DensityFilterStrategy {
        LARGEST(true,false),
        SMALLEST(false,true),
        NON_ZERO_SMALLEST(true,true),
        RANDOM(true);


        private final boolean ignoreZeroes;
        private final boolean smallestValues;
        private final boolean randomValues;

        DensityFilterStrategy(boolean ignoreZeroes, boolean smallestValues) {
            this.ignoreZeroes = ignoreZeroes;
            this.smallestValues = smallestValues;
            this.randomValues = false;
        }

        DensityFilterStrategy(boolean randomValues) {
            this.randomValues = true;
            this.ignoreZeroes = false;   //never mind this value this is random!!
            this.smallestValues = false; //never mind this value this is random!!
        }
    }

    private static class EdgeWeightComparator implements Comparator<Edge> {
        private final boolean ascending;

        private EdgeWeightComparator(boolean ascendingOrder) {
            this.ascending = ascendingOrder;
        }

        @Override
        public int compare(Edge o1, Edge o2) {
            if (ascending){
                return (Integer.valueOf(o1.getWeight())).compareTo(o2.getWeight());
            }
            else {
                return (Integer.valueOf(o2.getWeight())).compareTo(o1.getWeight());
            }
        }
    }
}
