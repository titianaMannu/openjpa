package org.apache.openjpa.lib.graph;

import org.apache.openjpa.lib.graph.support.GraphType;
import org.apache.openjpa.lib.graph.support.Topology;
import org.apache.openjpa.lib.graph.support.TopologyFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;


import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class GraphTest {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {GraphType.GENERAL, 1, 9, true},
                {GraphType.ONLY_CYCLE_TOPOLOGY, 1, -1, true},
                {GraphType.GENERAL, 1, 2, false},
                {GraphType.STAR_TOPOLOGY, -1, 2, false},
                {GraphType.GENERAL, null, null, true},
                {GraphType.EMPTY_TOPOLOGY, 1, 2, true}
        });
    }

    public GraphTest(GraphType type, Object node1, Object node2, boolean directed) {
        this.node1 = node1;
        if (node1 == null || node2 == null) {
            this.edge = null;
        } else {
            this.edge = new Edge(node1, node2, directed);
        }
        this.directed = directed;
        TopologyFactory topologyFactory = new TopologyFactory();
        topology = topologyFactory.getTopology(type);
    }

    private final Topology topology;
    private Graph graph;
    private final Object node1;
    private final Edge edge;
    private final boolean directed;


    @Before
    public void setUp() throws Exception {
        assert topology != null;
        graph = topology.buildTopology(directed);
    }

    @Test
    public void addRemoveNodeTest() {
        Collection<Object> nodes = graph.getNodes();
        int initialSize = nodes.size();
        //fake initialization
        for (Object node : nodes) {
            graph.addNode(node);
            assertEquals(initialSize, graph.getNodes().size());
        }

        if (node1 == null) {
            assertThrows(NullPointerException.class, () -> graph.addNode(null));
            return;
        }

        if (!graph.containsNode(node1)) {
            graph.addNode(this.node1);
            assertEquals(initialSize + 1, graph.getNodes().size());
            graph.removeNode(this.node1);
            assertEquals(initialSize, graph.getNodes().size());
        }

        graph.clear();
        assertEquals(0, graph.getEdges().size());
        //remove a node deletes edges too
        assertEquals(0, graph.getNodes().size());

    }


    @Test
    public void addRemoveEdgesTest() {

        Collection<Edge> edges = graph.getEdges();
        int initialEdgeSize = edges.size();
        int initialNodeSize = graph.getNodes().size();

        if (edge != null && (!graph.containsNode(edge.getFrom()) || !graph.containsNode(edge.getTo()))) {
            assertThrows(IllegalArgumentException.class, () -> graph.addEdge(edge));
            assertFalse(graph.removeEdge(edge));
        }else if (edge != null && !graph.getEdges().contains(edge)) {
            graph.addEdge(edge);
            assertTrue(graph.getEdges().size() >= initialEdgeSize);
        }

        // clean edges to obtain a completely disconnected graph
        boolean res;
        edges = graph.getEdges();
        for (Edge e : edges) {
            res = graph.removeEdge(e);
            assertTrue(res);

        }
        //check node size
        assertEquals(initialNodeSize, graph.getNodes().size());
        assertEquals(0, graph.getEdges().size());

    }


    @Test
    public void fromToTest() {
        //integrity check
        Collection<Edge> from = graph.getEdgesFrom(node1);
        for (Edge e : from) {
            Object to = e.getTo();
            Collection<Edge> c = graph.getEdgesTo(to);
            assertTrue(c.contains(e));
        }

        Collection<Edge> to = graph.getEdgesTo(node1);
        for (Edge e : to) {
            Object fromNode = e.getFrom();
            Collection<Edge> c = graph.getEdgesFrom(fromNode);
            assertTrue(c.contains(e));
        }
    }

}
