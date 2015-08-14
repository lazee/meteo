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

import no.api.meteo.MeteoException;
import no.api.meteo.entity.core.service.locationforecast.LocationForecast;
import no.api.meteo.entity.core.service.locationforecast.PeriodForecast;
import no.api.meteo.entity.core.service.locationforecast.PointForecast;
import no.api.meteo.service.locationforecast.LocationforcastLTSParser;
import no.api.meteo.service.locationforecast.builder.PointForecastBuilder;
import no.api.meteo.test.MeteoTestException;
import no.api.meteo.test.MeteoTestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static no.api.meteo.util.MeteoDateUtils.cloneZonedDateTime;
import static no.api.meteo.util.MeteoDateUtils.fullFormatToZonedDateTime;

public class MeteoForecastIndexerTest {

    private LocationForecast locationForecast;

    private MeteoForecastIndexer indexer;

    private ZoneId zoneId = ZoneId.of("Z");

    @Before
    public void loadResources() throws MeteoTestException, MeteoException {
        String resource = MeteoTestUtils.getTextResource("/META-INF/meteo/locationsforecastlts/test1.xml");
        LocationforcastLTSParser parser = new LocationforcastLTSParser();
        locationForecast = parser.parse(resource);
        indexer = new MeteoForecastIndexer(locationForecast.getForecasts());
    }

    @Test
    public void testGetMatchingPeriodForecasts() throws Exception {
        Assert.assertTrue(locationForecast.getForecasts().get(10) instanceof PointForecast);
        PointForecast pointForecast = (PointForecast) locationForecast.getForecasts().get(10);
        List<PeriodForecast> periodForecastList = indexer.getMatchingPeriodForecasts(
                cloneZonedDateTime(pointForecast.getFromTime()));
        Assert.assertEquals(16, periodForecastList.size());

        ZonedDateTime nonTime = ZonedDateTime.now(zoneId).withYear(1960);
        PointForecastBuilder builder = PointForecastBuilder.fromPointForecast(pointForecast);
        builder.setFromTime(nonTime);
        builder.setToTime(nonTime);
        pointForecast = builder.build();
        Assert.assertEquals(0,
                            indexer.getMatchingPeriodForecasts(cloneZonedDateTime(pointForecast.getFromTime())).size());
    }

    @Test
    public void testGetBestFitPeriodForecast1() throws Exception {
        PeriodForecast p = indexer.getBestFitPeriodForecast(fullFormatToZonedDateTime("2015-01-07T04:00:00Z"), fullFormatToZonedDateTime(
                "2015-01-07T10:00:00Z"));
        Assert.assertNotNull(p);
        Assert.assertEquals(new Integer(15), p.getSymbol().getNumber());
        Assert.assertEquals("Fog", p.getSymbol().getId());
        ZonedDateTime dt = cloneZonedDateTime(p.getFromTime());
        Assert.assertEquals(5, dt.getHour());
        ZonedDateTime tt = cloneZonedDateTime(p.getToTime());
        Assert.assertEquals(11, tt.getHour());
    }

    @Test
    public void testGetBestFitPeriodForecast2() throws Exception {
        PeriodForecast p = indexer.getBestFitPeriodForecast(fullFormatToZonedDateTime("2015-01-07T03:00:00Z"), fullFormatToZonedDateTime(
                "2015-01-07T11:00:00Z"));
        Assert.assertNotNull(p);
        Assert.assertEquals(new Integer(15), p.getSymbol().getNumber());
        Assert.assertEquals("Fog", p.getSymbol().getId());
        ZonedDateTime dt = cloneZonedDateTime(p.getFromTime());
        Assert.assertEquals(4, dt.getHour());
        ZonedDateTime tt = cloneZonedDateTime(p.getToTime());
        Assert.assertEquals(10, tt.getHour());
    }

    @Test
    public void testGetBestFitPeriodForecast3() throws Exception {
        PeriodForecast p = indexer.getBestFitPeriodForecast(fullFormatToZonedDateTime("2015-01-07T01:00:00Z"), fullFormatToZonedDateTime("2015-01-07T07:00:00Z"));
        Assert.assertNotNull(p);
        Assert.assertEquals(new Integer(44), p.getSymbol().getNumber());
        Assert.assertEquals("LightSnowSun", p.getSymbol().getId());
        ZonedDateTime dt = cloneZonedDateTime(p.getFromTime());
        Assert.assertEquals(2, dt.getHour());
        ZonedDateTime tt = cloneZonedDateTime(p.getToTime());
        Assert.assertEquals(8, tt.getHour());

    }

    @Test
    public void testGetTightestFitPeriodForecast() throws Exception {
        Assert.assertNull(indexer.getTightestFitScoreForecast(null));
        Assert.assertNull(indexer.getTightestFitPeriodForecast(null));
        PointForecast pointForecast = (PointForecast) locationForecast.getForecasts().get(10);
        Assert.assertNotNull(pointForecast);
        ScoreForecast matchingScoreForecast =
                indexer.getTightestFitScoreForecast(cloneZonedDateTime(pointForecast.getFromTime()));
        Assert.assertNotNull(matchingScoreForecast);
        Assert.assertEquals(1, matchingScoreForecast.getPointTightScore());

        PeriodForecast periodForecast = indexer.getTightestFitPeriodForecast(cloneZonedDateTime(
                pointForecast.getFromTime()));
        Assert.assertEquals(periodForecast.getFromTime(), matchingScoreForecast.getPeriodForecast().getFromTime());
    }

    @Test
    public void testGetWidestFitPeriodForecast() throws Exception {
        Assert.assertNull(indexer.getWidestFitScoreForecast(null));
        Assert.assertNull(indexer.getWidestFitPeriodForecast(null));
        PointForecast pointForecast = (PointForecast) locationForecast.getForecasts().get(10);
        Assert.assertNotNull(pointForecast);
        ScoreForecast matchingScoreForecast = indexer.getWidestFitScoreForecast(cloneZonedDateTime(
                pointForecast.getFromTime()));
        Assert.assertNotNull(matchingScoreForecast);
        PeriodForecast periodForecast = indexer.getWidestFitPeriodForecast(cloneZonedDateTime(
                pointForecast.getFromTime()));
        Assert.assertNotNull(periodForecast);
        Assert.assertNotNull(periodForecast.getFromTime());
        Assert.assertNotNull(periodForecast.getToTime());
        ZonedDateTime ft = cloneZonedDateTime(periodForecast.getFromTime());
        ZonedDateTime tt = cloneZonedDateTime(periodForecast.getToTime());
        // TODO Add real tests here . Remember to convert into GMT to avoid tests from failing when summer time is over in Norway
    }

    @Test
    public void testPeriodIndexKey() throws Exception {
        HourIndexKey p1 = new HourIndexKey(ZonedDateTime.now(zoneId).withYear(1970));
        HourIndexKey p2 = new HourIndexKey(ZonedDateTime.now(zoneId).withYear(1980));
        Assert.assertFalse(p1.equals(null));
        Assert.assertFalse(p2.equals(null));
        Assert.assertTrue(p1.equals(p1));
        Assert.assertFalse(p1.equals(p2));

        p1 = new HourIndexKey(ZonedDateTime.now(zoneId).withMonth(2));
        p2 = new HourIndexKey(ZonedDateTime.now(zoneId).withMonth(3));
        Assert.assertFalse(p1.equals(null));
        Assert.assertFalse(p2.equals(null));
        Assert.assertTrue(p1.equals(p1));
        Assert.assertFalse(p1.equals(p2));

        p1 = new HourIndexKey(ZonedDateTime.now(zoneId).withDayOfMonth(1));
        p2 = new HourIndexKey(ZonedDateTime.now(zoneId).withDayOfMonth(2));
        Assert.assertFalse(p1.equals(null));
        Assert.assertFalse(p2.equals(null));
        Assert.assertTrue(p1.equals(p1));
        Assert.assertFalse(p1.equals(p2));

        p1 = new HourIndexKey(ZonedDateTime.now(zoneId).withHour(3));
        p2 = new HourIndexKey(ZonedDateTime.now(zoneId).withHour(5));
        Assert.assertFalse(p1.equals(null));
        Assert.assertFalse(p2.equals(null));
        Assert.assertTrue(p1.equals(p1));
        Assert.assertFalse(p1.equals(p2));
    }

    @Test
    public void testGetPointForecast() throws Exception {
        ZonedDateTime testTime = ZonedDateTime.now(zoneId).withYear(2015).withMonth(1).withDayOfMonth(7).withHour(16);
        PointForecast pointForecast = indexer.getPointForecast(testTime);
        Assert.assertNotNull(pointForecast);
        Assert.assertEquals(100.0, pointForecast.getHighClouds().getPercent(), 0.);

        testTime = ZonedDateTime.now(zoneId).withYear(2001).withMonth(5).withDayOfMonth(6).withHour(16);
        pointForecast = indexer.getPointForecast(testTime);
        Assert.assertNull(pointForecast);
    }
}
