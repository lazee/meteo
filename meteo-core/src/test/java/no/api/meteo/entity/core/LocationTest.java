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

package no.api.meteo.entity.core;

import org.junit.Assert;
import org.junit.Test;

public class LocationTest {

    @Test
    public void testFromCoordinates() throws Exception {
        Location location = Location.fromCoordinates("12.2,34.8,120");
        Assert.assertEquals(12.2, location.getLongitude(), 0.0);
        Assert.assertEquals(34.8, location.getLatitude(), 0.0);
        Assert.assertEquals(new Integer(120), location.getAltitude());
    }

    @Test
    public void testFromCoordinates2() throws Exception {
        Location location = Location.fromCoordinates("12.2,34.8");
        Assert.assertEquals(12.2, location.getLongitude(), 0.0);
        Assert.assertEquals(34.8, location.getLatitude(), 0.0);
        Assert.assertEquals(new Integer(0), location.getAltitude());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalCoordinates() throws Exception {
        Location.fromCoordinates("12.A,34.8,120");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalCoordinates2() throws Exception {
        Location.fromCoordinates("12.2 ,34.8,120");

    }
}