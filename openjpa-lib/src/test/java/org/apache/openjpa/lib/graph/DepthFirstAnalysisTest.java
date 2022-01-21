package org.apache.openjpa.lib.graph;


import org.apache.openjpa.lib.graph.support.GraphType;
import org.apache.openjpa.lib.graph.support.Topology;
import org.apache.openjpa.lib.graph.support.TopologyFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class DepthFirstAnalysisTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                //multidimensional approach
                {GraphType.GENERAL, true, Edge.TYPE_BACK},
                {GraphType.GENERAL, true, Edge.TYPE_FORWARD},
                {GraphType.GENERAL, true, Edge.TYPE_TREE},

                {GraphType.STAR_TOPOLOGY, true, Edge.TYPE_TREE},
                {GraphType.STAR_TOPOLOGY, true, Edge.TYPE_BACK},
                {GraphType.STAR_TOPOLOGY, true, Edge.TYPE_FORWARD},

                {GraphType.ONLY_CYCLE_TOPOLOGY, true, Edge.TYPE_BACK},
                {GraphType.ONLY_CYCLE_TOPOLOGY, true, Edge.TYPE_FORWARD},
                {GraphType.ONLY_CYCLE_TOPOLOGY, true, Edge.TYPE_TREE},


                {GraphType.GENERAL, false, Edge.TYPE_BACK},
                {GraphType.GENERAL, false, Edge.TYPE_FORWARD},
                {GraphType.GENERAL, false, Edge.TYPE_TREE},

                {GraphType.STAR_TOPOLOGY, false, Edge.TYPE_TREE},
                {GraphType.STAR_TOPOLOGY, false, Edge.TYPE_BACK},
                {GraphType.STAR_TOPOLOGY, false, Edge.TYPE_FORWARD},

                {GraphType.ONLY_CYCLE_TOPOLOGY, false, Edge.TYPE_BACK},
                {GraphType.ONLY_CYCLE_TOPOLOGY, false, Edge.TYPE_FORWARD},
                {GraphType.ONLY_CYCLE_TOPOLOGY, false, Edge.TYPE_TREE},
        });
    }

    private DepthFirstAnalysis dfs;
    private  Graph graph;
    private final int edgeType;
    private final Topology topology;
    private final boolean useCustomComparator;

    public DepthFirstAnalysisTest(GraphType type, boolean useCustomComparator, int edgeType) {
        this.useCustomComparator = useCustomComparator;
        this.edgeType = edgeType;
        TopologyFactory topologyFactory = new TopologyFactory();
        topology = topologyFactory.getTopology(type);
    }


    @Before
    public void setUp() {
        assert topology != null;
        graph = topology.buildTopology(true);
        dfs = new DepthFirstAnalysis(graph);

        if (useCustomComparator) {
            Comparator<Object> comparator = mock(Comparator.class);
            when(comparator.compare(any(), any())).thenAnswer(invocationOnMock -> {
                if (graph.getEdgesFrom(invocationOnMock.getArguments()[0]).size()
                        >
                        graph.getEdgesFrom(invocationOnMock.getArguments()[1]).size()) {
                    return 1;
                } else if (graph.getEdgesFrom(invocationOnMock.getArguments()[0]).size()
                        ==
                        graph.getEdgesFrom(invocationOnMock.getArguments()[1]).size()) {
                    return 0;
                }

                return -1;
            });
            dfs.setNodeComparator(comparator);
        }
    }

    @Test
    public void getEdgesTest() {
        Collection<Edge> edges = dfs.getEdges(edgeType);
        List<Edge> expected = topology.getExpectedEdges(edgeType);
        assert edges != null;
        assert expected != null;
        //verify that all expected edges are returned
        for (Edge e : edges) {
            assertTrue(expected.contains(e));
        }

        // verify that the returned edges are as many as expected
        assertEquals(edges.size(), expected.size());
    }

    @Test
    public void getSortedNodesTest() {
        assumeFalse(useCustomComparator);

        List<Object> nodes = new ArrayList<>(dfs.getSortedNodes());
        assertEquals(graph.getNodes().size(), nodes.size());

        int logicalTime = 0;
        int currTime;
        for (int i = 0; i < nodes.size(); i++) {

            currTime = dfs.getFinishedTime(nodes.get(i));
            // sort based on logical time
            assertTrue(logicalTime <= currTime);
            if (logicalTime < currTime) {
                logicalTime = currTime;
            }

        }
    }

    @Test
    public void getSortedNodesCustomComparatorTest() {
        assumeTrue(useCustomComparator);

        List<Object> nodes = new ArrayList<>(dfs.getSortedNodes());
        assertEquals(graph.getNodes().size(), nodes.size());

        int currTime = 0;
        Object prev = null;
        Object curr;
        for (Object node : nodes) {
            curr = node;
            if (prev == null) {
                prev = node;
                continue;
            }

            int outDegreeCurr = graph.getEdgesFrom(curr).size();
            int outDegreePrev = graph.getEdgesFrom(prev).size();
            assertTrue(currTime < dfs.getFinishedTime(node) || outDegreeCurr >= outDegreePrev );
            currTime = dfs.getFinishedTime(node);
            prev = curr;

        }
    }

    @Test
    public void cycleTest() {
        if (dfs.hasNoCycles()) {
            assertEquals(0, dfs.getEdges(Edge.TYPE_BACK).size());
            return;
        }
        Collection<Edge> forwardEdges = dfs.getEdges(Edge.TYPE_FORWARD);
        Collection<Edge> treeEdges = dfs.getEdges(Edge.TYPE_TREE);
        Collection<Edge> backEdges = dfs.getEdges(Edge.TYPE_BACK);
        List<Edge> cycle;
        for (Edge e : backEdges) {
            cycle = e.getCycle();
            for (Edge ec : cycle) {
                if (ec != e) {
                    //others are tree edges or forward
                    assertTrue(treeEdges.contains(ec) || forwardEdges.contains(ec));
                }
            }
        }

    }


}


