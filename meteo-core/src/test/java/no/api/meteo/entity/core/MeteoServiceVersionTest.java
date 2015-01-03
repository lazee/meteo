/*
 * Copyright (c) 2011-2015 Amedia AS.
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

package no.api.meteo.entity.core;

import org.junit.Assert;
import org.junit.Test;

public class MeteoServiceVersionTest {

    @Test
    public void testToStringVersion() throws Exception {
        MeteoServiceVersion meteoServiceVersion = new MeteoServiceVersion(2, 3);
        Assert.assertEquals("2.3", meteoServiceVersion.toStringVersion());
        MeteoServiceVersion meteoServiceVersion1 = new MeteoServiceVersion(2, 3);
        Assert.assertTrue(meteoServiceVersion1.equals(meteoServiceVersion));
        Assert.assertEquals(meteoServiceVersion.hashCode(), meteoServiceVersion1.hashCode());
    }
}