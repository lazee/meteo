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
import no.api.meteo.entity.core.service.locationforecast.Forecast;
import no.api.meteo.entity.core.service.locationforecast.LocationForecast;
import no.api.meteo.entity.core.service.locationforecast.PeriodForecast;
import no.api.meteo.entity.core.service.locationforecast.PointForecast;
import no.api.meteo.service.locationforecast.LocationforecastLTSService;
import no.api.meteo.entity.extras.MeteoExtrasForecast;
import no.api.meteo.services.internal.MeteoForecastHourIndexer;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class LocationForecastHelper {

    private LocationForecast locationForecast;

    private MeteoForecastHourIndexer indexer = null;

    public LocationForecastHelper(LocationForecast locationForecast) {
        this.locationForecast = locationForecast;
        if (this.locationForecast != null) {
            indexer = new MeteoForecastHourIndexer(locationForecast.getForecasts());
        }
    }

    public List<MeteoExtrasForecast> getPointForecastsByHour(int hours)
            throws MeteoException {

        validateIndexer();
        if (!validData()) {
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
        if (locationForecast == null) {
            return false;
        }
        return true;
    }

    private void validateIndexer() throws MeteoException {
        if (indexer == null) {
            throw new MeteoException("Indexer haven't been initialized. " +
                    "Something went wrong during fetching of the data");
        }
    }

    public MeteoExtrasForecast getNearestForecast(Date date) throws MeteoException {
        validateIndexer();

        if (!validData()) {
            return null;
        }

        PointForecast chosenForecast = null;
        DateTime dateTime = new DateTime(date);
        for (Forecast forecast : locationForecast.getForecasts()) {
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
