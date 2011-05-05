/*
 * Copyright (c) 2011 A-pressen Digitale Medier
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

package no.api.meteo.service.locationforecastlts;

import no.api.meteo.entity.MeteoData;
import no.api.meteo.parser.MeteoDataParser;
import no.api.meteo.MeteoException;
import no.api.meteo.client.MeteoClient;
import no.api.meteo.client.MeteoResponse;
import no.api.meteo.entity.Coordinates;
import no.api.meteo.service.locationforecastlts.entity.LocationForecast;
import no.api.meteo.service.AbstractMeteoService;
import no.api.meteo.service.MeteoServiceVersion;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static no.api.meteo.util.MeteoConstants.PARAM_ALTITUDE;
import static no.api.meteo.util.MeteoConstants.PARAM_LATITUDE;
import static no.api.meteo.util.MeteoConstants.PARAM_LONGITUDE;

/**
 * http://api.met.no/weatherapi/locationforecastlts/1.0/documentation
 */
public class LocationforecastLTSService extends AbstractMeteoService {

    private static final String MET_SERVICE_NAME = "locationforecastlts";

    private static final MeteoServiceVersion version = new MeteoServiceVersion(1, 0);

    private MeteoDataParser<LocationForecast> parser;

    public LocationforecastLTSService(MeteoClient meteoClient) {
        super(meteoClient, MET_SERVICE_NAME, version);
        parser = new LocationforcastLTSParser();
    }

    public MeteoData<LocationForecast> fetchContent(Coordinates coordinates) {
        MeteoResponse response = getMeteoClient().fetchContent(createServiceUrl(coordinates));
        return new MeteoData<LocationForecast>(parser.parse(response.getData()), response);
    }

    public URL createServiceUrl(Coordinates coordinates) {
        if (coordinates == null) {
            throw new MeteoException("The provided coordinates is null");
        }
        Map<String, Object> queryParameters = new HashMap<String, Object>();
        queryParameters.put(PARAM_LONGITUDE, coordinates.getLongitude());
        queryParameters.put(PARAM_LATITUDE, coordinates.getLatitude());
        queryParameters.put(PARAM_ALTITUDE, coordinates.getAltitude());
        return createRequestUrl(queryParameters);
    }

}
