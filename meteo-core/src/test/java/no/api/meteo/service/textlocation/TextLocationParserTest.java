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

package no.api.meteo.service.textlocation;

import no.api.meteo.client.MeteoData;
import no.api.meteo.client.MeteoTestClient;
import no.api.meteo.entity.core.service.textlocation.TextLocationTime;
import no.api.meteo.entity.core.service.textlocation.TextLocationWeather;
import no.api.meteo.test.MeteoTestUtils;
import org.junit.Assert;
import org.junit.Test;

public class TextLocationParserTest {

    @Test
    public void testFetchContent() throws Exception {
        TextLocationService service =
                new TextLocationService(
                        new MeteoTestClient(
                                MeteoTestUtils.getTextResource("/META-INF/meteo/textlocation/textlocation1.xml")));
        MeteoData<TextLocationWeather> data = service.fetchContent(1, 1); // Really doesn't matter what we pick here since we are using a mock.
        Assert.assertNotNull(service);
        Assert.assertNotNull(data);
        TextLocationWeather result = data.getResult();
        Assert.assertNotNull(result);
        Assert.assertNotNull(data.getResponse().getData());
        Assert.assertNotNull(data.getResponse().getResponseHeaders());
        Assert.assertEquals(1, data.getResponse().getResponseHeaders().size());
        Assert.assertEquals("Forecasts for 59.9,10.6", result.getProductDescription());

        Assert.assertEquals(7, result.getTextLocationTimes().size());

        TextLocationTime textLocationTime = result.getTextLocationTimes().get(2);
        Assert.assertEquals("2015-08-24T22:00Z", textLocationTime.getFrom().toString());
        Assert.assertEquals("2015-08-25T22:00Z", textLocationTime.getTo().toString());
        Assert.assertEquals("Østlig senere sørlig bris, på kysten til dels frisk bris. Regn, stedvis mye nedbør vest for Oslo. Lokal torden. Nord for Mjøsa oppholdsvær først på dagen.", textLocationTime.getTextLocation().getForecast());

    }
}
