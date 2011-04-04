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

package no.api.meteo.yr;

import no.api.meteo.MeteoNetUtils;
import no.api.meteo.MeteoRuntimeException;
import no.api.meteo.client.MeteoClient;
import no.api.meteo.client.MeteoResponse;

public class YRManager {

    public static final String ERROR_THE_GIVEN_PLACE_NAME_IS_INVALID = "The given placeName is invalid: ";

    private String yrUrlPrefix = "http://www.yr.no/";

    private String yrUrlPostfix = "/varsel.xml";

    private MeteoClient meteoClient;

    public YRManager(MeteoClient meteoClient) {
        this.meteoClient = meteoClient;
    }

    /**
     * Fetch forecast data from yr.no.
     *
     * @param placeName XXX
     * @param locale    Set locale for what language to be used for the returned content. This must be one of the
     *                  languages supported by yr.no.
     * @return Dataholder object containing the fetched XML data.
     * @throws no.api.meteo.MeteoRuntimeException
     *          If content couldn't be fetched from the yr.no servers.
     */
    public YRContent fetchForecast(String placeName, YRLocale locale) {
        if (!YrUtil.isLegalPlaceName(placeName)) {
            throw new MeteoRuntimeException(ERROR_THE_GIVEN_PLACE_NAME_IS_INVALID + placeName);
        }
        MeteoResponse response = meteoClient.fetchContent(
                MeteoNetUtils.createUrl(yrUrlPrefix + locale.getPlace() + "/" + placeName + yrUrlPostfix));
        return new YRContent(response.getData());
    }

}
