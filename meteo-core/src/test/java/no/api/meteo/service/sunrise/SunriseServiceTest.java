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

package no.api.meteo.service.sunrise;

import no.api.meteo.client.MeteoData;
import no.api.meteo.client.MeteoTestClient;
import no.api.meteo.entity.core.service.sunrise.Sunrise;
import no.api.meteo.entity.core.service.sunrise.SunriseDate;
import no.api.meteo.test.MeteoTestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public final class SunriseServiceTest {

    @Test
    public void testFetchContent() throws Exception {
        SunriseService service = new SunriseService(new MeteoTestClient(
                MeteoTestUtils.getTextResource("/META-INF/meteo/sunrise/sunrise1.xml")));
        MeteoData<Sunrise> sunriseMeteoData = service.fetchContent(19, 70, LocalDate.now());
        Assert.assertNotNull(sunriseMeteoData);
        Assert.assertNotNull(sunriseMeteoData.getResult());
        Assert.assertNotNull(sunriseMeteoData.getResult().getDates());
        SunriseDate sunriseDate = sunriseMeteoData.getResult().getDates().get(0);
        Assert.assertNotNull(sunriseDate);
        Assert.assertNotNull(sunriseDate.getSun());
        Assert.assertNotNull(sunriseDate.getMoon());
        Assert.assertTrue(sunriseDate.getSun().getNeverSet());
    }

}
