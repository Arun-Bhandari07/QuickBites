package com.QuickBites.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.QuickBites.app.utilities.DijkstraRouter;
import com.graphhopper.util.shapes.GHPoint;

@RestController
@RequestMapping("/api/route")
public class RoutingController {
	
	@Value("${resturant.location.latitude}")
	private double fromLat;
	
	@Value("${resturant.location.longitude}")
	private double fromLon;

    private final DijkstraRouter dijkstraRouter;

    
    public RoutingController(DijkstraRouter dijkstraRouter) {
        this.dijkstraRouter = dijkstraRouter;
    }

    @GetMapping
    public ResponseEntity<List<GHPoint>> getShortestPath(
            @RequestParam double toLat,
            @RequestParam double toLon) {

        GHPoint from = new GHPoint(fromLat, fromLon);
        GHPoint to = new GHPoint(toLat, toLon);
        List<GHPoint> path = dijkstraRouter.getShortestPath(from, to);
        
        if (path.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(path);
    }
}
