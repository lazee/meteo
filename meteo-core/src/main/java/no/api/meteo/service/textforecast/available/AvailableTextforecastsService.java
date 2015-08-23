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

package no.api.meteo.service.textforecast.available;

import no.api.meteo.MeteoException;
import no.api.meteo.client.MeteoClient;
import no.api.meteo.client.MeteoData;
import no.api.meteo.client.MeteoResponse;
import no.api.meteo.entity.core.MeteoServiceVersion;
import no.api.meteo.entity.core.service.textforecast.available.Available;
import no.api.meteo.service.AbstractMeteoService;
import no.api.meteo.service.MeteoDataParser;

/**
 * Service object for fetching data for the Textforcast/Available service at http://api.met.no.
 *
 * {@link <a href="http://api.met.no/weatherapi/textforecast/1.6/documentation">http://api.met
 * .no/weatherapi/textforecast/1.6/documentation</a>}
 */
public class AvailableTextforecastsService extends AbstractMeteoService {

    private final MeteoDataParser<Available> parser;

    public AvailableTextforecastsService(MeteoClient meteoClient) {
        super(meteoClient, "textforecast", new MeteoServiceVersion(1, 6));
        parser = new AvailableTextforecastsParser();
    }

    /**
     * Fetch a list of all available textforecasts.
     *
     * @return A MeteoData object containing response information and available textforecasts converted into an
     * Available object.
     *
     * @throws MeteoException
     *         If a problem occurred while fetching or parsing the MET data.
     */
    public MeteoData<Available> fetchContent() throws MeteoException {
        MeteoResponse response = getMeteoClient().fetchContent(
                createServiceUriBuilder().addParameter("available", null).build());
        return new MeteoData<>(parser.parse(response.getData()), response);
    }

}
