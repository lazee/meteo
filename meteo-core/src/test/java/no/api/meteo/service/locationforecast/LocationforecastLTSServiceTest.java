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

package no.api.meteo.service.locationforecast;

import no.api.meteo.MeteoException;
import no.api.meteo.client.MeteoClient;
import no.api.meteo.client.MeteoClientException;
import no.api.meteo.client.MeteoData;
import no.api.meteo.client.MeteoTestClient;
import no.api.meteo.entity.core.service.locationforecast.Forecast;
import no.api.meteo.entity.core.service.locationforecast.LocationForecast;
import no.api.meteo.entity.extras.MeteoExtrasForecast;
import no.api.meteo.entity.extras.MeteoExtrasForecastDay;
import no.api.meteo.entity.extras.MeteoExtrasLongTermForecast;
import no.api.meteo.service.locationforecast.extras.LocationForecastHelper;
import no.api.meteo.service.locationforecast.extras.LongtermForecastHelper;
import no.api.meteo.test.MeteoTestException;
import no.api.meteo.test.MeteoTestUtils;
import no.api.meteo.util.MeteoNetUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

import static no.api.meteo.util.MeteoDateUtils.fullFormatToZonedDateTime;

public final class LocationforecastLTSServiceTest {

    @Test(expected = MeteoClientException.class)
    public void testException() throws Exception {
        MeteoClient client = new MeteoTestClient(null);
        client.fetchContent(null);
    }

    @Test(expected = MeteoClientException.class)
    public void testException2() throws Exception {
        MeteoClient client = new MeteoTestClient(null);
        client.fetchContent(MeteoNetUtils.createUri("http://www.fo.no"));
    }

    @Test
    public void testFetchContent() throws Exception {
        LocationforecastLTSService service = new LocationforecastLTSService(new MeteoTestClient(MeteoTestUtils
                .getTextResource("/META-INF/meteo/locationsforecastlts/test1.xml")));
        MeteoData<LocationForecast> data = service.fetchContent(1, 1, 1);
        Assert.assertNotNull(service);
        Assert.assertNotNull(data);
        Assert.assertNotNull(data.getResult());
        Assert.assertNotNull(data.getResponse().getData());
        Assert.assertNotNull(data.getResponse().getResponseHeaders());
        Assert.assertEquals(1, data.getResponse().getResponseHeaders().size());
        Assert.assertEquals("foo", data.getResponse().getResponseHeaders().get(0).getName());
        Assert.assertEquals("bar", data.getResponse().getResponseHeaders().get(0).getValue());
    }
    
    @Test
    public void testLongterm() throws MeteoTestException, MeteoException {
        LocationforecastLTSService service = new LocationforecastLTSService(
                new MeteoTestClient(
                        MeteoTestUtils.getTextResource("/META-INF/meteo/locationsforecastlts/test3.xml")));
        MeteoData<LocationForecast> data = service.fetchContent(1, 1, 1);
        LocationForecast result = data.getResult();
        Assert.assertEquals(367, result.getForecasts().size());
        int i = 0;
        for (Forecast f : result.getForecasts()) {
            if (f.getFrom().toString().contains("2015-09-13T16")) {
                i++;
            }
        }
        Assert.assertEquals(5, i);
        LocationForecastHelper helper = new LocationForecastHelper(result);
        Optional<MeteoExtrasForecast> forecastForPeriod =
                helper.getForecastForPeriod(fullFormatToZonedDateTime("2015-09-13T16:00:00Z"),
                                            fullFormatToZonedDateTime("2015-09-13T22:00:00Z"));
        Assert.assertTrue(forecastForPeriod.isPresent());

        LongtermForecastHelper longtermForecastHelper = new LongtermForecastHelper(result);

        MeteoExtrasLongTermForecast longTermForecast = longtermForecastHelper.createLongTermForecast();
        MeteoExtrasForecastDay meteoExtrasForecastDay = longTermForecast.getForecastDays().get(1);
        Assert.assertEquals("2015-09-13", meteoExtrasForecastDay.getDay().toString());
        Assert.assertEquals(4, meteoExtrasForecastDay.getForecasts().size());
    }
}
