//package org.poo.app;
//
//import lombok.Getter;
//import lombok.Setter;
//
//import java.util.*;
//
//@Getter @Setter
//public class DirectedWeightedGraph {
//
//    // Inner class to represent an edge
//    static class Edge {
//        int target; // Target node
//        double weight; // Weight of the edge
//
//        public Edge(int target, double weight) {
//            this.target = target;
//            this.weight = weight;
//        }
//
//        @Override
//        public String toString() {
//            return "Target: " + target + ", Weight: " + weight;
//        }
//    }
//
//    // Graph represented as an adjacency list
//    private final Map<Integer, List<Edge>> adjacencyList;
//
//    // Constructor
//    public DirectedWeightedGraph() {
//        this.adjacencyList = new HashMap<>();
//    }
//
//    // Method to add a vertex
//    public void addVertex(int vertex) {
//        if(!this.adjacencyList.containsKey(vertex)) {
//            this.adjacencyList.put(vertex, new ArrayList<>());
//        }
//    }
//
//    // Method to add a directed weighted edge
//    public void addEdge(int source, int target, double weight) {
//        if(!this.adjacencyList.containsKey(source)) {
//            this.adjacencyList.put(source, new ArrayList<>());
//        }
//        if(!this.adjacencyList.containsKey(target)) {
//            this.adjacencyList.put(target, new ArrayList<>());
//        }
//
//        List<Edge> targetList = this.adjacencyList.get(source);
//        targetList.add(new Edge(target, weight));
//    }
//
//    // Method to get the edges of a vertex
//    public List<Edge> getEdges(int vertex) {
//        return adjacencyList.getOrDefault(vertex, Collections.emptyList());
//    }
//
//    // Method to display the graph
//    public void printGraph() {
//        for (int vertex : adjacencyList.keySet()) {
//            System.out.println("Vertex " + vertex + " -> " + adjacencyList.get(vertex));
//        }
//    }
//
//    public double dfs(DirectedWeightedGraph graph, int source, int target,
//                      ArrayList<Integer> visited, double cost) {
//        visited.add(source);
//        if(source == target) {
//            return cost;
//        }
//
//        List<Edge> nearNodes = graph.getAdjacencyList().get(source);
//        for (Edge edge : nearNodes) {
//            if(!visited.contains(edge.target)) {
//                double rez = dfs(graph, edge.target, target, visited, cost * edge.weight);
//                if(rez != 0) {
//                    return rez;
//                }
//            }
//        }
//
//
//
//        return 0;
//    }
//
//    public static void main(String[] args) {
//        // Example usage
//        DirectedWeightedGraph graph = new DirectedWeightedGraph();
//
//
//        // Adding edges
//        graph.addEdge(1, 2, 4.5);
//        graph.addEdge(1, 3, 3.2);
//        graph.addEdge(2, 3, 1.7);
//        graph.addEdge(5, 4, 1.0);
//
//        ArrayList<Integer> visited = new ArrayList<>();
//
//
//        // Displaying the graph
//        graph.printGraph();
//
//        double x = graph.dfs(graph, 1, 3, visited, 1);
//        System.out.println(x);
//    }
//}
