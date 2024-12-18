package org.poo.app;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.ExchangeInput;

import java.math.BigDecimal;
import java.util.*;

@Getter @Setter
class DirectedWeightedGraph {

    // Inner class to represent an edge
    static class Edge {
        String to;
        BigDecimal rate;

        public Edge(String to, BigDecimal rate) {
            this.to = to;
            this.rate = rate;
        }

        @Override
        public String toString() {
            return "Target: " + to + ", Weight: " + rate;
        }
    }

    // Graph represented as an adjacency list
    private final Map<String, List<Edge>> adjacencyList;

    // Constructor
    public DirectedWeightedGraph() {
        this.adjacencyList = new HashMap<>();
    }

//    // Method to add a vertex
//    public void addNode(String node) {
//        if (!this.adjacencyList.containsKey(node)) {
//            this.adjacencyList.put(node, new ArrayList<>());
//        }
//    }

    // Method to add a directed weighted edge
    public void addEdge(String from, String to, BigDecimal rate) {
        if (!this.adjacencyList.containsKey(from)) {
            this.adjacencyList.put(from, new ArrayList<>());
        }
        if (!this.adjacencyList.containsKey(to)) {
            this.adjacencyList.put(to, new ArrayList<>());
        }

        List<Edge> targetList = this.adjacencyList.get(from);
        targetList.add(new Edge(to, rate));
    }

    // Method to get the edges of a vertex
//    public List<Edge> getEdges(int vertex) {
//        return adjacencyList.getOrDefault(vertex, Collections.emptyList());
//    }

    // Method to display the graph
    public void printGraph() {
        for (String vertex : adjacencyList.keySet()) {
            System.out.println("Vertex " + vertex + " -> " + adjacencyList.get(vertex));
        }
    }

    public BigDecimal dfs(DirectedWeightedGraph graph, String source, String target,
                      ArrayList<String> visited, BigDecimal cost) {
        visited.add(source);
        if (source.equals(target)) {
            return cost;
        }

        List<Edge> nearNodes = graph.getAdjacencyList().get(source);
        for (Edge edge : nearNodes) {
            if (!visited.contains(edge.to)) {
                BigDecimal rez = dfs(graph, edge.to, target, visited, edge.rate.multiply(cost));
                if (rez.doubleValue() != 0) {
                    return rez;
                }
            }
        }


        return new BigDecimal("0");
    }
}



@Getter @Setter
public class ExchangeRateList{
    public static ArrayList<ExchangeRate> rates;
    public static String dominant;
    public static DirectedWeightedGraph graph;
    public ExchangeRateList(ExchangeInput[] input) {
        rates = new ArrayList<ExchangeRate>();

        graph = new DirectedWeightedGraph();


        for (ExchangeInput exchangeInput : input) {
            rates.add(new ExchangeRate(exchangeInput.getFrom(), exchangeInput.getTo(), exchangeInput.getRate()));
            rates.add(new ExchangeRate(exchangeInput.getTo(), exchangeInput.getFrom(), 1 / exchangeInput.getRate()));

            graph.addEdge(exchangeInput.getFrom(), exchangeInput.getTo(), BigDecimal.valueOf(exchangeInput.getRate()));
            graph.addEdge(exchangeInput.getTo(), exchangeInput.getFrom(), BigDecimal.valueOf(1 / exchangeInput.getRate()));
        }
        getDominant();


        graph.printGraph();
        System.out.println("\n\n\n");
    }

    private void getDominant() {
        int nrEur = 0;
        int nrUsd = 0;
        for(ExchangeRate r : rates) {
            if(r.getFrom().equals("EUR"))
                nrEur++;
            if(r.getFrom().equals("USD"))
                nrUsd++;
        }
        dominant = nrEur > nrUsd ? "EUR" : "USD";
    }

    public static double convertRate(String From, String To) {
        ArrayList<String> visited = new ArrayList<>();
        BigDecimal z = graph.dfs(graph, From, To, visited, BigDecimal.valueOf(1));
//        System.out.println("dfs = " + z);
        double x = convertRate2(From, To);
//        System.out.println("normal = " + x);

        return z.doubleValue();
    }



    public static double convertRate2(String From, String To) {
        for(ExchangeRate r : rates) {
            if(r.getFrom().equals(From) && r.getTo().equals(To)) {
                return r.getRate();
            }
        }


        double FromToEuroRate = convertRate(From, dominant);
        double FromEuroRateToTo = convertRate(dominant, To);


        return FromEuroRateToTo * FromToEuroRate;
    }
}
