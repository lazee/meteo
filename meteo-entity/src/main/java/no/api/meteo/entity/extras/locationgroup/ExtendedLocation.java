package no.api.meteo.entity.extras.locationgroup;

import no.api.meteo.entity.core.Location;

public class ExtendedLocation extends Location {

    private String name;

    public ExtendedLocation(String name, Double longitude, Double latitude, Double altitude) {
        super(longitude, latitude, altitude);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
