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

package no.api.meteo.service.sunrise;

import no.api.meteo.MeteoException;
import no.api.meteo.client.MeteoClient;
import no.api.meteo.client.MeteoData;
import no.api.meteo.client.MeteoResponse;
import no.api.meteo.entity.core.MeteoServiceVersion;
import no.api.meteo.entity.core.service.sunrise.Sunrise;
import no.api.meteo.service.AbstractMeteoService;
import no.api.meteo.service.MeteoDataParser;
import no.api.meteo.util.MeteoDateUtils;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static no.api.meteo.util.MeteoConstants.PARAM_DATE;
import static no.api.meteo.util.MeteoConstants.PARAM_FROM;
import static no.api.meteo.util.MeteoConstants.PARAM_LATITUDE;
import static no.api.meteo.util.MeteoConstants.PARAM_LONGITUDE;
import static no.api.meteo.util.MeteoConstants.PARAM_TO;

public final class SunriseService extends AbstractMeteoService {

    private static final String MET_SERVICE_NAME = "sunrise";

    private static final MeteoServiceVersion VERSION = new MeteoServiceVersion(1, 0);

    private final MeteoDataParser<Sunrise> parser;

    public SunriseService(MeteoClient meteoClient) {
        super(meteoClient, MET_SERVICE_NAME, VERSION);
        parser = new SunriseParser();
    }

    public MeteoData<Sunrise> fetchContent(double longitude, double latitude, Date date) throws MeteoException {
        MeteoResponse response = getMeteoClient().fetchContent(createServiceUrl(longitude, latitude, date));
        return new MeteoData<>(parser.parse(response.getData()), response);
    }

    public MeteoData<Sunrise> fetchContent(double longitude, double latitude, Date from, Date to) throws MeteoException {
        MeteoResponse response = getMeteoClient().fetchContent(createServiceUrlFromTo(longitude, latitude, from, to));
        return new MeteoData<>(parser.parse(response.getData()), response);
    }

    private URL createServiceUrl(double longitude, double latitude, Date date) throws MeteoException {
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(PARAM_LONGITUDE, longitude);
        queryParameters.put(PARAM_LATITUDE, latitude);
        queryParameters.put(PARAM_DATE, MeteoDateUtils.dateToYyyyMMdd(date));
        return createRequestUrl(queryParameters);
    }

    private URL createServiceUrlFromTo(double longitude, double latitude, Date from, Date to) throws MeteoException {
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put(PARAM_LONGITUDE, longitude);
        queryParameters.put(PARAM_LATITUDE, latitude);
        queryParameters.put(PARAM_FROM, MeteoDateUtils.dateToYyyyMMdd(from));
        queryParameters.put(PARAM_TO, MeteoDateUtils.dateToYyyyMMdd(to));
        return createRequestUrl(queryParameters);
    }
}
