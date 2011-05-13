package no.api.meteo.locationgroups.config;

import no.api.meteo.MeteoException;
import no.api.meteo.entity.extras.locationgroup.LocationGroup;

import java.io.InputStream;
import java.util.Map;

public final class ConfigLoader {

    private ConfigLoader() {
        // Intentional
    }

    public static Map<String, LocationGroup> load(InputStream inputStream) throws MeteoException {
        ConfigParser parser = new ConfigParser();
        return parser.parse(inputStream);
    }

    public static Map<String, LocationGroup> load(String data) throws MeteoException {
        ConfigParser parser = new ConfigParser();
        return parser.parse(data);
    }

}
