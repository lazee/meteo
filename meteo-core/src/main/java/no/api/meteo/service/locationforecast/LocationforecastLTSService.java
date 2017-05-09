/*
 * Copyright (c) 2011-2015 Amedia Utvikling AS.
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

import static no.api.meteo.util.MeteoConstants.PARAM_ALTITUDE;
import static no.api.meteo.util.MeteoConstants.PARAM_LATITUDE;
import static no.api.meteo.util.MeteoConstants.PARAM_LONGITUDE;

/**
 * Service object for fetching data for the LocationforecastLTS service in the MET API.
 */
public final class LocationforecastLTSService extends AbstractMeteoService {

    private static final String MET_SERVICE_NAME = "locationforecastlts";

    private final MeteoDataParser<LocationForecast> parser;

    /**
     * Constructor requiring an MeteoClient instance.
     *
     * @param meteoClient
     *         An instance of MeteoClient that the service will use to fetch the data.
     */
    public LocationforecastLTSService(MeteoClient meteoClient) {
        super(meteoClient, MET_SERVICE_NAME, new MeteoServiceVersion(1, 3));
        parser = new LocationforcastLTSParser();
    }

    /**
     * Fetch a LocationforecastLTS from the MET API, based on longitude, latitude and altitude.
     *
     * @param longitude
     *         The longitude of the location where the forecast should be fetched for.
     * @param latitude
     *         The latitude of the location where the forecast should be fetched for.
     * @param altitude
     *         The altitude of the location where the forecast should be fetched for.
     *
     * @return A MeteoData object containing response information and data converted into a LocationForcast object.
     *
     * @throws MeteoException
     *         If a problem occurred while fetching or parsing the MET data.
     */
    public MeteoData<LocationForecast> fetchContent(double longitude, double latitude, int altitude)
            throws MeteoException {
        MeteoResponse response = getMeteoClient().fetchContent(
                createServiceUriBuilder()
                        .addParameter(PARAM_LATITUDE, latitude)
                        .addParameter(PARAM_LONGITUDE, longitude)
                        .addParameter(PARAM_ALTITUDE, altitude).build()
        );
        return new MeteoData<>(parser.parse(response.getData()), response);
    }

    /**
     * Fetch a LocationforecastLTS from the MET API, based on longitude, and latitude.
     *
     * @param longitude
     *         The longitude of the location where the forecast should be fetched for.
     * @param latitude
     *         The latitude of the location where the forecast should be fetched for.
     *
     * @return A MeteoData object containing response information and data converted into a LocationForcast object.
     *
     * @throws MeteoException
     *         If a problem occurred while fetching or parsing the MET data.
     */
    public MeteoData<LocationForecast> fetchContent(double longitude, double latitude) throws MeteoException {
        MeteoResponse response = getMeteoClient().fetchContent(
                createServiceUriBuilder()
                        .addParameter(PARAM_LATITUDE, latitude)
                        .addParameter(PARAM_LONGITUDE, longitude).build()
        );
        return new MeteoData<>(parser.parse(response.getData()), response);
    }

}
