package no.api.meteo.entity.extras.locationgroup;

import java.util.ArrayList;
import java.util.List;

public class LocationGroup {

    private String id;

    private List<ExtendedLocation> locations = new ArrayList<ExtendedLocation>();

    public LocationGroup(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public List<ExtendedLocation> getLocations() {
        return locations;
    }
}
