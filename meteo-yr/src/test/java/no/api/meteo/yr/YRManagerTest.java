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

package no.api.meteo.yr;

import no.api.meteo.MeteoRuntimeException;
import no.api.meteo.client.MeteoClient;
import no.api.meteo.client.MeteoResponse;
import no.api.meteo.test.MeteoTestException;
import no.api.meteo.test.MeteoTestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.URL;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class YRManagerTest {

    private MeteoClient meteoClient;

    @Before
    public void before() throws MeteoTestException {
        meteoClient = mock(MeteoClient.class);
        when(meteoClient.fetchContent(Mockito.<URL>any()))
                        .thenReturn(createMockResponse(TestConstants.YR_TEST_DATA_1_XML));

    }

    /* --- tests --- */
    
    @Test
    public void test_fetch_forecast() throws Exception {
        YRManager yrManager = new YRManager(meteoClient);
        YRContent yrContent = yrManager.fetchForecast("Norge/Hordaland/Bergen", YRLocale.NN);
        Assert.assertNotNull(yrContent);
        Assert.assertTrue(yrContent.toXML().contains("periodevis"));
        
    }

    @Test(expected = MeteoRuntimeException.class)
    public void test_fetch_forecast_with_invalid_place() throws Exception {
        YRManager yrManager = new YRManager(meteoClient);
        yrManager.fetchForecast("//", YRLocale.NN);
    }

    /* --- private --- */
    
    private MeteoResponse createMockResponse(String resourcePath) throws MeteoTestException {
        return new MeteoResponse(MeteoTestUtils.getTextResource(resourcePath), null);
    }
}
