/*
 * Copyright (c) 2011-2013 Amedia AS.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package no.api.meteo.service.locationforecast;

import no.api.meteo.MeteoException;
import no.api.meteo.client.MeteoClient;
import no.api.meteo.client.MeteoData;
import no.api.meteo.client.MeteoResponse;
import no.api.meteo.entity.core.MeteoServiceVersion;
import no.api.meteo.entity.core.service.locationforecast.LocationForecast;
import no.api.meteo.service.AbstractMeteoService;
import no.api.meteo.service.MeteoDataParser;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static no.api.meteo.util.MeteoConstants.*;

/**
 * http://api.met.no/weatherapi/locationforecastlts/1.0/documentation
 * http://api.met.no/weatherapi/locationforecast/1.9/documentation
 */
public class LocationforecastLTSService extends AbstractMeteoService {

    // Due to a "bug" in the MET API we need to use the locationforecast service until the LTS feed is fixed.
    //private static final String MET_SERVICE_NAME = "locationforecastlts";
    private static final String MET_SERVICE_NAME = "locationforecast";

    // Changed for the same reason as MET_SERVICE_NAME
    //private static final MeteoServiceVersion version = new MeteoServiceVersion(1, 0);
    private static final MeteoServiceVersion VERSION = new MeteoServiceVersion(1, 9);

    private MeteoDataParser<LocationForecast> parser;

    public LocationforecastLTSService(MeteoClient meteoClient) {
        super(meteoClient, MET_SERVICE_NAME, VERSION);
        parser = new LocationforcastLTSParser();
    }

    public MeteoData<LocationForecast> fetchContent(double longitude, double latitude, double altitude) throws MeteoException {
        MeteoResponse response = getMeteoClient().fetchContent(createServiceUrl(longitude, latitude, altitude, false));
        return new MeteoData<>(parser.parse(response.getData()), response);
    }

    public MeteoData<LocationForecast> fetchContent(double longitude, double latitude) throws MeteoException {
        MeteoResponse response = getMeteoClient().fetchContent(createServiceUrl(longitude, latitude, 0, true));
        return new MeteoData<>(parser.parse(response.getData()), response);
    }

    private URL createServiceUrl(double longitude, double latitude, double altitude, boolean skipAltitude) throws MeteoException {
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(PARAM_LONGITUDE, longitude);
        queryParameters.put(PARAM_LATITUDE, latitude);
        if (!skipAltitude) {
            queryParameters.put(PARAM_ALTITUDE, (int) altitude);
        }
        return createRequestUrl(queryParameters);
    }

}
