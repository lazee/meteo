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

package no.api.meteo.service.textforecast;

import no.api.meteo.client.MeteoData;
import no.api.meteo.client.MeteoTestClient;
import no.api.meteo.entity.core.service.textforecast.Area;
import no.api.meteo.entity.core.service.textforecast.Time;
import no.api.meteo.entity.core.service.textforecast.Weather;
import no.api.meteo.entity.core.service.textforecast.query.ForecastQuery;
import no.api.meteo.test.MeteoTestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TextforecastParserTest {

    @Test
    public void testFetchContent() throws Exception {
        TextforecastService service =
                new TextforecastService(
                        new MeteoTestClient(
                                MeteoTestUtils.getTextResource("/META-INF/meteo/textforecast/land.xml")));
        MeteoData<Weather> data = service.fetchContent(
                ForecastQuery.AW_ENGM_EN); // Really doesn't matter what we pick here since we are using a mock.
        Assert.assertNotNull(service);
        Assert.assertNotNull(data);
        Weather result = data.getResult();
        Assert.assertNotNull(result);
        Assert.assertNotNull(data.getResponse().getData());
        Assert.assertNotNull(data.getResponse().getResponseHeaders());
        Assert.assertEquals(1, data.getResponse().getResponseHeaders().size());
        Assert.assertEquals("merged", result.getProductionDescripton());
        Assert.assertEquals("Tekstvarsel for Norge", result.getTitle());

        List<Time> times = result.getTimes();
        Assert.assertEquals(4, times.size());
        List<Area> areas = times.get(0).getForecastType().getAreas();

        Assert.assertEquals(20, areas.size());
        Area area = areas.get(2);
        Assert.assertEquals("Troms", area.getName());
        Assert.assertEquals("county", area.getType());
        Assert.assertEquals("Skiftende bris. Skyet, i nord spredt regn, men etter hvert oppholdsvær. Lokal tåke. " +
                                    "Fra i ettermiddag nordaustlig bris. Til dels pent vær.",
                            area.getLocation().getIn());

        area = times.get(1).getForecastType().getAreas().get(3);
        Assert.assertEquals("Akershus", area.getName());
        Assert.assertEquals("county", area.getType());
        Assert.assertEquals("Østlig bris. På kysten sørøstlig periodevis liten kuling, om kveld sørvestlig opp i " +
                                    "stiv kuling. Regn, senere regnbyger, først i sør. Lokalt mye nedbør.",
                            area.getLocation().getIn());
    }

}