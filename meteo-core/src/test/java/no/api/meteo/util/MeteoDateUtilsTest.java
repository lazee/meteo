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
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public final class MeteoDateUtilsTest {

    private static TimeZone systemTimezone;

    @BeforeClass
    public static void before() {
        systemTimezone = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+2"));
    }

    @AfterClass
    public static void after() {
        TimeZone.setDefault(systemTimezone);
    }

    @Test(expected = MeteoException.class)
    public void testBadDateString() throws Exception {
        MeteoDateUtils.fullFormatToZonedDateTime("ddd");
    }

    @Test
    public void testLocalDateToYyyyMMdd() throws Exception {
        LocalDate firstDate = LocalDate.of(2010, 5, 17);
        Assert.assertEquals("20100517", MeteoDateUtils.zonedDateTimeToYyyyMMdd(firstDate));

    }

    @Test
    public void testFullFormatToDate() throws Exception {
        Assert.assertNull(MeteoDateUtils.fullFormatToZonedDateTime(null));
        ZonedDateTime dateTime = MeteoDateUtils.fullFormatToZonedDateTime("2011-05-10T03:00:00Z");
        Assert.assertNotNull(dateTime);
        Assert.assertEquals("2011-05-10T03:00Z", dateTime.toString());
    }

    @Test
    public void testYyyyMMddToDate() throws Exception {
        LocalDate localDate = MeteoDateUtils.yyyyMMddToLocalDate("2011-05-10");
        Assert.assertNotNull(localDate);
        Assert.assertEquals("2011-05-10", localDate.toString());

    }

    @Test
    public void testDateToString() throws Exception {
        Assert.assertNull(MeteoDateUtils.localDateToString(null, null));
        LocalDate dt = LocalDate.of(1977, 3, 21);
        Assert.assertEquals("0321", MeteoDateUtils.localDateToString(dt, "MMdd"));

    }

    @Test
    public void testDateToYyyyMMdd() throws Exception {
        Assert.assertNull(MeteoDateUtils.zonedDateTimeToYyyyMMdd(null));
        LocalDate dt = LocalDate.of(1977, 3, 21);
        Assert.assertEquals("19770321", MeteoDateUtils.zonedDateTimeToYyyyMMdd(dt));

    }

    @Test
    public void testDateToHHmm() throws Exception {
        Assert.assertNull(MeteoDateUtils.zonedDateTimeToHHMM(null));
        ZonedDateTime dateTime = MeteoDateUtils.fullFormatToZonedDateTime("2015-08-14T20:01:00Z");
        Assert.assertEquals("20:01", MeteoDateUtils.zonedDateTimeToHHMM(dateTime));
    }
}
