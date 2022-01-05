package org.apache.openjpa.lib.graph.support.topologies;

import org.apache.openjpa.lib.graph.Edge;
import org.apache.openjpa.lib.graph.Graph;
import org.apache.openjpa.lib.graph.support.Topology;

import java.util.ArrayList;
import java.util.List;

public class EmptyTopology implements Topology {

    @Override
    public Graph buildTopology(boolean directed) {
        return new Graph();
    }

    @Override
    public List<Edge> getExpectedEdges(int type) {
        return new ArrayList<>();
    }
}
