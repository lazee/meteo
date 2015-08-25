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

package no.api.meteo.service.textlocation;

import no.api.meteo.MeteoException;
import no.api.meteo.client.MeteoClient;
import no.api.meteo.client.MeteoData;
import no.api.meteo.client.MeteoResponse;
import no.api.meteo.entity.core.MeteoServiceVersion;
import no.api.meteo.entity.core.service.textlocation.TextLocationWeather;
import no.api.meteo.service.AbstractMeteoService;
import no.api.meteo.service.MeteoDataParser;

/**
 * This service returns textforecasts and warnings for a geographical point or area. This includes text from other
 * sources than met.no
 */
public class TextLocationService extends AbstractMeteoService {

    private final MeteoDataParser<TextLocationWeather> parser;

    public TextLocationService(MeteoClient meteoClient) {
        super(meteoClient, "textlocation", new MeteoServiceVersion(1, 0));
        parser = new TextLocationParser();
    }

    /**
     * Fetch textforecasts and warnings for a geographical point or area, in Norwegian.
     *
     * @param longitude
     *         The longitude for the geographical point.
     * @param latitude
     *         The latitude for the geographical point.
     *
     * @return A MeteoData object containing response information and textforecast converted into an TextLocationWeather
     * object.
     *
     * @throws MeteoException
     *         If a problem occurred while fetching or parsing the MET data.
     */
    public MeteoData<TextLocationWeather> fetchContent(double longitude, double latitude)
            throws MeteoException {
        return fetchContent(longitude, latitude, TextLocationLanguage.NB);

    }

    /**
     * Fetch textforecasts and warnings for a geographical point or area.
     *
     * @param longitude
     *         The longitude for the geographical point.
     * @param latitude
     *         The latitude for the geographical point.
     * @param language
     *         The chosen language for the forecast. Note that textforecasts in english are available for only some
     *         areas and some types ( e.g textforecasts for the coastal areas ).
     *
     * @return A MeteoData object containing response information and textforecast converted into an TextLocationWeather
     * object.
     *
     * @throws MeteoException
     *         If a problem occurred while fetching or parsing the MET data.
     */
    public MeteoData<TextLocationWeather> fetchContent(double longitude, double latitude, TextLocationLanguage language)
            throws MeteoException {
        MeteoResponse response = getMeteoClient().fetchContent(
                createServiceUriBuilder()
                        .addParameter("latitude", latitude)
                        .addParameter("longitude", longitude)
                        .addParameter("language", language.getValue())
                        .build());
        return new MeteoData<>(parser.parse(response.getData()), response);
    }

}
