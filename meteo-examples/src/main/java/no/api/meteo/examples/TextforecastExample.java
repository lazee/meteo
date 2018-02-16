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
import no.api.meteo.entity.core.service.textforecast.Time;
import no.api.meteo.entity.core.service.textforecast.Weather;
import no.api.meteo.entity.core.service.textforecast.query.ForecastQuery;
import no.api.meteo.service.textforecast.TextforecastService;

@Slf4j
public class TextforecastExample {

    private final MeteoClient meteoClient;

    public TextforecastExample() {
        meteoClient = new DefaultMeteoClient("MyExampleApp");
    }

    public MeteoData<Weather> runExample() throws MeteoException {
        TextforecastService service = new TextforecastService(meteoClient);
        return service.fetchContent(ForecastQuery.LAND_NB);
    }

    public void shutDown() {
        meteoClient.shutdown();
    }

    public static void main(String[] args) throws MeteoException {
        TextforecastExample example = new TextforecastExample();
        MeteoData<Weather> data = example.runExample();
        log.info("Textforecast for %", data.getResult().getTitle());
        for (Time time : data.getResult().getTimes()) {
            log.info(time.toString());
        }
        example.shutDown();
    }
}
