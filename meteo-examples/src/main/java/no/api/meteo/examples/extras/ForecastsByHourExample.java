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

package no.api.meteo.examples.extras;

import no.api.meteo.MeteoException;
import no.api.meteo.client.DefaultMeteoClient;
import no.api.meteo.client.MeteoClient;
import no.api.meteo.entity.Coordinates;
import no.api.meteo.examples.AbstractExample;
import no.api.meteo.services.MeteoServicesManagerImpl;
import no.api.meteo.services.MeteoServicesManager;
import no.api.meteo.services.entity.MeteoForecastPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 */
public class ForecastsByHourExample extends AbstractExample {

    public static final double LONGITUDE_OSLO = 10.7460923576733;

    public static final double LATITUDE_OSLO = 59.912726542422;

    public static final int ALTITUDE_OSLO = 14;

    private static final Logger log = LoggerFactory.getLogger(ForecastsByHourExample.class);

    private MeteoClient meteoClient;

    private MeteoServicesManager servicesManager;

    public ForecastsByHourExample() {
        configureLog("INFO");
        meteoClient = new DefaultMeteoClient();
        servicesManager = new MeteoServicesManagerImpl(meteoClient);
    }

    public void runExample() {
        try {
            List<MeteoForecastPair> list =  servicesManager.fetchPointForecastsByHour(10,
                    new Coordinates(LONGITUDE_OSLO, LATITUDE_OSLO, ALTITUDE_OSLO));

            log.info("Got " + list.size() + " forecasts.");
            for (MeteoForecastPair pair : list) {
                prettyLogPeriodForecast(pair.getPeriodForecast());
            }
        } catch (MeteoException e) {
            log.error("Something went wrong", e);
        }

    }

    public void shutDown() {
        meteoClient.shutdown();
    }

    public static void main(String[] args) {
        ForecastsByHourExample forecastsByHourExample = new ForecastsByHourExample();
        forecastsByHourExample.runExample();
    }
}
