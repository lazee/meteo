/*
 * Copyright (c) 2011-2013 Amedia AS.
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

import no.api.meteo.client.MeteoClient;
import no.api.meteo.client.MeteoClientException;
import no.api.meteo.client.MeteoData;
import no.api.meteo.client.MeteoTestClient;
import no.api.meteo.entity.core.service.locationforecast.LocationForecast;
import no.api.meteo.test.MeteoTestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

public class LocationforecastLTSServiceTest {

    @Test(expected = MeteoClientException.class)
    public void testException() throws Exception {
        MeteoClient client = new MeteoTestClient(null);
        client.fetchContent(null);
    }

    @Test(expected = MeteoClientException.class)
    public void testException2() throws Exception {
        MeteoClient client = new MeteoTestClient(null);
        client.fetchContent(new URL("http://www.fo.no"));
    }

    @Test
    public void testFetchContent() throws Exception {
        LocationforecastLTSService service = new LocationforecastLTSService(new MeteoTestClient(MeteoTestUtils
                .getTextResource("/META-INF/meteo/locationsforecastlts/test1.xml")));
        MeteoData<LocationForecast> data = service.fetchContent(1, 1, 1);
        Assert.assertNotNull(service);
        Assert.assertNotNull(data);
        Assert.assertNotNull(data.getResult());
        Assert.assertNotNull(data.getRawResult());
        Assert.assertNotNull(data.getHttpHeaders());
        Assert.assertEquals(1, data.getHttpHeaders().size());
        Assert.assertEquals("foo", data.getHttpHeaders().get(0).getName());
        Assert.assertEquals("bar", data.getHttpHeaders().get(0).getValue());
    }
}
