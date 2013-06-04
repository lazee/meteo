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

package no.api.meteo.examples;

import no.api.meteo.MeteoException;
import no.api.meteo.client.DefaultMeteoClient;
import no.api.meteo.client.MeteoClient;
import no.api.meteo.client.MeteoData;
import no.api.meteo.entity.core.service.locationforecast.LocationForecast;
import no.api.meteo.entity.extras.MeteoExtrasForecast;
import no.api.meteo.service.locationforecast.LocationforecastLTSService;
import no.api.meteo.services.LocationForecastHelper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class LongTermForecastExample {

    public static final double LONGITUDE_OSLO = 10.7460923576733;

    public static final double LATITUDE_OSLO = 59.912726542422;

    public static final int ALTITUDE_OSLO = 14;

    private MeteoClient meteoClient;

    public LongTermForecastExample() {
        meteoClient = new DefaultMeteoClient();
    }

    public void runExample() {
        LocationforecastLTSService ltsService = new LocationforecastLTSService(meteoClient);
        try {
            // Fetch the data from api.met.no
            MeteoData<LocationForecast> data =  ltsService.fetchContent(LONGITUDE_OSLO, LATITUDE_OSLO, ALTITUDE_OSLO);

            LocationForecastHelper locationForecastHelper = new LocationForecastHelper(data.getResult());

            DateTime dt = DateTime.now().plusDays(2);

            printForecast(0, 6, locationForecastHelper, dt);
            printForecast(6, 12, locationForecastHelper, dt);
            printForecast(12, 18, locationForecastHelper, dt);
            printForecast(18, 00, locationForecastHelper, dt);

        } catch (MeteoException e) {
            // Got client exception. No data available
            System.out.println("Caught exception : " + e.getMessage());
        }
    }

    public void shutDown() {
        meteoClient.shutdown();
    }

    public static void main(String[] args) {
        LongTermForecastExample longTermForecastExample = new LongTermForecastExample();
        longTermForecastExample.runExample();
    }

    private void printForecast(int from, int to, LocationForecastHelper locationForecastHelper, DateTime dt) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        MeteoExtrasForecast extra  = locationForecastHelper.getBestForecastForPeriod(dt.withHourOfDay(from), (to == 0 ? dt.withHourOfDay(to).plusDays(
                1) : dt.withHourOfDay(to)));
        if (extra == null) {
            System.out.println("Something went wrong");
        } else {
            DateTime f = new DateTime(extra.getPeriodForecast().getFromTime());
            DateTime t = new DateTime(extra.getPeriodForecast().getToTime());
            System.out.println("RETURN OBJECT : " + f.toString(fmt) + " : " + t.toString(fmt) + " : " + extra.getPeriodForecast().getSymbol().getId());
        }
    }
}
