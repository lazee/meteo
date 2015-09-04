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

import lombok.extern.slf4j.Slf4j;
import no.api.meteo.entity.core.service.locationforecast.Forecast;
import no.api.meteo.entity.core.service.locationforecast.LocationForecast;
import no.api.meteo.entity.core.service.locationforecast.PeriodForecast;
import no.api.meteo.entity.core.service.locationforecast.PointForecast;
import no.api.meteo.entity.extras.MeteoExtrasForecast;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static no.api.meteo.util.MeteoDateUtils.METZONE;
import static no.api.meteo.util.MeteoDateUtils.cloneZonedDateTime;
import static no.api.meteo.util.MeteoDateUtils.getNow;
import static no.api.meteo.util.MeteoDateUtils.toZeroMSN;

/**
 * Helper class that will save you from the dirty job of interpreting the forecast data yourself.
 *
 * <p>Interpreting the location forecast data can be pretty hard. This is an implementation of how we at Amedia
 * Utvikling chooses to do it.</p>
 */
@Slf4j
public final class LocationForecastHelper extends AbstractForecastHelper {


    /**
     * Construct a new instance of this helper without any title set.
     *
     * @param locationForecast
     *         The location forecast this helper will work on.
     */
    public LocationForecastHelper(LocationForecast locationForecast) {
        super(locationForecast);
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
            Optional<PointForecast> pointForecast = getIndexer().getPointForecast(ahead);
            if (pointForecast.isPresent()) {
                Optional<PeriodForecast> periodForecast =
                        getIndexer().getTightestFitPeriodForecast(pointForecast.get().getFrom());
                if (periodForecast.isPresent()) {
                    pointExtrasForecasts.add(new MeteoExtrasForecast(pointForecast.get(), periodForecast.get()));
                }
            }
        }
        return pointExtrasForecasts;
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
        ZonedDateTime dt = toZeroMSN(dateTime.withZoneSameInstant(METZONE));
        PointForecast chosenForecast = null;
        for (Forecast forecast : getLocationForecast().getForecasts()) {
            if (forecast instanceof PointForecast) {
                PointForecast pointForecast = (PointForecast) forecast;
                if (isDateMatch(dt, cloneZonedDateTime(pointForecast.getFrom()))) {
                    chosenForecast = pointForecast;
                    break;
                } else if (chosenForecast == null) {
                    chosenForecast = pointForecast;
                } else if (isNearerDate(pointForecast.getFrom(), dt, chosenForecast.getFrom())) {
                    chosenForecast = pointForecast;
                }
            }
        }
        return chosenForecast == null
                ? Optional.empty()
                : Optional.of(new MeteoExtrasForecast(
                        chosenForecast, getIndexer().getWidestFitPeriodForecast(chosenForecast.getFrom()).get()));
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