package org.apache.openjpa.lib.graph.support.topologies;

import org.apache.openjpa.lib.graph.Edge;
import org.apache.openjpa.lib.graph.Graph;
import org.apache.openjpa.lib.graph.support.Topology;

import java.util.ArrayList;
import java.util.List;

public class StarTopology implements Topology {
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


        Edge e1 = new Edge(n1, n2, directed);
        graph.addEdge(e1);
        Edge e2 = new Edge(n1, n3, directed);
        graph.addEdge(e2);
        Edge e3 = new Edge(n1, n4, directed);
        graph.addEdge(e3);
        Edge e4 = new Edge(n1, n5, directed);
        graph.addEdge(e4);

        treeList.add(e1);
        treeList.add(e2);
        treeList.add(e3);
        treeList.add(e4);

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
