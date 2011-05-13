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

package no.api.meteo.examples;

import no.api.meteo.MeteoException;
import no.api.meteo.client.DefaultMeteoClient;
import no.api.meteo.client.MeteoClient;
import no.api.meteo.client.MeteoData;
import no.api.meteo.entity.core.service.sunrise.Sunrise;
import no.api.meteo.entity.core.service.sunrise.SunriseDate;
import no.api.meteo.service.sunrise.SunriseService;
import no.api.meteo.util.MeteoDateUtils;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class SunriseExample {

    public static final double LONGITUDE_OSLO = 10.7460923576733;

    public static final double LATITUDE_OSLO = 59.912726542422;

    private static final Logger log = LoggerFactory.getLogger(LocationExample.class);

    private MeteoClient meteoClient;

    public SunriseExample() {
        BasicConfigurator.configure();
        meteoClient = new DefaultMeteoClient();
    }

    public MeteoData<Sunrise> runExample() {
        SunriseService sunriseService = new SunriseService(meteoClient);
        try {
            return sunriseService.fetchContent(LONGITUDE_OSLO, LATITUDE_OSLO, new Date());
        } catch (MeteoException e) {
            // Got client exception. No data available
            log.error("Caught exception : " + e.getMessage());
            return null;
        }
    }

    public void shutDown() {
        meteoClient.shutdown();
    }

    public static void main(String[] args) {
        SunriseExample sunriseExample = new SunriseExample();
        MeteoData<Sunrise> data = sunriseExample.runExample();
        // Just to prove that we have data
        if (data == null || data.getResult() == null || data.getResult().getDates() == null ||
                data.getResult().getDates().size() < 1) {
            log.error("Something went wrong!");
        } else {
            SunriseDate sunriseDate = data.getResult().getDates().get(0);
            log.info("On " + MeteoDateUtils.dateToYyyyMMdd(sunriseDate.getDate()) + " the sun will rise at " +
                    MeteoDateUtils.dateToHHmm(sunriseDate.getSun().getRise()) + " in Oslo");
        }
    }
}

