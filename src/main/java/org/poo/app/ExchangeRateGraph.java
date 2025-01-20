package org.poo.app;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.ExchangeInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * oriented weighted graph that holds the paths to different currencies with their
 * associated rate
 */
@Getter @Setter
class DirectedWeightedGraph {
    private Map<String, List<Edge>> adjacencyList = new HashMap<>();

    class Edge {
        private String to;
        private double rate;

        /**
         * constructor for an edge that has an already existing start
         * @param to
         * @param rate
         */
        Edge(final String to, final double rate) {
            this.to = to;
            this.rate = rate;
        }
    }


    /**
     * adds a path from a currency to another with the associated conv rate
     * @param from
     * @param to
     * @param rate
     */
    public void addEdge(final String from, final String to, final double rate) {
        if (!this.adjacencyList.containsKey(from)) {
            this.adjacencyList.put(from, new ArrayList<>());
        }
        if (!this.adjacencyList.containsKey(to)) {
            this.adjacencyList.put(to, new ArrayList<>());
        }

        List<Edge> targetList = this.adjacencyList.get(from);
        targetList.add(new Edge(to, rate));
    }

    /**
     * dfs algorithm that with each subsequent call, conv rate is multiplied so that
     * when a path is found we can directly return the cumulated conv rate over multiple
     * currencies
     * @param graph
     * @param source
     * @param target
     * @param visited
     * @param cost
     * @return
     */
    public double dfs(final DirectedWeightedGraph graph, final String source,
                          final String target, final ArrayList<String> visited,
                          final double cost) {
        visited.add(source);
        if (source.equals(target)) {
            return cost;
        }
        List<Edge> nearNodes = graph.getAdjacencyList().get(source);
        for (Edge edge : nearNodes) {
            if (!visited.contains(edge.to)) {
                double rez = dfs(graph, edge.to, target, visited,
                        edge.rate * cost);
                if (rez != 0) {
                    return rez;
                }
            }
        }
        return 0;
    }
}



@Getter @Setter
public class ExchangeRateGraph {
    private static DirectedWeightedGraph graph;
    private static ExchangeRateGraph instance;


    /**
     * constructor that builds the graph based on input.
     * if there s From -> To (conv rate) there will be To -> From (1 / conv rate)
     * @param input
     */
    private ExchangeRateGraph(final ExchangeInput[] input) {
        graph = new DirectedWeightedGraph();

        for (ExchangeInput exchangeInput : input) {
            graph.addEdge(exchangeInput.getFrom(), exchangeInput.getTo(),
                    exchangeInput.getRate());
            graph.addEdge(exchangeInput.getTo(), exchangeInput.getFrom(),
                    1 / exchangeInput.getRate());
        }
    }

    /**
     * get th instance of class
     * prevents creation of multiple useless instances as they are useless
     * @param input
     * @return
     */
    public static ExchangeRateGraph getInstance(final ExchangeInput[] input) {
        if (instance == null) {
            instance = new ExchangeRateGraph(input);
        }
        return instance;
    }


    /**
     * call dfs to get the conv rate From a currency to another
     * @param from
     * @param to
     * @return
     */
    private static double convertRate(final String from, final String to) {
        ArrayList<String> visited = new ArrayList<>();
        double z = graph.dfs(graph, from, to, visited, 1);
        return z;

    }

    /**
     * make direct conversion from one currency to another
     * @param from
     * @param to
     * @param amount
     * @return
     */
    public static double makeConversion(final String from, final String to, final double amount) {
        double convRate = 1;
        convRate = convertRate(from, to);
        return amount * convRate;
    }

    /**
     * reset instance for following tests
     */
    public static void resetInstance() {
        instance = null;
    }

}
