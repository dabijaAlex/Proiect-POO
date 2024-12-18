package org.poo.app;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.ExchangeInput;

import java.math.BigDecimal;
import java.util.*;


/**
 * oriented weighted graph that holds the paths to different currencies with their
 * associated rate
 */
@Getter @Setter
class DirectedWeightedGraph {
    private Map<String, List<Edge>> adjacencyList;

    class Edge {
        private String to;
        private BigDecimal rate;

        /**
         * constructor for an edge that has an already existing start
         * @param to
         * @param rate
         */
        public Edge(String to, BigDecimal rate) {
            this.to = to;
            this.rate = rate;
        }
    }

    /**
     * Constructor
     */
    public DirectedWeightedGraph() {
        this.adjacencyList = new HashMap<>();
    }

    /**
     * adds a path from a currency to another with the associated conv rate
     * @param from
     * @param to
     * @param rate
     */
    public void addEdge(final String from, final String to, final BigDecimal rate) {
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
    public BigDecimal dfs(final DirectedWeightedGraph graph, final String source,
                          final String target, final ArrayList<String> visited,
                          final BigDecimal cost) {
        visited.add(source);
        if (source.equals(target)) {
            return cost;
        }
        List<Edge> nearNodes = graph.getAdjacencyList().get(source);
        for (Edge edge : nearNodes) {
            if (!visited.contains(edge.to)) {
                BigDecimal rez = dfs(graph, edge.to, target, visited,
                        edge.rate.multiply(cost));
                if (rez.doubleValue() != 0) {
                    return rez;
                }
            }
        }
        return new BigDecimal("0");
    }
}



@Getter @Setter
public class ExchangeRateGraph {
    static DirectedWeightedGraph graph;

    /**
     * constructor that builds the graph based on input.
     * if there s From -> To (conv rate) there will be To -> From (1 / conv rate)
     * @param input
     */
    public ExchangeRateGraph(final ExchangeInput[] input) {
        graph = new DirectedWeightedGraph();

        for (ExchangeInput exchangeInput : input) {
            graph.addEdge(exchangeInput.getFrom(), exchangeInput.getTo(),
                    BigDecimal.valueOf(exchangeInput.getRate()));
            graph.addEdge(exchangeInput.getTo(), exchangeInput.getFrom(),
                    BigDecimal.valueOf(1 / exchangeInput.getRate()));
        }
    }


    /**
     * call dfs to get the conv rate From a currency to another
     * @param from
     * @param to
     * @return
     */
    public static double convertRate(final String from, final String to) {
        ArrayList<String> visited = new ArrayList<>();
        BigDecimal z = graph.dfs(graph, from, to, visited, BigDecimal.valueOf(1));
        return z.doubleValue();
    }

}
