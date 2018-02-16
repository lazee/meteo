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
import no.api.meteo.entity.core.service.textforecast.available.Available;
import no.api.meteo.entity.core.service.textforecast.available.Query;
import no.api.meteo.service.textforecast.available.AvailableTextforecastsService;

@Slf4j
public class AvailableTextforecastsExample {

    private final MeteoClient meteoClient;

    private AvailableTextforecastsExample() {
        meteoClient = new DefaultMeteoClient("MyExampleApp");
    }

    private MeteoData<Available> fetchDate() throws MeteoException {
        AvailableTextforecastsService service = new AvailableTextforecastsService(meteoClient);
        return service.fetchContent();

    }

    private void shutDown() {
        meteoClient.shutdown();
    }

    public static void main(String[] args) throws MeteoException {
        AvailableTextforecastsExample example = new AvailableTextforecastsExample();
        MeteoData<Available> data = example.fetchDate();
        for (Query query : data.getResult().getQueries()) {
            log.info(query.toString());
        }
        example.shutDown();
    }
}
