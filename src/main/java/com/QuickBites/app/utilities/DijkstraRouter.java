package com.QuickBites.app.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.AccessFilter;
import com.graphhopper.routing.util.EdgeFilter;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.storage.BaseGraph;
import com.graphhopper.util.EdgeExplorer;
import com.graphhopper.util.EdgeIterator;
import com.graphhopper.util.shapes.GHPoint;

@Component
public class DijkstraRouter {

    private  GraphHopper hopper;
    private  BaseGraph graph;
    private  EncodingManager encodingManager;
    private  EdgeFilter edgeFilter;

    public DijkstraRouter(GraphHopper hopper) {
        this.hopper = hopper;
        this.graph = hopper.getBaseGraph();
        this.encodingManager = hopper.getEncodingManager();
        this.edgeFilter = AccessFilter.ALL_EDGES;
    }

    public List<GHPoint> getShortestPath(GHPoint from, GHPoint to) {
        int sourceNode = hopper.getLocationIndex()
                .findClosest(from.lat, from.lon, edgeFilter)
                .getClosestNode();

        int targetNode = hopper.getLocationIndex()
                .findClosest(to.lat, to.lon, edgeFilter)
                .getClosestNode();

        Map<Integer, Double> distance = new HashMap<>();
        Map<Integer, Integer> prev = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        PriorityQueue<NodeEntry> queue = new PriorityQueue<>(Comparator.comparingDouble(NodeEntry::getDistance));

        distance.put(sourceNode, 0.0);
        queue.add(new NodeEntry(sourceNode, 0.0));

        EdgeExplorer explorer = graph.createEdgeExplorer(edgeFilter);

        while (!queue.isEmpty()) {
            NodeEntry current = queue.poll();
            int baseNode = current.nodeId;
            
            if (baseNode == targetNode) break; // Early exit if we've reached the target
            if (visited.contains(baseNode)) continue;
            
            visited.add(baseNode);

            EdgeIterator edgeIter = explorer.setBaseNode(baseNode);

            while (edgeIter.next()) {
                int adjNode = edgeIter.getAdjNode();
                double weight = edgeIter.getDistance();

                double newDist = distance.get(baseNode) + weight;
                if (newDist < distance.getOrDefault(adjNode, Double.POSITIVE_INFINITY)) {
                    distance.put(adjNode, newDist);
                    prev.put(adjNode, baseNode);
                    queue.add(new NodeEntry(adjNode, newDist));
                }
            }
        }

        return reconstructPath(prev, sourceNode, targetNode);
    }

    private List<GHPoint> reconstructPath(Map<Integer, Integer> prev, int sourceNode, int targetNode) {
        List<GHPoint> path = new ArrayList<>();
        
        // Check if path exists
        if (!prev.containsKey(targetNode)) {
            return path; // return empty path if no route found
        }

        int current = targetNode;
        while (current != sourceNode) {
            double lat = graph.getNodeAccess().getLat(current);
            double lon = graph.getNodeAccess().getLon(current);
            path.add(new GHPoint(lat, lon));
            current = prev.get(current);
        }
        
        // Add source node
        double lat = graph.getNodeAccess().getLat(sourceNode);
        double lon = graph.getNodeAccess().getLon(sourceNode);
        path.add(new GHPoint(lat, lon));
        
        Collections.reverse(path);
        return path;
    }

    static class NodeEntry {
        final int nodeId;
        final double distance;

        NodeEntry(int nodeId, double distance) {
            this.nodeId = nodeId;
            this.distance = distance;
        }

        double getDistance() {
            return distance;
        }
    }
}