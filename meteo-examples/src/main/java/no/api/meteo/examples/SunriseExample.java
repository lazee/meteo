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
import no.api.meteo.entity.core.service.sunrise.Sunrise;
import no.api.meteo.entity.core.service.sunrise.SunriseDate;
import no.api.meteo.service.sunrise.SunriseService;
import no.api.meteo.util.MeteoDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class SunriseExample {

    //public static final double LONGITUDE_OSLO = 10.7460923576733;
    public static final double LONGITUDE_SARPSBORG = 11.11;

    //public static final double LATITUDE_OSLO = 59.912726542422;
    public static final double LATITUDE_SARPSBORG = 59.28;


    private static final Logger log = LoggerFactory.getLogger(LocationExample.class);

    private final MeteoClient meteoClient;

    public SunriseExample() {
        meteoClient = new DefaultMeteoClient("MyExampleApp");
    }

    public MeteoData<Sunrise> runExample() throws MeteoException {
        SunriseService sunriseService = new SunriseService(meteoClient);
        return sunriseService.fetchContent(LONGITUDE_SARPSBORG, LATITUDE_SARPSBORG, LocalDate.now());

    }

    public void shutDown() {
        meteoClient.shutdown();
    }

    public static void main(String[] args) throws MeteoException {
        SunriseExample example = new SunriseExample();
        MeteoData<Sunrise> data = example.runExample();
        SunriseDate sunriseDate = data.getResult().getDates().get(0);
        log.info("On " + MeteoDateUtils.zonedDateTimeToYyyyMMdd(sunriseDate.getDate()) + " the sun will rise at " +
                         MeteoDateUtils.zonedDateTimeToHHMM(sunriseDate.getSun().getRise()));
        example.shutDown();

    }
}

