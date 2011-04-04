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

import no.api.meteo.client.DefaultMeteoClient;
import no.api.meteo.client.MeteoClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class YrLocationExampleTest {

 private MeteoClient meteoClient;

 @Before
 public void before() {
     meteoClient = new DefaultMeteoClient();
 }

 @After
 public void after() {
     meteoClient.shutdown();
 }

 @Test
    public void test_run_example() throws Exception {
        YRManager yrManager = new YRManager(meteoClient);
        YRContent yrContent = yrManager.fetchForecast("Norway/Hordaland/Bergen/Bergen", YRLocale.NN);

     System.out.println(yrContent.toJSON());


    }
}
