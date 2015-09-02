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

package no.api.meteo.service.locationforecast.extras;

import lombok.NonNull;
import no.api.meteo.MeteoException;
import no.api.meteo.entity.core.service.locationforecast.Forecast;
import no.api.meteo.entity.core.service.locationforecast.LocationForecast;
import no.api.meteo.entity.core.service.locationforecast.PeriodForecast;
import no.api.meteo.entity.core.service.locationforecast.PointForecast;
import no.api.meteo.entity.extras.MeteoExtrasForecast;
import no.api.meteo.entity.extras.MeteoExtrasForecastDay;
import no.api.meteo.entity.extras.MeteoExtrasLongTermForecast;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static no.api.meteo.util.MeteoDateUtils.cloneZonedDateTime;

/**
 * Helper class that will save you from the dirty job of interpreting the forecast data yourself.
 *
 * <p>Interpreting the location forecast data can be pretty hard. This is an implementation of how we at Amedia
 * Utvikling chooses to do it.</p>
 */
public final class LocationForecastHelper {

    private final LocationForecast locationForecast;

    private MeteoForecastIndexer indexer = null;

    private final ZoneId zoneId;

    /**
     * Construct a new instance of this helper without any title set.
     *
     * @param locationForecast
     *         The location forecast this helper will work on.
     */
    public LocationForecastHelper(@NonNull LocationForecast locationForecast) {
        this.locationForecast = locationForecast;
        zoneId = ZoneId.of("Z");
        indexer = new MeteoForecastIndexer(locationForecast.getForecasts());
    }

    /**
     * Get all point forecasts from now and to the given hours ahead.
     *
     * @param hoursAhead
     *         The number of hours to look ahead for point forecasts.
     *
     * @return List of found forecasts.
     */
    public List<MeteoExtrasForecast> findHourlyPointForecastsFromNow(int hoursAhead) {
        List<MeteoExtrasForecast> pointExtrasForecasts = new ArrayList<>();
        ZonedDateTime now = getNow();
        for (int i = 0; i < hoursAhead; i++) {
            ZonedDateTime ahead = now.plusHours(i);
            Optional<PointForecast> pointForecast = indexer.getPointForecast(ahead);
            if (pointForecast.isPresent()) {
                Optional<PeriodForecast> periodForecast =
                        indexer.getTightestFitPeriodForecast(pointForecast.get().getFromTime());
                if (periodForecast.isPresent()) {
                    pointExtrasForecasts.add(new MeteoExtrasForecast(pointForecast.get(), periodForecast.get()));
                }
            }
        }
        return pointExtrasForecasts;
    }

    private ZonedDateTime getNow() {
        return ZonedDateTime.now(zoneId);
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
    public Optional<MeteoExtrasForecast> findBestForecastForPeriod(ZonedDateTime from, ZonedDateTime to) {

        // Make sure the given input dates are converted to UTC before moving on.
        Optional<PeriodForecast> periodForecast = indexer.getBestFitPeriodForecast(
                from.withZoneSameInstant(zoneId),
                to.withZoneSameInstant(zoneId));

        if (!periodForecast.isPresent()) {
            return Optional.empty();
        }
        Optional<PointForecast> pointForecast = indexer.getPointForecast(periodForecast.get().getFromTime());
        if (!pointForecast.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(new MeteoExtrasForecast(pointForecast.get(), periodForecast.get()));
    }

    /**
     * Create a longterm forecast.
     *
     * @return A long term forecast, which is a week in our view of the world. But how many days you will get in this
     * forecast depends on the given location forecast. So from 0-7 can be expected.
     */
    public MeteoExtrasLongTermForecast createLongTermForecast() {
        List<MeteoExtrasForecastDay> forecastDays = new ArrayList<>();
        ZonedDateTime dt = getNow();
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
     *
     * @throws MeteoException
     *         If an error occurred while creating the simple longterm forecast.
     */
    public MeteoExtrasLongTermForecast createSimpleLongTermForecast() throws MeteoException {
        List<MeteoExtrasForecastDay> forecastDays = new ArrayList<>();
        ZonedDateTime dt = getNow();
        for (int i = 0; i <= 6; i++) {
            ZonedDateTime dti = dt.plusDays(i);
            if (indexer.hasForecastsForDay(dti)) {
                MeteoExtrasForecastDay mefd = createSimpleForcastForDay(dti);
                if (mefd != null && mefd.getForecasts().size() > 0) {
                    forecastDays.add(mefd);
                }
            }
        }
        return new MeteoExtrasLongTermForecast(forecastDays);
    }

    /**
     * Create a detailed forecast for a given date within this location forecast.
     *
     * @param dateTime
     *         The date to create the forecast for.
     *
     * @return The detailed forecast for the given date. Will be empty if data is not found.
     */
    public MeteoExtrasForecastDay createForcastForDay(ZonedDateTime dateTime) {
        ZonedDateTime dt = dateTime.withZoneSameInstant(zoneId);
        List<MeteoExtrasForecast> forecasts = new ArrayList<>();
        findBestForecastForPeriod(dt.withHour(0), dt.withHour(6)).ifPresent(forecasts::add);
        findBestForecastForPeriod(dt.withHour(6), dt.withHour(12)).ifPresent(forecasts::add);
        findBestForecastForPeriod(dt.withHour(12), dt.withHour(18)).ifPresent(forecasts::add);
        findBestForecastForPeriod(dt.withHour(18), dt.plusDays(1).withHour(0)).ifPresent(forecasts::add);
        return new MeteoExtrasForecastDay(dt.toLocalDate(), forecasts);
    }

    public MeteoExtrasForecastDay createSimpleForcastForDay(ZonedDateTime dateTime) {
        ZonedDateTime dt = dateTime.withZoneSameInstant(zoneId);
        List<MeteoExtrasForecast> forecasts = new ArrayList<>();
        findNearestForecast(dt.withHour(14)).ifPresent(forecasts::add);
        return new MeteoExtrasForecastDay(dt.toLocalDate(), forecasts);
    }

    /**
     * Get the most accurate forecast for the given date.
     *
     * @param dateTime
     *         The date to get the forecast for.
     *
     * @return Optional containing the forecast if found, else {@link Optional#empty()}
     */
    public Optional<MeteoExtrasForecast> findNearestForecast(ZonedDateTime dateTime) {
        ZonedDateTime dt = dateTime.withZoneSameInstant(zoneId);
        PointForecast chosenForecast = null;
        for (Forecast forecast : locationForecast.getForecasts()) {
            if (forecast instanceof PointForecast) {
                PointForecast pointForecast = (PointForecast) forecast;
                if (isDateMatch(dt, cloneZonedDateTime(pointForecast.getFromTime()))) {
                    chosenForecast = pointForecast;
                    break;
                } else if (chosenForecast == null) {
                    chosenForecast = pointForecast;
                } else if (isNearerDate(pointForecast.getFromTime(), dt, chosenForecast.getFromTime())) {
                    chosenForecast = pointForecast;
                }
            }
        }
        return chosenForecast == null
                ? Optional.empty()
                : Optional.of(new MeteoExtrasForecast(
                        chosenForecast, indexer.getWidestFitPeriodForecast(chosenForecast.getFromTime()).get()));
    }

    private void addForecastForDay(ZonedDateTime dt, List<MeteoExtrasForecastDay> lst) {
        if (indexer.hasForecastsForDay(dt)) {
            MeteoExtrasForecastDay mefd = createForcastForDay(dt);
            if (mefd != null && mefd.getForecasts().size() > 0) {
                lst.add(mefd);
            }
        }
    }

    private boolean isNearerDate(ZonedDateTime pointTime, ZonedDateTime dateTime, ZonedDateTime chosenTime) {
        return Math.abs(pointTime.toInstant().getEpochSecond() - dateTime.toInstant().getEpochSecond())
                < Math.abs(chosenTime.toInstant().getEpochSecond() - dateTime.toInstant().getEpochSecond());
    }

    private boolean isDateMatch(ZonedDateTime requestedDate, ZonedDateTime actualDate) {
        return requestedDate.getYear() == actualDate.getYear() &&
                requestedDate.getMonthValue() == actualDate.getMonthValue()
                && requestedDate.getDayOfMonth() == actualDate.getDayOfMonth() &&
                requestedDate.getHour() == actualDate.getHour();
    }

}