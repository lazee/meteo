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

package no.api.meteo.services.internal;

import no.api.meteo.service.locationforecast.entity.Forecast;
import no.api.meteo.service.locationforecast.entity.PeriodForecast;
import no.api.meteo.service.locationforecast.entity.PointForecast;
import org.joda.time.DateTime;
import org.joda.time.Hours;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeteoForecastHourIndexer {

    private List<Forecast> forecasts;

    private Map<PeriodIndexKey, List<ScoreForecast>> periodIndex;

    public MeteoForecastHourIndexer(List<Forecast> forecasts) {
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
                if (createIndexKeyFromDate(dateTime)
                        .equals(createIndexKeyFromDate(new DateTime(pointForecast.getFromTime())))) {
                    return pointForecast;
                }
            }
        }
        return null;
    }

    public List<PeriodForecast> getMatchingPeriodForecasts(DateTime from) {
        List<PeriodForecast> periodForecasts = new ArrayList<PeriodForecast>();
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
        return periodIndex.get(createIndexKeyFromDate(new DateTime(from)));
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

    protected ScoreForecast getWidestFitScoreForecast(DateTime from) {
        return getXScoreForecast(from, true);
    }

    private ScoreForecast getXScoreForecast(DateTime from, boolean widest) {
        if (from == null) {
            return null;
        }
        PeriodIndexKey periodIndexKey = createIndexKeyFromDate(new DateTime(from));
        List<ScoreForecast> scoreForecasts = periodIndex.get(periodIndexKey);
        if (scoreForecasts == null) {
            return null;
        }
        ScoreForecast lowest = null;
        for (ScoreForecast scoreForecast : scoreForecasts) {
            if (lowest == null) {
                lowest = scoreForecast;
            } else {
                int lowestScore = (widest ? lowest.getWideScore() : lowest.getTightScore());
                int forecastScore = (widest ? scoreForecast.getWideScore() : scoreForecast.getTightScore());
                if (forecastScore < lowestScore) {
                    lowest = scoreForecast;
                }
            }
        }
        return lowest;
    }

    private void init() {
        periodIndex = new HashMap<PeriodIndexKey, List<ScoreForecast>>();
        for (Forecast forecast : forecasts) {
            if (forecast instanceof PeriodForecast) {
                indexPeriodForecast((PeriodForecast) forecast);
            }
        }
    }

    private void indexPeriodForecast(PeriodForecast periodForecast) {
        List<PeriodIndexKey> indexKeys = createIndexKeysFromPeriodForecast(periodForecast);
        for (PeriodIndexKey periodIndexKey : indexKeys) {
            ScoreForecast scoreForecast =
                    new ScoreForecast(periodForecast, calculateScore(periodForecast, periodIndexKey, 1),
                            calculateScore(periodForecast, periodIndexKey, -1));
            addForecastToIndex(periodIndexKey, scoreForecast);
        }
    }

    private int calculateScore(PeriodForecast periodForecast, PeriodIndexKey periodIndexKey, int spanWeight) {
        DateTime periodFromTime = new DateTime(periodForecast.getFromTime());
        return (Hours.hoursBetween(periodFromTime, new DateTime(periodForecast.getToTime())).getHours() *
                spanWeight) +
                Hours.hoursBetween(periodFromTime, periodIndexKey.getDateTime()).getHours();
    }

    private List<PeriodIndexKey> createIndexKeysFromPeriodForecast(PeriodForecast periodForecast) {
        List<PeriodIndexKey> keyList = new ArrayList<PeriodIndexKey>();
        DateTime fromTime = new DateTime(periodForecast.getFromTime());
        DateTime toTime = new DateTime(periodForecast.getToTime());

        DateTime activeTime = toTime;
        while (activeTime.isAfter(fromTime)) {
            keyList.add(createIndexKeyFromDate(activeTime));
            activeTime = activeTime.minusHours(1);
        }

        keyList.add(createIndexKeyFromDate(fromTime));
        return keyList;
    }

    private void addForecastToIndex(PeriodIndexKey key, ScoreForecast scoreForecast) {
        if (periodIndex.containsKey(key)) {
            periodIndex.get(key).add(scoreForecast);
        } else {
            List<ScoreForecast> indexList = new ArrayList<ScoreForecast>();
            indexList.add(scoreForecast);
            periodIndex.put(key, indexList);
        }
    }

    private PeriodIndexKey createIndexKeyFromDate(DateTime pointInTime) {
        return new PeriodIndexKey(pointInTime);
    }


}