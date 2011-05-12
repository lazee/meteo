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

import no.api.meteo.MeteoException;
import no.api.meteo.client.MeteoClient;
import no.api.meteo.client.MeteoData;
import no.api.meteo.service.locationforecast.LocationforecastLTSService;
import no.api.meteo.service.locationforecast.entity.Forecast;
import no.api.meteo.service.locationforecast.entity.LocationForecast;
import no.api.meteo.service.locationforecast.entity.PeriodForecast;
import no.api.meteo.service.locationforecast.entity.PointForecast;
import no.api.meteo.services.entity.MeteoExtrasForecast;
import no.api.meteo.services.internal.MeteoForecastHourIndexer;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationForecastHelper {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private MeteoClient meteoClient;

    private double longitude;

    private double latitude;

    private double altitude;

    private MeteoData<LocationForecast> meteoData = null;

    private LocationforecastLTSService locationforecastLTSService;

    private MeteoForecastHourIndexer indexer = null;

    private LocationForecastHelper(MeteoClient meteoClient, double longitude, double latitude, double altitude) {
        this.meteoClient = meteoClient;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        locationforecastLTSService = new LocationforecastLTSService(meteoClient);
    }

    public static LocationForecastHelper createInstance
            (MeteoClient meteoClient, double longitude, double latitude, double altitude) {

        LocationForecastHelper
                locationForecastHelper = new LocationForecastHelper(meteoClient, longitude, latitude, altitude);
        return locationForecastHelper;
    }

    public MeteoData<LocationForecast> getMeteoData() {
        return meteoData;
    }

    public void fetch() throws MeteoException {
        meteoData = locationforecastLTSService.fetchContent(longitude, latitude, altitude);
        if (meteoData != null && meteoData.getResult() != null) {
            indexer = new MeteoForecastHourIndexer(meteoData.getResult().getForecasts());
        }
    }

    public List<MeteoExtrasForecast> getPointForecastsByHour(int hours)
            throws MeteoException {

        validateIndexer();

        if (validData()) {
            return null;
        }

        List<MeteoExtrasForecast> pointExtrasForecasts = new ArrayList<MeteoExtrasForecast>();

        DateTime timeNow = new DateTime();
        for (int i = 0; i < hours; i++) {
            DateTime dataTime = timeNow.plusHours(i);
            PointForecast pointForecast = indexer.getPointForecast(dataTime);
            if (pointForecast != null) {
                PeriodForecast periodForecast =
                        indexer.getTightestFitPeriodForecast(new DateTime(pointForecast.getFromTime()));
                pointExtrasForecasts.add(new MeteoExtrasForecast(periodForecast, pointForecast));
            }
        }
        return pointExtrasForecasts;
    }

    private boolean validData() {
        if (meteoData == null || meteoData.getResult().getForecasts() == null) {
            return true;
        }
        return false;
    }

    private void validateIndexer() throws MeteoException {
        if (indexer == null) {
            throw new MeteoException("Indexer haven't been initialized. " +
                    "Something went wrong during fetching of the data");
        }
    }

    public MeteoExtrasForecast getNearestForecast(Date date) throws MeteoException {
        validateIndexer();

        if (validData()) {
            return null;
        }

        PointForecast chosenForecast = null;
        DateTime dateTime = new DateTime(date);
        for (Forecast forecast : meteoData.getResult().getForecasts()) {
            if (forecast instanceof PointForecast) {
                PointForecast pointForecast = (PointForecast) forecast;
                if (isDateMatch(dateTime, new DateTime(pointForecast.getFromTime()))) {
                    chosenForecast = pointForecast;
                    break;
                } else if (chosenForecast == null) {
                    chosenForecast = pointForecast;
                } else if (isNearerDate(new DateTime(pointForecast.getFromTime()), dateTime,
                        new DateTime(chosenForecast.getFromTime()))) {
                    chosenForecast = pointForecast;
                }
            }
        }
        return new MeteoExtrasForecast(indexer.getWidestFitPeriodForecast(new DateTime(chosenForecast.getFromTime())),
                chosenForecast);
    }

    private boolean isNearerDate(DateTime pointTime, DateTime dateTime, DateTime chosenTime) {
        if (Math.abs(pointTime.getMillis() - dateTime.getMillis()) <
                Math.abs(chosenTime.getMillis() - dateTime.getMillis())) {
            return true;
        }
        return false;
    }

    private boolean isDateMatch(DateTime requestedDate, DateTime actualDate) {
        if (requestedDate.getYear() == actualDate.getYear() &&
                requestedDate.getMonthOfYear() == actualDate.getMonthOfYear()
                && requestedDate.getDayOfMonth() == actualDate.getDayOfMonth() &&
                requestedDate.getHourOfDay() == actualDate.getHourOfDay()) {
            return true;
        }
        return false;
    }

}
