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

package no.api.meteo.services;

import no.api.meteo.entity.MeteoData;
import no.api.meteo.MeteoException;
import no.api.meteo.client.MeteoClient;
import no.api.meteo.entity.Coordinates;
import no.api.meteo.service.locationforecastlts.entity.LocationForecast;
import no.api.meteo.service.locationforecastlts.entity.PointForecast;
import no.api.meteo.service.locationforecastlts.LocationforecastLTSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * High-level api for Meteo offering a number of extra services.
 *
 * In addition to the low-level api found in meteo-core, this api offers extra functionality that hopefully will
 * make it easier to use the MET data.
 * 
 */
public class MeteoServiceManagerImpl implements MeteoServicesManager {

    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    private MeteoClient meteoClient;

    public MeteoServiceManagerImpl(MeteoClient meteoClient) {
        this.meteoClient = meteoClient;
    }

    /**
     * Will fetch point forecasts for the next given number of hours, starting from the current hour.
     *
     * Each point forecast representing a hour.
     *
     * @param hours The next number of hours to fetch point forecasts for. Please note that the method will only be
     * able to return what is available in the feed.
     * @param coordinates The coordinates pointing to the location from where the point forecasts should be collected from.
     * @return List of point forecasts for the requested location. Empty list if none where found.
     */
    @Override
    public List<PointForecast> fetchPointForecastsByHour(int hours, Coordinates coordinates) {
        List<PointForecast> pointForecasts = new ArrayList<PointForecast>();
        LocationforecastLTSService locationforecastLTSService = new LocationforecastLTSService(meteoClient);
        try {
            MeteoData<LocationForecast> meteoData = locationforecastLTSService.fetchContent(coordinates);
            if (log.isTraceEnabled()) {
                log.trace("Fetched content:" + meteoData.getRawResult());
            }
        } catch (MeteoException e) {
            log.error("Could not fetch point forecasts!", e);
        }
        return pointForecasts;
    }

}
