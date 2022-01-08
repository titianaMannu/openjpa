package org.apache.openjpa.lib.graph.support.topologies;

import org.apache.openjpa.lib.graph.Edge;
import org.apache.openjpa.lib.graph.Graph;
import org.apache.openjpa.lib.graph.support.Topology;

import java.util.ArrayList;
import java.util.List;

public class OnlyCycleTopology implements Topology {
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

        // self edge for 1
        Edge e1 = new Edge(n1, n1, directed);
        graph.addEdge(e1);

        Edge e2 = new Edge(n1, n2, directed);
        graph.addEdge(e2);

        Edge e3 = new Edge(n2, n3, directed);
        graph.addEdge(e3);

        Edge e4 = new Edge(n3, n1, directed);
        graph.addEdge(e4);

        //improve coverage
        Edge e7 = new Edge(n1, n3, directed);
        graph.addEdge(e7);

        // self edge for 3
        Edge e5 = new Edge(n3, n3, directed);
        graph.addEdge(e5);

        // self edge for 3
        Edge e6 = new Edge(n2, n2, directed);
        graph.addEdge(e6);



        treeList.add(e2);
        treeList.add(e3);

        backList.add(e1);
        backList.add(e5);
        backList.add(e6);
        backList.add(e4);

        forwardList.add(e7);

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
