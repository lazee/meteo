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

package no.api.meteo.examples;

import no.api.meteo.MeteoException;
import no.api.meteo.client.DefaultMeteoClient;
import no.api.meteo.client.MeteoClient;
import no.api.meteo.client.MeteoData;
import no.api.meteo.entity.core.service.locationforecast.LocationForecast;
import no.api.meteo.entity.core.Model;
import no.api.meteo.service.locationforecast.LocationforecastLTSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocationExample {

    private static final double LONGITUDE_OSLO = 10.7460923576733;

    private static final double LATITUDE_OSLO = 59.912726542422;

    private static final int ALTITUDE_OSLO = 14;

    private static final Logger log = LoggerFactory.getLogger(LocationExample.class);

    private final MeteoClient meteoClient;

    private LocationExample() {
        meteoClient = new DefaultMeteoClient(5);
    }

    private MeteoData<LocationForecast> runExample() throws MeteoException {
        LocationforecastLTSService ltsService = new LocationforecastLTSService(meteoClient);
        return ltsService.fetchContent(LONGITUDE_OSLO, LATITUDE_OSLO, ALTITUDE_OSLO);
    }

    private void shutDown() {
        meteoClient.shutdown();
    }

    public static void main(String[] args) throws MeteoException {
        LocationExample locationExample = new LocationExample();
        MeteoData<LocationForecast> data = locationExample.runExample();
        for (Model m : data.getResult().getMeta().getModels()) {
            log.info("Model Name: " + m.getName());
        }
        locationExample.shutDown();
    }
}
