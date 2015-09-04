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

import lombok.extern.slf4j.Slf4j;
import no.api.meteo.MeteoException;
import no.api.meteo.client.DefaultMeteoClient;
import no.api.meteo.client.MeteoClient;
import no.api.meteo.client.MeteoData;
import no.api.meteo.entity.core.service.textlocation.TextLocationTime;
import no.api.meteo.entity.core.service.textlocation.TextLocationWeather;
import no.api.meteo.service.textlocation.TextLocationService;

@Slf4j
public class TextlocationExample {

    private static final double LONGITUDE_OSLO = 10.7460923576733;

    private static final double LATITUDE_OSLO = 59.912726542422;

    private final MeteoClient meteoClient;

    public TextlocationExample() {
        meteoClient = new DefaultMeteoClient();
    }

    public MeteoData<TextLocationWeather> runExample() throws MeteoException {
        TextLocationService service = new TextLocationService(meteoClient);
        return service.fetchContent(LONGITUDE_OSLO, LATITUDE_OSLO);
    }

    public void shutDown() {
        meteoClient.shutdown();
    }

    public static void main(String[] args) throws MeteoException {
        TextlocationExample example = new TextlocationExample();
        MeteoData<TextLocationWeather> data = example.runExample();
        log.info("Textforecast for % - %", LONGITUDE_OSLO, LATITUDE_OSLO);
        log.info("Product description: " + data.getResult().getProductionDescription());

        for (TextLocationTime time : data.getResult().getTextLocationTimes()) {
            log.info("From: " + time.getFrom().toString());
            log.info("Forecast: " + time.getTextLocation().getForecast());
        }
        example.shutDown();
    }
}
