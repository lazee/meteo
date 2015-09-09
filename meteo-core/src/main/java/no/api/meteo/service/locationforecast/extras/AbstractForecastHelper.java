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

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import no.api.meteo.entity.core.service.locationforecast.LocationForecast;
import no.api.meteo.entity.core.service.locationforecast.PeriodForecast;
import no.api.meteo.entity.core.service.locationforecast.PointForecast;
import no.api.meteo.entity.extras.MeteoExtrasForecast;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;

import static no.api.meteo.util.MeteoDateUtils.METZONE;
import static no.api.meteo.util.MeteoDateUtils.toZeroMSN;

@Slf4j
public class AbstractForecastHelper {

    @Getter
    private final LocationForecast locationForecast;

    @Getter
    private MeteoForecastIndexer indexer = null;

    public AbstractForecastHelper(LocationForecast locationForecast) {
        this.locationForecast = locationForecast;
        indexer = new MeteoForecastIndexer(locationForecast.getForecasts());
    }

    public Optional<MeteoExtrasForecast> getForecastForPeriod(ZonedDateTime from, ZonedDateTime to) {
        ZonedDateTime fromz = toZeroMSN(from.withZoneSameInstant(METZONE));
        ZonedDateTime toz = toZeroMSN(to.withZoneSameInstant(METZONE));
        Optional<PeriodForecast> periodForecast = getIndexer().getExactFitPeriodForecast(fromz, toz);
        if (!periodForecast.isPresent()) {
            log.debug("Could not find period forecast for " + fromz.toString() + " -- " + toz.toString());
            return Optional.empty();
        }

        Optional<PointForecast> pointForecast = getPointForecastForPeriod(fromz, periodForecast);
        if (!pointForecast.isPresent()) {
            log.error("No point forecast for " + fromz.toString() + "  :" + fromz.toString() + " -- " +
                              toz.toString());
            return Optional.empty();
        }
        return Optional.of(new MeteoExtrasForecast(pointForecast.get(), periodForecast.get()));
    }

    private Optional<PointForecast> getPointForecastForPeriod(ZonedDateTime from,
                                                              Optional<PeriodForecast> periodForecast) {
        Duration d = Duration.between(periodForecast.get().getFrom(), periodForecast.get().getTo());
        long distance = (d.getSeconds() / 60 / 60 / 2);
        ZonedDateTime dateTime = periodForecast.get().getFrom().plusHours(distance);
        Optional<PointForecast> pointForecast = getIndexer().getPointForecast(dateTime);

        // Try to find point forecast in the middle of period
        if (!pointForecast.isPresent()) {

            // Try to find period forecast for the from time and five hours ahead.
            // Might be a little bit dirty implementation, but it should do for now.
            pointForecast = Optional.empty();

            for (int i = 0; i < 5; i++) {
                if (!pointForecast.isPresent()) {
                    pointForecast = getIndexer().getPointForecast(from.plusHours(i));
                }
            }
        }
        return pointForecast;
    }


}
