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

package no.api.meteo.entity.core.service.sunrise;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static no.api.meteo.util.MeteoDateUtils.fullFormatToZonedDateTime;

public class SunriseDateTest {

    @Test
    public void testIsSun1() throws Exception {
        //<sun rise="2015-08-05T03:01:23Z" set="2015-08-05T19:43:21Z">
        //<noon altitude="46.1816138680073" />
        //</sun>
        ZonedDateTime rise = fullFormatToZonedDateTime("2015-08-05T03:01:23Z");
        ZonedDateTime set = fullFormatToZonedDateTime("2015-08-05T19:43:21Z");

        Sun sun = new Sun(rise, set, false, false, null, null, null, null);
        SunriseDate sunrise = new SunriseDate(LocalDate.of(2015, 8, 5), sun, null);
        ZonedDateTime time = fullFormatToZonedDateTime("2015-08-05T21:05:09Z");

        Assert.assertFalse(sunrise.isSun(time));

        time = fullFormatToZonedDateTime("2015-08-05T07:05:09Z");
        Assert.assertTrue(sunrise.isSun(time));

    }


    @Test
    public void testIsSun() throws Exception {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Z"));
        ZonedDateTime before = now.minusHours(1);
        ZonedDateTime after = now.plusHours(1);

        Sun sun = new Sun(before, after, false, true, null, null, null, null);
        SunriseDate sunrise = new SunriseDate(LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth()), sun, null);
        Assert.assertTrue(sunrise.isSun(now));

        sun = new Sun(before, after, true, true, null, null, null, null);
        sunrise = new SunriseDate(LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth()), sun, null);
        Assert.assertFalse(sunrise.isSun(now));

        sun = new Sun(before, after, false, false, null, null, null, null);
        sunrise = new SunriseDate(LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth()), sun, null);
        Assert.assertTrue(sunrise.isSun(now));

    }

    @Test
    public void testSummerInNorth() throws Exception {
        /*
        http://api.met.no/weatherapi/sunrise/1.0/?lat=70.66;lon=23.68;date=2016-06-13

        <astrodata xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="http://api.met.no/weatherapi/sunrise/1.0/schema">
            <meta licenseurl="http://api.met.no/license_data.html" />
            <time date="2016-06-13">
                <location latitude="70.66" longitude="23.68">
                    <sun never_set="true" />
                    <moon phase="First quarter" rise="2016-06-13T11:27:40Z" set="2016-06-12T23:11:09Z" />
                </location>
            </time>
        </astrodata>
         */
        Sun sun = new Sun(null, null, false, true, null, null, null, null);
        SunriseDate sunriseDate = new SunriseDate(LocalDate.of(2016, 6, 13), sun, null);
        Assert.assertFalse(sunriseDate.isNight(ZonedDateTime.of(2016, 6, 13, 19, 0, 0, 0, ZoneId.of("Z"))));
        Assert.assertFalse(sunriseDate.isNight(ZonedDateTime.of(2016, 6, 13, 13, 0, 0, 0, ZoneId.of("Z"))));
        Assert.assertFalse(sunriseDate.isNight(ZonedDateTime.of(2016, 6, 13, 10, 0, 0, 0, ZoneId.of("Z"))));
        Assert.assertFalse(sunriseDate.isNight(ZonedDateTime.of(2016, 6, 13, 23, 0, 0, 0, ZoneId.of("Z"))));
    }

}