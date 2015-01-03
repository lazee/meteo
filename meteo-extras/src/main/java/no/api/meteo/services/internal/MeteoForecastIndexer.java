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

package no.api.meteo.services.internal;

import no.api.meteo.entity.core.service.locationforecast.Forecast;
import no.api.meteo.entity.core.service.locationforecast.PeriodForecast;
import no.api.meteo.entity.core.service.locationforecast.PointForecast;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.joda.time.Hours.hoursBetween;

public class MeteoForecastIndexer {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private List<Forecast> forecasts;

    private Map<HourIndexKey, List<ScoreForecast>> hourIndex;

    private Map<DayIndexKey, List<PeriodForecast>> dayIndex;

    public MeteoForecastIndexer(List<Forecast> forecasts) {
        this.forecasts = forecasts;
        init();
    }

    /**
     * Get the PointForecast that matches the given date.
     *
     * @param dateTime Date object used to find a matching PointForecast.
     * @return A matching PointForecast if found, else <code>null</code>.
     */
    public PointForecast getPointForecast(DateTime dateTime) {
        for (Forecast forecast : forecasts) {
            if (forecast instanceof PointForecast) {
                PointForecast pointForecast = (PointForecast) forecast;
                if (createHourIndexKey(dateTime)
                        .equals(createHourIndexKey(new DateTime(pointForecast.getFromTime())))) {
                    return pointForecast;
                }
            }
        }
        return null;
    }

    public List<PeriodForecast> getMatchingPeriodForecasts(DateTime from) {
        List<PeriodForecast> periodForecasts = new ArrayList<>();
        List<ScoreForecast> scoreForecasts = getMatchingScoreForecasts(from);
        if (scoreForecasts == null) {
            return periodForecasts;
        }
        for (ScoreForecast scoreForecast : scoreForecasts) {
            periodForecasts.add(scoreForecast.getPeriodForecast());
        }
        return periodForecasts;
    }

    protected List<ScoreForecast> getMatchingScoreForecasts(DateTime from) {
        return hourIndex.get(createHourIndexKey(new DateTime(from)));
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
    public PeriodForecast getTightestFitPeriodForecast(DateTime from) {
        ScoreForecast scoreForecast = getTightestFitScoreForecast(from);
        if (scoreForecast == null) {
            return null;
        }
        return scoreForecast.getPeriodForecast();
    }

    public boolean hasForecastsForDay(DateTime from) {
        return dayIndex.containsKey(new DayIndexKey(from));
    }

    protected ScoreForecast getTightestFitScoreForecast(DateTime from) {
        return getXScoreForecast(from, false);
    }

    /*
    * Find the period forecast that is the widest fit for a point forecast.
    *
    * <p>We use the exact same kind of logic to find the widest fit as we do for the tightest fit. The only change is
    * that we invert the span tightScore. Meaning that a large span will tightScore lower than a small span</p>
    *
    */
    public PeriodForecast getWidestFitPeriodForecast(DateTime from) {
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
    public PeriodForecast getBestFitPeriodForecast(DateTime from, DateTime to) {
        if (from == null || to == null) {
            return null;
        }

        // Making sure that we remove minutes, seconds and milliseconds from the request timestamps
        DateTime requestFrom = from.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);
        DateTime requestTo = to.withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0);

        //  Get list of period forecasts for the requested day. Return null if date isn't present
        List<PeriodForecast> forecastsList = dayIndex.get(new DayIndexKey(requestFrom));
        if (forecastsList == null) {
            return null;
        }

        PeriodForecast chosenForecast = null;
        int score = 0;
        int tmpScore = 0;

        for (PeriodForecast forecast : forecastsList) {
            DateTime actualFrom = new DateTime(forecast.getFromTime());
            DateTime actualTo = new DateTime(forecast.getToTime());

            if (requestFrom.equals(actualFrom) && requestTo.equals(actualTo)) {
                return forecast;
            } else if (
                    (requestFrom.isBefore(actualFrom) && requestTo.isBefore(actualFrom)) ||
                            (requestFrom.isAfter(actualTo) && requestTo.isAfter(actualTo)) ||
                            actualTo.isEqual(actualFrom)) {
                continue; // this period is outside the requested period or this is a point forecast.
            } else if (requestFrom.isBefore(actualFrom) && requestTo.isBefore(actualTo)) {
                tmpScore = between(requestTo, actualFrom);
            } else if ((actualFrom.isBefore(requestFrom) || actualFrom.isEqual(requestFrom)) &&
                    actualTo.isBefore(requestTo)) {
                tmpScore = between(actualTo, requestFrom);
            } else if (actualFrom.isAfter(requestFrom) &&
                    (actualTo.isBefore(requestTo) || actualTo.isEqual(requestTo))) {
                tmpScore = between(actualTo, actualFrom);
            } else if (actualFrom.isBefore(requestFrom) && actualTo.isAfter(requestTo)) {
                tmpScore = between(requestTo, requestFrom);
            } else {
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd:HH:mm");
                log.warn("Unhandled forecast Requested period:" + requestFrom.toString(fmt) + "--" +
                        requestTo.toString(fmt) + ", Actual period: " +
                        actualFrom.toString(fmt) + "--" + actualTo.toString(fmt));
            }

            tmpScore = Math.abs(tmpScore);

            if ((score == 0 && tmpScore > 0) || tmpScore > score) {
                score = tmpScore;
                chosenForecast = forecast;
            }
        }

        return chosenForecast;
    }

    protected ScoreForecast getWidestFitScoreForecast(DateTime time) {
        return getXScoreForecast(time, true);
    }

    private int between(DateTime t1, DateTime t2) {
        return hoursBetween(t1, t2).getHours();
    }

    private ScoreForecast getXScoreForecast(DateTime time, boolean widest) {
        if (time == null) {
            return null;
        }
        HourIndexKey hourIndexKey = createHourIndexKey(new DateTime(time));
        List<ScoreForecast> scoreForecasts = hourIndex.get(hourIndexKey);
        if (scoreForecasts == null) {
            return null;
        }
        ScoreForecast lowest = null;
        for (ScoreForecast scoreForecast : scoreForecasts) {
            if (lowest == null) {
                lowest = scoreForecast;
            } else {
                int lowestScore = (widest ? lowest.getPointWideScore() : lowest.getPointTightScore());
                int forecastScore = (widest ? scoreForecast.getPointWideScore() : scoreForecast.getPointTightScore());
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
                DayIndexKey dayIndexKey = new DayIndexKey(new DateTime(periodForecast.getFromTime()));
                addForecastToPeriodIndex(dayIndexKey, periodForecast);
            }
        }
    }

    private int calculatePointScore(PeriodForecast periodForecast, HourIndexKey hourIndexKey, int spanWeight) {
        DateTime periodFromTime = new DateTime(periodForecast.getFromTime());
        DateTime periodToTime = new DateTime(periodForecast.getToTime());
        return (hoursBetween(periodFromTime, periodToTime).getHours() * spanWeight) +
                hoursBetween(periodFromTime, hourIndexKey.getDateTime()).getHours();
    }

    private List<HourIndexKey> createIndexKeysFromPeriodForecast(PeriodForecast periodForecast) {
        List<HourIndexKey> keyList = new ArrayList<>();
        DateTime fromTime = new DateTime(periodForecast.getFromTime());
        DateTime activeTime = new DateTime(periodForecast.getToTime());
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

    private HourIndexKey createHourIndexKey(DateTime pointInTime) {
        return new HourIndexKey(pointInTime);
    }


}