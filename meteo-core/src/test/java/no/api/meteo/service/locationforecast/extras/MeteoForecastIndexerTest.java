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

import no.api.meteo.MeteoException;
import no.api.meteo.entity.core.service.locationforecast.LocationForecast;
import no.api.meteo.entity.core.service.locationforecast.PeriodForecast;
import no.api.meteo.entity.core.service.locationforecast.PointForecast;
import no.api.meteo.service.locationforecast.LocationforcastLTSParser;
import no.api.meteo.test.MeteoTestException;
import no.api.meteo.test.MeteoTestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static no.api.meteo.util.MeteoDateUtils.cloneZonedDateTime;
import static no.api.meteo.util.MeteoDateUtils.fullFormatToZonedDateTime;

public class MeteoForecastIndexerTest {

    private LocationForecast locationForecast;

    private MeteoForecastIndexer indexer;

    private final ZoneId zoneId = ZoneId.of("Z");

    @Before
    public void loadResources() throws MeteoTestException, MeteoException {
        String resource = MeteoTestUtils.getTextResource("/META-INF/meteo/locationsforecastlts/test1.xml");
        LocationforcastLTSParser parser = new LocationforcastLTSParser();
        locationForecast = parser.parse(resource);
        indexer = new MeteoForecastIndexer(locationForecast.getForecasts());
    }

    /*@Test
    public void testGetMatchingPeriodForecasts() throws Exception {
        Assert.assertTrue(locationForecast.getForecasts().get(10) instanceof PointForecast);
        PointForecast pointForecast = (PointForecast) locationForecast.getForecasts().get(10);
        List<PeriodForecast> periodForecastList = indexer.getMatchingPeriodForecasts(
                cloneZonedDateTime(pointForecast.getFrom()));
        Assert.assertEquals(16, periodForecastList.size());

        ZonedDateTime nonTime = ZonedDateTime.now(zoneId).withYear(1960);
        PointForecastBuilder builder = PointForecastBuilder.fromPointForecast(pointForecast);
        builder.setFrom(nonTime);
        builder.setTo(nonTime);
        pointForecast = builder.build();
        Assert.assertEquals(0,
                            indexer.getMatchingPeriodForecasts(cloneZonedDateTime(pointForecast.getFrom())).size());
    }*/

    @Test
    public void testGetBestFitPeriodForecast1() throws Exception {
        Optional<PeriodForecast> p = indexer.getBestFitPeriodForecast(
                fullFormatToZonedDateTime("2015-01-07T04:00:00Z"),
                fullFormatToZonedDateTime("2015-01-07T10:00:00Z"));
        Assert.assertTrue(p.isPresent());
        Assert.assertEquals(new Integer(15), p.get().getSymbol().getNumber());
        Assert.assertEquals("Fog", p.get().getSymbol().getId());
        ZonedDateTime dt = cloneZonedDateTime(p.get().getFrom());
        Assert.assertEquals(4, dt.getHour());
        ZonedDateTime tt = cloneZonedDateTime(p.get().getTo());
        Assert.assertEquals(10, tt.getHour());
    }

    @Test
    public void testGetBestFitPeriodForecast2() throws Exception {
        Optional<PeriodForecast> p = indexer.getBestFitPeriodForecast(
                fullFormatToZonedDateTime("2015-01-07T03:00:00Z"),
                fullFormatToZonedDateTime("2015-01-07T11:00:00Z"));
        Assert.assertTrue(p.isPresent());
        Assert.assertEquals(new Integer(15), p.get().getSymbol().getNumber());
        Assert.assertEquals("Fog", p.get().getSymbol().getId());
        ZonedDateTime dt = cloneZonedDateTime(p.get().getFrom());
        Assert.assertEquals(3, dt.getHour());
        ZonedDateTime tt = cloneZonedDateTime(p.get().getTo());
        Assert.assertEquals(9, tt.getHour()); // TODO Really 9????
    }

    @Test
    public void testGetBestFitPeriodForecast3() throws Exception {
        Optional<PeriodForecast> p = indexer.getBestFitPeriodForecast(
                fullFormatToZonedDateTime("2015-01-07T01:00:00Z"),
                fullFormatToZonedDateTime("2015-01-07T07:00:00Z"));
        Assert.assertTrue(p.isPresent());
        Assert.assertEquals(new Integer(44), p.get().getSymbol().getNumber());
        Assert.assertEquals("LightSnowSun", p.get().getSymbol().getId());
        ZonedDateTime dt = cloneZonedDateTime(p.get().getFrom());
        Assert.assertEquals(1, dt.getHour());
        ZonedDateTime tt = cloneZonedDateTime(p.get().getTo());
        Assert.assertEquals(7, tt.getHour());

    }

    /*@Test
    public void testGetTightestFitPeriodForecast() throws Exception {
        Assert.assertFalse(indexer.getTightestFitScoreForecast(null).isPresent());
        Assert.assertFalse(indexer.getTightestFitPeriodForecast(null).isPresent());
        PointForecast pointForecast = (PointForecast) locationForecast.getForecasts().get(10);
        Assert.assertNotNull(pointForecast);
        Optional<ScoreForecast> matchingScoreForecast =
                indexer.getTightestFitScoreForecast(cloneZonedDateTime(pointForecast.getFrom()));
        Assert.assertTrue(matchingScoreForecast.isPresent());
        Assert.assertEquals(1, matchingScoreForecast.get().getPointTightScore());

        Optional<PeriodForecast> periodForecast = indexer.getTightestFitPeriodForecast(cloneZonedDateTime(
                pointForecast.getFrom()));
        Assert.assertEquals(periodForecast.get().getFrom(),
                            matchingScoreForecast.get().getPeriodForecast().getFrom());
    }*/

    @Test
    public void testGetWidestFitPeriodForecast() throws Exception {
        Assert.assertFalse(indexer.getWidestFitScoreForecast(null).isPresent());
        Assert.assertFalse(indexer.getWidestFitPeriodForecast(null).isPresent());
        PointForecast pointForecast = (PointForecast) locationForecast.getForecasts().get(10);
        Assert.assertNotNull(pointForecast);
        Optional<ScoreForecast> matchingScoreForecast = indexer.getWidestFitScoreForecast(cloneZonedDateTime(
                pointForecast.getFrom()));
        Assert.assertTrue(matchingScoreForecast.isPresent());
        Optional<PeriodForecast> periodForecast = indexer.getWidestFitPeriodForecast(cloneZonedDateTime(
                pointForecast.getFrom()));
        Assert.assertTrue(periodForecast.isPresent());
        Assert.assertNotNull(periodForecast.get().getFrom());
        Assert.assertNotNull(periodForecast.get().getTo());
        ZonedDateTime ft = cloneZonedDateTime(periodForecast.get().getFrom());
        ZonedDateTime tt = cloneZonedDateTime(periodForecast.get().getTo());
        // TODO Add real tests here . Remember to convert into GMT to avoid tests from failing when summer time is
        // over in Norway
    }

    @Test
    public void testPeriodIndexKey() throws Exception {
        HourMatcher p1 = new HourMatcher(ZonedDateTime.now(zoneId).withYear(1970));
        HourMatcher p2 = new HourMatcher(ZonedDateTime.now(zoneId).withYear(1980));
        Assert.assertFalse(p1.equals(p2));

        p1 = new HourMatcher(ZonedDateTime.now(zoneId).withMonth(2));
        p2 = new HourMatcher(ZonedDateTime.now(zoneId).withMonth(3));
        Assert.assertFalse(p1.equals(p2));

        p1 = new HourMatcher(ZonedDateTime.now(zoneId).withDayOfMonth(1));
        p2 = new HourMatcher(ZonedDateTime.now(zoneId).withDayOfMonth(2));
        Assert.assertFalse(p1.equals(p2));

        p1 = new HourMatcher(ZonedDateTime.now(zoneId).withHour(3));
        p2 = new HourMatcher(ZonedDateTime.now(zoneId).withHour(5));
        Assert.assertFalse(p1.equals(p2));
    }

    @Test
    public void testGetPointForecast() throws Exception {
        ZonedDateTime testTime = ZonedDateTime.now(zoneId).withYear(2015).withMonth(1).withDayOfMonth(7).withHour(16);
        Optional<PointForecast> pointForecast = indexer.getPointForecast(testTime);
        Assert.assertTrue(pointForecast.isPresent());
        Assert.assertEquals(100.0, pointForecast.get().getHighClouds().getPercent(), 0.);

        testTime = ZonedDateTime.now(zoneId).withYear(2001).withMonth(5).withDayOfMonth(6).withHour(16);
        pointForecast = indexer.getPointForecast(testTime);
        Assert.assertFalse(pointForecast.isPresent());
    }
}
