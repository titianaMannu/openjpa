package org.apache.openjpa.lib.graph.support.topologies;

import org.apache.openjpa.lib.graph.Edge;
import org.apache.openjpa.lib.graph.Graph;
import org.apache.openjpa.lib.graph.support.Topology;

import java.util.ArrayList;
import java.util.List;


public class GeneralTopology implements Topology {


    private static final List<Edge> treeList = new ArrayList<>();
    private static final List<Edge> forwardList = new ArrayList<>();
    private static final List<Edge> backList = new ArrayList<>();


    @Override
    public Graph buildTopology(boolean directed) {
        treeList.clear();
        forwardList.clear();
        backList.clear();

        Graph graph = new Graph();
        Integer n1 = 1;
        graph.addNode(n1);
        Integer n2 = 2;
        graph.addNode(n2);
        Integer n3 = 3;
        graph.addNode(n3);
        Integer n4 = 4;
        graph.addNode(n4);
        Integer n5 = 5;
        graph.addNode(n5);
        Integer n6 = 6;
        graph.addNode(n6);
        Integer n7 = 7;
        graph.addNode(n7);
        Integer n8 = 8;
        graph.addNode(n8);
        Integer n9 = 9;
        graph.addNode(n9);
        Integer n10 = 10;
        graph.addNode(n10);

        //improve statement and condition coverage
        Integer n11 = 11;
        graph.addNode(n11);

        //fork1
        Edge e1 = new Edge(n1, n2, directed);
        graph.addEdge(e1);
        Edge e2 = new Edge(n2, n4, directed);
        graph.addEdge(e2);
        Edge e3 = new Edge(n4, n6, directed);
        graph.addEdge(e3);
        Edge e10 = new Edge(n4, n9, directed);
        graph.addEdge(e10);
        //improve statement and condition coverage
        Edge e11 = new Edge(n4, n10, directed);
        graph.addEdge(e11);
        Edge e12 = new Edge(n8, n11, directed);
        graph.addEdge(e12);
        Edge e13 = new Edge(n8, n6, directed);
        graph.addEdge(e13);

        //fork2
        Edge e4 = new Edge(n1, n3, directed);
        graph.addEdge(e4);
        Edge e5 = new Edge(n3, n5, directed);
        graph.addEdge(e5);
        Edge e6 = new Edge(n5, n8, directed);
        graph.addEdge(e6);
        Edge e9 = new Edge(n5, n7, directed);
        graph.addEdge(e9);

        // forward edge
        Edge e7 = new Edge(n1, n8, directed);
        graph.addEdge(e7);
        // back edge -- cycle
        Edge e8 = new Edge(n6, n2, directed);
        graph.addEdge(e8);

        treeList.add(e1);
        treeList.add(e2);
        treeList.add(e3);
        treeList.add(e4);
        treeList.add(e5);
        treeList.add(e6);
        treeList.add(e9);
        treeList.add(e10);
        treeList.add(e11);
        treeList.add(e12);

        forwardList.add(e7);
        forwardList.add(e13);

        backList.add(e8);

        return graph;
    }


    @Override
    public List<Edge> getExpectedEdges(int type) {
        switch (type) {
            case Edge.TYPE_BACK:
                return backList;
            case Edge.TYPE_FORWARD:
                return forwardList;
            case Edge.TYPE_TREE:
                return treeList;
            default:
                return new ArrayList<>();
        }
    }
}
