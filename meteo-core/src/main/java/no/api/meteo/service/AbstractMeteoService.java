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

package no.api.meteo.service;

import no.api.meteo.MeteoException;
import no.api.meteo.client.MeteoClient;
import no.api.meteo.service.entity.MeteoServiceVersion;
import no.api.meteo.util.MeteoNetUtils;

import java.net.URL;
import java.util.Map;

public abstract class AbstractMeteoService {

    private static final String API_MET_NO_SERVICE_PREFIX = "http://api.met.no/weatherapi/";

    private MeteoServiceVersion serviceVersion;

    private String metServiceName;

    private final MeteoClient meteoClient;
    
    public AbstractMeteoService(MeteoClient meteoClient, String metServiceName, MeteoServiceVersion serviceVersion) {
        this.meteoClient = meteoClient;
        this.metServiceName = metServiceName;
        this.serviceVersion = serviceVersion;
        
    }

    protected MeteoClient getMeteoClient() {
        return meteoClient;
    }

    protected URL createRequestUrl(Map<String, Object> queryParameters) throws MeteoException {
            StringBuffer sb = new StringBuffer();
            sb.append(API_MET_NO_SERVICE_PREFIX).append(metServiceName).append("/").append(
                    serviceVersion.toStringVersion()).append("/?");

            boolean first = true;
            for (String s : queryParameters.keySet()) {
                sb.append(first ? "" : "&").append(s).append("=").append(queryParameters.get(s).toString());
                first = false;
            }
            return MeteoNetUtils.createUrl(sb.toString());
    }


}
