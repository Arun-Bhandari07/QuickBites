package com.QuickBites.app.utilities;

import com.graphhopper.GraphHopper;
import com.graphhopper.config.CHProfile;
import com.graphhopper.config.Profile;

public class GraphBuilder {

    private final String osmFilePath = "src/main/resources/maps/nepal-latest.osm.pbf";
    private final String graphFolder = "graph-cache";

    public GraphHopper buildGraph() {
        GraphHopper hopper = new GraphHopper();

        hopper.setOSMFile(osmFilePath);
        hopper.setGraphHopperLocation(graphFolder);

        // Define the routing profile
        Profile carProfile = new Profile("car")
                .setVehicle("car")
                .setWeighting("fastest")
                .setTurnCosts(false);

        hopper.setProfiles(carProfile);
        hopper.getCHPreparationHandler().setCHProfiles(new CHProfile("car"));

        hopper.importOrLoad();

        return hopper;
    }
}
