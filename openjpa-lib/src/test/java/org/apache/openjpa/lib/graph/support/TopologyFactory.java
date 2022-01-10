package org.apache.openjpa.lib.graph.support;

import org.apache.openjpa.lib.graph.support.topologies.GeneralTopology;
import org.apache.openjpa.lib.graph.support.topologies.OnlyCycleTopology;
import org.apache.openjpa.lib.graph.support.topologies.StarTopology;

public class TopologyFactory {

    public Topology getTopology(GraphType graphType) {
        if (graphType  == null ){
            return null;
        }

        switch (graphType) {
            case GENERAL:
                return new GeneralTopology();
            case STAR_TOPOLOGY:
                return new StarTopology();
            case ONLY_CYCLE_TOPOLOGY:
                return new OnlyCycleTopology();
            default:
                return null;
        }

    }
}
