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

import lombok.extern.slf4j.Slf4j;
import no.api.meteo.entity.core.service.locationforecast.Forecast;
import no.api.meteo.entity.core.service.locationforecast.PeriodForecast;
import no.api.meteo.entity.core.service.locationforecast.PointForecast;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static no.api.meteo.util.MeteoDateUtils.cloneZonedDateTime;

@Slf4j
class MeteoForecastIndexer {

    private final List<Forecast> forecasts;

    private Map<HourIndexKey, List<ScoreForecast>> hourIndex;

    private Map<DayIndexKey, List<PeriodForecast>> dayIndex;

    MeteoForecastIndexer(List<Forecast> forecasts) {
        this.forecasts = forecasts;
        init();
    }

    /**
     * Get the PointForecast that matches the given date.
     *
     * @param dateTime
     *         Date object used to find a matching PointForecast.
     *
     * @return A matching PointForecast if found, else <code>null</code>.
     */
    PointForecast getPointForecast(ZonedDateTime dateTime) {
        for (Forecast forecast : forecasts) {
            if (forecast instanceof PointForecast) {
                PointForecast pointForecast = (PointForecast) forecast;
                if (createHourIndexKey(dateTime)
                        .equals(createHourIndexKey(cloneZonedDateTime(pointForecast.getFromTime())))) {
                    return pointForecast;
                }
            }
        }
        return null;
    }

    List<PeriodForecast> getMatchingPeriodForecasts(ZonedDateTime from) {
        List<PeriodForecast> periodForecasts = new ArrayList<>();
        List<ScoreForecast> scoreForecasts = getMatchingScoreForecasts(from);
        if (scoreForecasts == null) {
            return periodForecasts;
        }
        periodForecasts.addAll(
                scoreForecasts
                        .stream()
                        .map(ScoreForecast::getPeriodForecast)
                        .collect(Collectors.toList()));
        return periodForecasts;
    }

    List<ScoreForecast> getMatchingScoreForecasts(ZonedDateTime from) {
        return hourIndex.get(createHourIndexKey(cloneZonedDateTime(from)));
    }

    /*
    * Find the period forecast that is the tightest fit for a point forecast.
    *
    * <p>We use two rules to decide what the "tightest fit" is. First we look at the time span within a PeriodForecast.
    * Secondly we look at the distance from the start of the PeriodForecast to the PointForecast. The sum of these two
    * numbers gives a tightScore. The period forecast with the lowest tightScore is considered to be the tightest
    * fit.</p>
    *
    * <h3>Example</h3>
    *
    * <p>Lets says we have a PeriodForecast with fromTime set to 12:00 PM and toTime set to 04:00 PM. This gives us a 4
    * hour span.</p>
    *
    * <pre>
    *  12:00 PM.................................................................................04:00 PM
    *
    *  12:00 PM...............01:00 PM
    * </pre>
    *
    * <p>Then we have a PointForecast for 01:00 PM. The distance from 01:00 PM to 12:00 PM is 1 hour. When we add the
    * span (4) and the distance (1) we get the tightScore (5).</p>
    *
    */
    PeriodForecast getTightestFitPeriodForecast(ZonedDateTime from) {
        ScoreForecast scoreForecast = getTightestFitScoreForecast(from);
        if (scoreForecast == null) {
            return null;
        }
        return scoreForecast.getPeriodForecast();
    }

    boolean hasForecastsForDay(ZonedDateTime from) {
        return dayIndex.containsKey(new DayIndexKey(from));
    }

    ScoreForecast getTightestFitScoreForecast(ZonedDateTime from) {
        return getXScoreForecast(from, false);
    }

    /*
    * Find the period forecast that is the widest fit for a point forecast.
    *
    * <p>We use the exact same kind of logic to find the widest fit as we do for the tightest fit. The only change is
    * that we invert the span tightScore. Meaning that a large span will tightScore lower than a small span</p>
    *
    */
    PeriodForecast getWidestFitPeriodForecast(ZonedDateTime from) {
        ScoreForecast scoreForecast = getWidestFitScoreForecast(from);
        if (scoreForecast == null) {
            return null;
        }
        return scoreForecast.getPeriodForecast();
    }

    /**
     * Find the period forecast that has the best fitted forecast for a given period.
     *
     * This means a forecast covering the requested period, with to and from time closest possible to that period. If
     * this function only can find a period forecast that covers parts of the requested period, then the forecast
     * covering the most will be used.
     */
    PeriodForecast getBestFitPeriodForecast(ZonedDateTime from, ZonedDateTime to) {
        if (from == null || to == null) {
            return null;
        }

        // Making sure that we remove minutes, seconds and milliseconds from the request timestamps
        ZonedDateTime requestFrom = from.withMinute(0).withSecond(0).withNano(0);
        ZonedDateTime requestTo = to.withMinute(0).withSecond(0).withNano(0);

        //  Get list of period forecasts for the requested day. Return null if date isn't present
        List<PeriodForecast> forecastsList = dayIndex.get(new DayIndexKey(requestFrom));
        if (forecastsList == null) {
            return null;
        }

        PeriodForecast chosenForecast = null;
        long score = 0;
        long tmpScore = 0;

        for (PeriodForecast forecast : forecastsList) {
            ZonedDateTime actualFrom = cloneZonedDateTime(forecast.getFromTime());
            ZonedDateTime actualTo = cloneZonedDateTime(forecast.getToTime());

            if (requestFrom.equals(actualFrom) && requestTo.equals(actualTo)) {
                return forecast;
            } else if (
                    (requestFrom.isBefore(actualFrom) && requestTo.isBefore(actualFrom)) ||
                            (requestFrom.isAfter(actualTo) && requestTo.isAfter(actualTo)) ||
                            actualTo.isEqual(actualFrom)) {
                continue; // this period is outside the requested period or this is a point forecast.
            } else if (requestFrom.isBefore(actualFrom) && requestTo.isBefore(actualTo)) {
                tmpScore = hoursBetween(requestTo, actualFrom);
            } else if ((actualFrom.isBefore(requestFrom) || actualFrom.isEqual(requestFrom)) &&
                    actualTo.isBefore(requestTo)) {
                tmpScore = hoursBetween(actualTo, requestFrom);
            } else if (actualFrom.isAfter(requestFrom) &&
                    (actualTo.isBefore(requestTo) || actualTo.isEqual(requestTo))) {
                tmpScore = hoursBetween(actualTo, actualFrom);
            } else if (actualFrom.isBefore(requestFrom) && actualTo.isAfter(requestTo)) {
                tmpScore = hoursBetween(requestTo, requestFrom);
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm");
                log.warn("Unhandled forecast Requested period:" + requestFrom.format(formatter) + "--" +
                                 requestTo.format(formatter) + ", Actual period: " +
                                 actualFrom.format(formatter) + "--" + actualTo.format(formatter));
            }

            tmpScore = Math.abs(tmpScore);

            if ((score == 0 && tmpScore > 0) || tmpScore > score) {
                score = tmpScore;
                chosenForecast = forecast;
            }
        }

        return chosenForecast;
    }



    ScoreForecast getWidestFitScoreForecast(ZonedDateTime time) {
        return getXScoreForecast(time, true);
    }


    private ScoreForecast getXScoreForecast(ZonedDateTime time, boolean widest) {
        if (time == null) {
            return null;
        }
        HourIndexKey hourIndexKey = createHourIndexKey(cloneZonedDateTime(time));
        List<ScoreForecast> scoreForecasts = hourIndex.get(hourIndexKey);
        if (scoreForecasts == null) {
            return null;
        }
        ScoreForecast lowest = null;
        for (ScoreForecast scoreForecast : scoreForecasts) {
            if (lowest == null) {
                lowest = scoreForecast;
            } else {
                long lowestScore = (widest ? lowest.getPointWideScore() : lowest.getPointTightScore());
                long forecastScore = (widest ? scoreForecast.getPointWideScore() : scoreForecast.getPointTightScore());
                if (forecastScore < lowestScore) {
                    lowest = scoreForecast;
                }
            }
        }
        return lowest;
    }

    private void init() {
        hourIndex = new HashMap<>();
        dayIndex = new HashMap<>();
        for (Forecast forecast : forecasts) {

            if (forecast instanceof PeriodForecast) {
                PeriodForecast periodForecast = (PeriodForecast) forecast;

                // Handle hour index
                List<HourIndexKey> indexKeys = createIndexKeysFromPeriodForecast(periodForecast);
                for (HourIndexKey hourIndexKey : indexKeys) {
                    ScoreForecast scoreForecast =
                            new ScoreForecast(periodForecast, calculatePointScore(periodForecast, hourIndexKey, 1),
                                              calculatePointScore(periodForecast, hourIndexKey, -1));
                    addForecastToHourIndex(hourIndexKey, scoreForecast);
                }

                // Handle period index
                DayIndexKey dayIndexKey = new DayIndexKey(cloneZonedDateTime(periodForecast.getFromTime()));
                addForecastToPeriodIndex(dayIndexKey, periodForecast);
            }
        }
    }

    private long calculatePointScore(PeriodForecast periodForecast, HourIndexKey hourIndexKey, int spanWeight) {
        ZonedDateTime periodFromTime = cloneZonedDateTime(periodForecast.getFromTime());
        ZonedDateTime periodToTime = cloneZonedDateTime(periodForecast.getToTime());
        return (hoursBetween(periodFromTime, periodToTime) * spanWeight) +
                hoursBetween(periodFromTime, hourIndexKey.getDateTime());
    }

    private long hoursBetween(ZonedDateTime from, ZonedDateTime to) {
        return Duration.between(from.toInstant(), to.toInstant()).toHours();
    }

    private List<HourIndexKey> createIndexKeysFromPeriodForecast(PeriodForecast periodForecast) {
        List<HourIndexKey> keyList = new ArrayList<>();
        ZonedDateTime fromTime = cloneZonedDateTime(periodForecast.getFromTime());
        ZonedDateTime activeTime = cloneZonedDateTime(periodForecast.getToTime());
        while (activeTime.isAfter(fromTime)) {
            keyList.add(createHourIndexKey(activeTime));
            activeTime = activeTime.minusHours(1);
        }
        keyList.add(createHourIndexKey(fromTime));
        return keyList;
    }

    private void addForecastToHourIndex(HourIndexKey key, ScoreForecast scoreForecast) {
        if (hourIndex.containsKey(key)) {
            hourIndex.get(key).add(scoreForecast);
        } else {
            List<ScoreForecast> indexList = new ArrayList<>();
            indexList.add(scoreForecast);
            hourIndex.put(key, indexList);
        }
    }

    private void addForecastToPeriodIndex(DayIndexKey key, PeriodForecast periodForecast) {
        if (dayIndex.containsKey(key)) {
            dayIndex.get(key).add(periodForecast);
        } else {
            List<PeriodForecast> indexList = new ArrayList<>();
            indexList.add(periodForecast);
            dayIndex.put(key, indexList);
        }
    }

    private HourIndexKey createHourIndexKey(ZonedDateTime pointInTime) {
        return new HourIndexKey(pointInTime);
    }

}