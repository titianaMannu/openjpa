package org.apache.openjpa.lib.graph.support;

import org.apache.openjpa.lib.graph.Edge;
import org.apache.openjpa.lib.graph.Graph;

import java.util.List;

public interface Topology {
    Graph buildTopology(boolean directed);

    List<Edge> getExpectedEdges(int type);

}
