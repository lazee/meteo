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

import java.time.LocalDate;

import static no.api.meteo.util.MeteoConstants.PARAM_DATE;
import static no.api.meteo.util.MeteoConstants.PARAM_FROM;
import static no.api.meteo.util.MeteoConstants.PARAM_LATITUDE;
import static no.api.meteo.util.MeteoConstants.PARAM_LONGITUDE;
import static no.api.meteo.util.MeteoConstants.PARAM_TO;
import static no.api.meteo.util.MeteoDateUtils.zonedDateTimeToYyyyMMdd;

public final class SunriseService extends AbstractMeteoService {

    private final MeteoDataParser<Sunrise> parser;

    public SunriseService(MeteoClient meteoClient) {
        super(meteoClient, "sunrise", new MeteoServiceVersion(1, 0));
        parser = new SunriseParser();
    }

    public MeteoData<Sunrise> fetchContent(double longitude, double latitude, LocalDate date) throws MeteoException {
        MeteoResponse response = getMeteoClient().fetchContent(
                createServiceUriBuilder()
                        .addParameter(PARAM_LATITUDE, latitude)
                        .addParameter(PARAM_LONGITUDE, longitude)
                        .addParameter(PARAM_DATE, zonedDateTimeToYyyyMMdd(date)).build());
        return new MeteoData<>(parser.parse(response.getData()), response);
    }

    public MeteoData<Sunrise> fetchContent(double longitude, double latitude, LocalDate from, LocalDate to)
            throws MeteoException {
        MeteoResponse response = getMeteoClient().fetchContent(
                createServiceUriBuilder()
                        .addParameter(PARAM_LATITUDE, latitude)
                        .addParameter(PARAM_LONGITUDE, longitude)
                        .addParameter(PARAM_FROM, zonedDateTimeToYyyyMMdd(from))
                        .addParameter(PARAM_TO, zonedDateTimeToYyyyMMdd(to)).build()
        );
        return new MeteoData<>(parser.parse(response.getData()), response);
    }

}
