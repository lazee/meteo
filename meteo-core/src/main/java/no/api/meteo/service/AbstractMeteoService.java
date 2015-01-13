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

package no.api.meteo.service;

import no.api.meteo.client.MeteoClient;
import no.api.meteo.entity.core.MeteoServiceVersion;
import no.api.meteo.util.METServiceUrlBuilder;

public abstract class AbstractMeteoService {

    private final MeteoServiceVersion serviceVersion;

    private final String metServiceName;

    private final MeteoClient meteoClient;
    
    public AbstractMeteoService(MeteoClient meteoClient, String metServiceName, MeteoServiceVersion serviceVersion) {
        this.meteoClient = meteoClient;
        this.metServiceName = metServiceName;
        this.serviceVersion = serviceVersion;
    }

    protected MeteoClient getMeteoClient() {
        return meteoClient;
    }

    protected METServiceUrlBuilder createServiceUrlBuilder() {
        return METServiceUrlBuilder.create(metServiceName, serviceVersion);
    }

}
