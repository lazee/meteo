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
import lombok.extern.slf4j.Slf4j;
import no.api.meteo.MeteoException;
import no.api.meteo.entity.core.service.locationforecast.LocationForecast;
import no.api.meteo.entity.core.service.locationforecast.PeriodForecast;
import no.api.meteo.entity.core.service.locationforecast.PointForecast;
import no.api.meteo.entity.extras.MeteoExtrasForecast;
import no.api.meteo.entity.extras.MeteoExtrasForecastDay;
import no.api.meteo.entity.extras.MeteoExtrasLongTermForecast;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static no.api.meteo.util.MeteoDateUtils.METZONE;
import static no.api.meteo.util.MeteoDateUtils.getNow;
import static no.api.meteo.util.MeteoDateUtils.toZeroHMSN;
import static no.api.meteo.util.MeteoDateUtils.toZeroMSN;

@Slf4j
public class LongtermForecastHelper {

    private MeteoForecastIndexer indexer = null;

    private final LongtermTimeSeries series;

    /**
     * Construct a new instance of this helper without any title set.
     *
     * @param locationForecast
     *         The location forecast this helper will work on.
     */
    public LongtermForecastHelper(@NonNull LocationForecast locationForecast) {
        indexer = new MeteoForecastIndexer(locationForecast.getForecasts());
        series = new LongtermTimeSeries();
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
     * Create a longterm forecast.
     *
     * @return A long term forecast, which is a week in our view of the world. But how many days you will get in this
     * forecast depends on the given location forecast. So from 0-7 can be expected.
     */
    public MeteoExtrasLongTermForecast createLongTermForecast() {
        List<MeteoExtrasForecastDay> forecastDays = new ArrayList<>();
        ZonedDateTime dt = toZeroHMSN(getNow().plusDays(1));


        for (int i = 0; i < series.getSeries().size(); i++) {
            createLongTermForecastDay(dt.plusDays(i), series.getSeries().get(i))
                    .ifPresent(e -> forecastDays.add(e));

        }

        return new MeteoExtrasLongTermForecast(forecastDays);
    }

    private Optional<MeteoExtrasForecastDay> createLongTermForecastDay(ZonedDateTime dt,
                                                                       LongtermTimeSeries.LongtermTimeSerie serie) {
        if (!indexer.hasForecastsForDay(dt)) {
            return Optional.empty();
        }

        List<MeteoExtrasForecast> forecasts = new ArrayList<>();
        for (LongtermTimeSeries.FromTo fromTo : serie.getFromTos()) {
            getForecastForPeriod(dt.plusHours(fromTo.getFrom()),
                                 dt.plusHours(fromTo.getTo()))
                    .ifPresent(forecasts::add);
        }
        return forecasts.size() > 0
                ? Optional.of(new MeteoExtrasForecastDay(dt.toLocalDate(), forecasts))
                : Optional.empty();
    }

    private Optional<MeteoExtrasForecast> getForecastForPeriod(ZonedDateTime from, ZonedDateTime to) {
        Optional<PeriodForecast> periodForecast = indexer.getExactFitPeriodForecast(from, to);
        if (!periodForecast.isPresent()) {
            log.error("Could not find period forecast for " + from.toString() + " -- " + to.toString());
            return Optional.empty();
        }

        Optional<PointForecast> pointForecast = getPointForecastForPeriod(from, periodForecast);
        if (!pointForecast.isPresent()) {
            log.error("No point forecast for " + from.toString() + "  :" + from.toString() + " -- " +
                              to.toString());
            return Optional.empty();
        }
        return Optional.of(new MeteoExtrasForecast(pointForecast.get(), periodForecast.get()));
    }

    private Optional<PointForecast> getPointForecastForPeriod(ZonedDateTime from,
                                                              Optional<PeriodForecast> periodForecast) {
        Duration d = Duration.between(periodForecast.get().getFrom(), periodForecast.get().getTo());
        long distance = (d.getSeconds() / 60 / 60 / 2);
        ZonedDateTime dateTime = periodForecast.get().getFrom().plusHours(distance);
        Optional<PointForecast> pointForecast = indexer.getPointForecast(dateTime);

        // Try to find point forecast in the middle of period
        if (!pointForecast.isPresent()) {

            // Try to find period forecast for the from time and five hours ahead.
            // Might be a little bit dirty implementation, but it should do for now.
            pointForecast = Optional.empty();

            for (int i = 0; i < 5; i++) {
                if (!pointForecast.isPresent()) {
                    pointForecast = indexer.getPointForecast(from.plusHours(i));
                }
            }
        }
        return pointForecast;
    }

    public MeteoExtrasForecastDay createSimpleForcastForDay(ZonedDateTime dateTime) {
        ZonedDateTime dt = toZeroMSN(dateTime.withZoneSameInstant(METZONE));
        List<MeteoExtrasForecast> forecasts = new ArrayList<>();
        //findNearestForecast(dt.withHour(12)).ifPresent(forecasts::add);
        return new MeteoExtrasForecastDay(dt.toLocalDate(), forecasts);
    }

}
