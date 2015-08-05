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

package no.api.meteo.services;

import lombok.NonNull;
import no.api.meteo.MeteoException;
import no.api.meteo.entity.core.service.locationforecast.Forecast;
import no.api.meteo.entity.core.service.locationforecast.LocationForecast;
import no.api.meteo.entity.core.service.locationforecast.PeriodForecast;
import no.api.meteo.entity.core.service.locationforecast.PointForecast;
import no.api.meteo.entity.extras.MeteoExtrasForecast;
import no.api.meteo.entity.extras.MeteoExtrasForecastDay;
import no.api.meteo.entity.extras.MeteoExtrasLongTermForecast;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Helper class that will save you from the dirty job of interpreting the forecast data yourself.
 *
 * <p>Interpreting the location forecast data can be pretty hard. This is an implementation of how we at Amedia
 * Utvikling chooses to do it.</p>
 */
public final class LocationForecastHelper {

    private final LocationForecast locationForecast;

    private MeteoForecastIndexer indexer = null;

    private String title = null;

    /**
     * Construct a new instance of this helper
     *
     * @param locationForecast
     *         The location forecast this helper will work on.
     * @param title
     *         Set a title for the forecast. Eg: The name of the location. Like a city.
     */
    public LocationForecastHelper(@NonNull LocationForecast locationForecast, String title) {
        this.title = title;
        this.locationForecast = locationForecast;
        init();
    }

    /**
     * Construct a new instance of this helper without any title set.
     *
     * @param locationForecast
     *         The location forecast this helper will work on.
     */
    public LocationForecastHelper(@NonNull LocationForecast locationForecast) {
        this.locationForecast = locationForecast;
        init();
    }

    /**
     * Get the title
     *
     * @return The title as set upon construction of this helper.
     */
    public String getTitle() {
        return title;
    }


    /**
     * Get all point forecasts from now and to the given hours ahead.
     *
     * @param hoursAhead The number of hours to look ahead for point forecasts.
     * @return List of found forecasts.
     */
    public List<MeteoExtrasForecast> findHourlyPointForecastsFromNow(int hoursAhead) {
        List<MeteoExtrasForecast> pointExtrasForecasts = new ArrayList<>();
        DateTime timeNow = new DateTime();
        for (int i = 0; i < hoursAhead; i++) {
            DateTime dataTime = timeNow.plusHours(i);
            PointForecast pointForecast = indexer.getPointForecast(dataTime);
            if (pointForecast != null) {
                PeriodForecast periodForecast =
                        indexer.getTightestFitPeriodForecast(new DateTime(pointForecast.getFromTime()));
                pointExtrasForecasts.add(new MeteoExtrasForecast(pointForecast, periodForecast));
            }
        }
        return pointExtrasForecasts;
    }

    /**
     * Get what we consider the best forecast for a given period of time within the location forecast.
     *
     * @param from
     *         The start time.
     * @param to
     *         The end time.
     *
     * @return Optional containing a forecast or {@link Optional#empty()} if no forecast could be created.
     */
    public Optional<MeteoExtrasForecast> findBestForecastForPeriod(DateTime from, DateTime to) {
        PeriodForecast periodForecast = indexer.getBestFitPeriodForecast(from, to);
        if (periodForecast == null) {
            return Optional.empty();
        }
        PointForecast pointForecast = indexer.getPointForecast(new DateTime(periodForecast.getFromTime()));
        if (pointForecast == null) {
            return Optional.empty();
        }
        return Optional.of(new MeteoExtrasForecast(pointForecast, periodForecast));
    }

    /**
     * Create a longterm forecast.
     *
     * @return A long term forecast, which is a week in our view of the world. But how many days you will get in this
     * forecast depends on the given location forecast. So from 0-7 can be expected.
     */
    public MeteoExtrasLongTermForecast createLongTermForecast() {
        List<MeteoExtrasForecastDay> forecastDays = new ArrayList<>();
        DateTime dt = DateTime.now();
        for (int i = 0; i <= 6; i++) {
            addForecastForDay(dt.plusDays(i), forecastDays);
        }
        return new MeteoExtrasLongTermForecast(forecastDays);
    }

    /**
     * Create a longterm forecast, but only with a small subset of the weather data fields. Typically for use in simple
     * weather reports where you only show the predicted weather icon and temperature, and not all the weather details.
     *
     * @return A long term forecast, which is a week in our view of the world.
     */
    public MeteoExtrasLongTermForecast createSimpleLongTermForecast() throws MeteoException {
        List<MeteoExtrasForecastDay> forecastDays = new ArrayList<>();
        DateTime dt = DateTime.now();
        for (int i = 0; i <= 6; i++) {
            addSimpleForecastForDay(dt.plusDays(i), forecastDays);
        }
        return new MeteoExtrasLongTermForecast(forecastDays);
    }

    /**
     * Create a detailed forecast for a given date within this location forecast.
     *
     * @param dt
     *         The date to create the forecast for.
     *
     * @return The detailed forecast for the given date. Will be empty if data is not found.
     */
    public MeteoExtrasForecastDay createForcastForDay(DateTime dt) {
        List<MeteoExtrasForecast> forecasts = new ArrayList<>();
        addForecastToList(findBestForecastForPeriod(dt.withHourOfDay(0), dt.withHourOfDay(6)), forecasts);
        addForecastToList(findBestForecastForPeriod(dt.withHourOfDay(6), dt.withHourOfDay(12)), forecasts);
        addForecastToList(findBestForecastForPeriod(dt.withHourOfDay(12), dt.withHourOfDay(18)), forecasts);
        addForecastToList(findBestForecastForPeriod(dt.withHourOfDay(18), dt.plusDays(1).withHourOfDay(0)), forecasts);
        return new MeteoExtrasForecastDay(dt.toDate(), forecasts);
    }

    public MeteoExtrasForecastDay createSimpleForcastForDay(DateTime dt) {
        List<MeteoExtrasForecast> forecasts = new ArrayList<>();
        addForecastToList(findNearestForecast(dt.withHourOfDay(14).toDate()), forecasts);
        return new MeteoExtrasForecastDay(dt.toDate(), forecasts);
    }

    /**
     * Get the most accurate forecast for the current time.
     *
     * @return A forecast that best matches the time right now.
     */
    public Optional<MeteoExtrasForecast> getNearestForecast() {
        return findNearestForecast(new Date());
    }

    /**
     * Get the most accurate forecast for the given date.
     *
     * @param date The date to get the forecast for.
     * @return Optional containing the forecast if found, else {@link Optional#empty()}
     */
    public Optional<MeteoExtrasForecast> findNearestForecast(Date date) {
        if (date == null) {
            return Optional.empty();
        }
        return findNearestForecast(new DateTime(date));
    }

    /**
     * Get the most accurate forecast for the given date.
     *
     * @param date The date to get the forecast for.
     * @return Optional containing the forecast if found, else {@link Optional#empty()}
     */
    public Optional<MeteoExtrasForecast> findNearestForecast(DateTime date) {
        PointForecast chosenForecast = null;
        for (Forecast forecast : locationForecast.getForecasts()) {
            if (forecast instanceof PointForecast) {
                PointForecast pointForecast = (PointForecast) forecast;
                if (isDateMatch(date, new DateTime(pointForecast.getFromTime()))) {
                    chosenForecast = pointForecast;
                    break;
                } else if (chosenForecast == null) {
                    chosenForecast = pointForecast;
                } else if (isNearerDate(new DateTime(pointForecast.getFromTime()), date,
                                        new DateTime(chosenForecast.getFromTime()))) {
                    chosenForecast = pointForecast;
                }
            }
        }
        return (chosenForecast == null ?
                Optional.<MeteoExtrasForecast>empty() :
                Optional.of(new MeteoExtrasForecast(chosenForecast,
                                                    indexer.getWidestFitPeriodForecast(
                                                            new DateTime(chosenForecast.getFromTime())))));
    }

    private void init() {
        indexer = new MeteoForecastIndexer(locationForecast.getForecasts());
    }

    private void addForecastForDay(DateTime dt, List<MeteoExtrasForecastDay> lst) {
        if (indexer.hasForecastsForDay(dt)) {
            MeteoExtrasForecastDay mefd = createForcastForDay(dt);
            if (mefd != null && mefd.getForecasts().size() > 0) {
                lst.add(mefd);
            }
        }
    }

    private void addSimpleForecastForDay(DateTime dt, List<MeteoExtrasForecastDay> lst) throws MeteoException {
        if (indexer.hasForecastsForDay(dt)) {
            MeteoExtrasForecastDay mefd = createSimpleForcastForDay(dt);
            if (mefd != null && mefd.getForecasts().size() > 0) {
                lst.add(mefd);
            }
        }
    }

    private void addForecastToList(Optional<MeteoExtrasForecast> mef, List<MeteoExtrasForecast> lst) {
        if (mef.isPresent()) {
            lst.add(mef.get());
        }
    }

    private boolean isNearerDate(DateTime pointTime, DateTime dateTime, DateTime chosenTime) {
        return Math.abs(pointTime.getMillis() - dateTime.getMillis())
                < Math.abs(chosenTime.getMillis() - dateTime.getMillis());
    }

    private boolean isDateMatch(DateTime requestedDate, DateTime actualDate) {
        return requestedDate.getYear() == actualDate.getYear() &&
                requestedDate.getMonthOfYear() == actualDate.getMonthOfYear()
                && requestedDate.getDayOfMonth() == actualDate.getDayOfMonth() &&
                requestedDate.getHourOfDay() == actualDate.getHourOfDay();
    }


}
