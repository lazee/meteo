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
import no.api.meteo.entity.extras.MeteoExtrasLongTermForecast;
import no.api.meteo.service.locationforecast.LocationforcastLTSParser;
import no.api.meteo.test.MeteoTestException;
import no.api.meteo.test.MeteoTestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.ZoneId;

public class LocationForecastHelperTest {

    private LongtermForecastHelper longtermForecastHelper;

    private final ZoneId zoneId = ZoneId.of("Z");

    @Before
    public void loadResources() throws MeteoTestException, MeteoException {
        String resource = MeteoTestUtils.getTextResource("/META-INF/meteo/locationsforecastlts/test2.xml");
        LocationforcastLTSParser parser = new LocationforcastLTSParser();
        final LocationForecast locationForecast = parser.parse(resource);
        //final LocationForecastHelper helper = new LocationForecastHelper(locationForecast);
        longtermForecastHelper = new LongtermForecastHelper(locationForecast);
    }


   /* @Test
    public void testCreateForcastForDay() throws Exception {
        MeteoExtrasForecastDay forcastForDay =
                longtermForecastHelper.createForcastForDay(fullFormatToZonedDateTime("2015-09-03T04:00:00Z"));
        Assert.assertNotNull(forcastForDay.getForecasts());
        Assert.assertEquals(4, forcastForDay.getForecasts().size());

    }*/

    @Test
    public void testCreateLongTermForecast() {
        MeteoExtrasLongTermForecast longTermForecast = longtermForecastHelper.createLongTermForecast();
        Assert.assertEquals(90, longTermForecast.getForecastDays().size());
    }
}