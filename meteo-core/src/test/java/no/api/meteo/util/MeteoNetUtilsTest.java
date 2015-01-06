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

package no.api.meteo.util;

import no.api.meteo.MeteoException;
import org.junit.Assert;
import org.junit.Test;

public class MeteoNetUtilsTest {

    @Test(expected = MeteoException.class)
    public void test_create_url_error() throws Exception {
        MeteoNetUtils.createUrl("htt://www.ap dm .no");
    }

    @Test
    public void test_create_url() throws Exception {
        Assert.assertEquals("http://www.apdm.no", MeteoNetUtils.createUrl("http://www.apdm.no").toString());
    }
}
