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
import no.api.meteo.entity.core.service.locationforecast.PeriodForecast;
import no.api.meteo.entity.core.service.locationforecast.PointForecast;
import no.api.meteo.entity.extras.MeteoExtrasForecast;
import no.api.meteo.entity.extras.MeteoExtrasForecastDay;
import no.api.meteo.service.locationforecast.LocationforecastLTSService;
import no.api.meteo.services.LocationForecastHelper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class LongTermForecastExample {

    //longitude=5.32&latitude=60.39&moh=3.0

    //public static final double LONGITUDE_OSLO = 10.7460923576733;
    public static final double LONGITUDE_BERGEN = 5.32;

    //public static final double LATITUDE_OSLO = 59.912726542422;
    public static final double LATITUDE_BERGEN = 60.39;

    //public static final int ALTITUDE_OSLO = 14;
    public static final int ALTITUDE_BERGEN = 3;

    private MeteoClient meteoClient;

    public LongTermForecastExample() {
        meteoClient = new DefaultMeteoClient();
    }

    public void runExample() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
        LocationforecastLTSService ltsService = new LocationforecastLTSService(meteoClient);
        try {
            // Fetch the data from api.met.no
            MeteoData<LocationForecast> data =  ltsService.fetchContent(LONGITUDE_BERGEN, LATITUDE_BERGEN, ALTITUDE_BERGEN);
            //MeteoData<LocationForecast> data =  ltsService.fetchResource(LONGITUDE_OSLO, LATITUDE_OSLO, ALTITUDE_OSLO);

            LocationForecastHelper locationForecastHelper = new LocationForecastHelper(data.getResult());

            for (MeteoExtrasForecastDay day : locationForecastHelper.createLongTermForecast().getForecastDays()) {
                print("\nDATE : " + day.getDay());

                for (MeteoExtrasForecast forecast : day.getForecasts()) {
                    PeriodForecast p = forecast.getPeriodForecast();
                    PointForecast po = forecast.getPointForecast();
                    DateTime df = new DateTime(p.getFromTime());
                    DateTime dt = new DateTime(p.getToTime());
                    print(df.toString(fmt)+"-"+dt.toString(fmt)+" | "+p.getSymbol().getId()+" | "+Math.round(po.getTemperature().getValue()) + " | "+p.getPrecipitation().getMinValue() + "-" + p.getPrecipitation().getMaxValue() + "," + p.getPrecipitation().getValue());
                }
            }

        } catch (MeteoException e) {
            // Got client exception. No data available
            print("Caught exception : " + e.getMessage());
        }
    }

    public void print(String s) {
        System.out.println(s); // NOSONAR This is an example, so println is ok
    }

    public void shutDown() {
        meteoClient.shutdown();
    }

    public static void main(String[] args) {
        LongTermForecastExample longTermForecastExample = new LongTermForecastExample();
        longTermForecastExample.runExample();
    }

}
