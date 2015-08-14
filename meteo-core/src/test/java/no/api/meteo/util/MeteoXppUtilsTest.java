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

import org.junit.Assert;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParser;

import java.time.LocalDate;
import java.time.ZonedDateTime;

public final class MeteoXppUtilsTest {

    private final String testXml = "<test><entry string=\"foo\" integer=\"7\" double=\"1.2\"" +
            " boolean=\"true\" date=\"2011-05-06T05:09:00Z\" simple=\"2011-02-03\"/></test>";

    @Test
    public void testCreateNewPullParser() throws Exception {
        XmlPullParser xpp = MeteoXppUtils.createPullParser(testXml);
        Assert.assertNotNull(xpp);
    }

    @Test
    public void testGetAttributeValue() throws Exception {
        XmlPullParser xpp = MeteoXppUtils.createPullParser(testXml);
        int eventType = xpp.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (xpp.getName().equals("entry")) {
                    // String
                    Assert.assertEquals("foo", MeteoXppUtils.getString(xpp, "string"));
                    Assert.assertNull(MeteoXppUtils.getString(xpp, "foo"));
                    // Integer
                    Assert.assertEquals(new Integer(7), MeteoXppUtils.getInteger(xpp, "integer"));
                    Assert.assertNull(MeteoXppUtils.getInteger(xpp, "string"));
                    Assert.assertNull(MeteoXppUtils.getInteger(xpp, "foo"));
                    // Double
                    Assert.assertEquals(1.2, MeteoXppUtils.getDouble(xpp, "double"), 0.0);
                    Assert.assertNull(MeteoXppUtils.getDouble(xpp, "string"));
                    Assert.assertNull(MeteoXppUtils.getDouble(xpp, "foo"));
                    // Boolean
                    Assert.assertEquals(Boolean.TRUE, MeteoXppUtils.getBoolean(xpp, "boolean"));
                    Assert.assertFalse(MeteoXppUtils.getBoolean(xpp, "foo"));
                    // Simple date
                    Assert.assertNull(MeteoXppUtils.getLocalDate(xpp, "foo"));
                    LocalDate simpleDate = MeteoXppUtils.getLocalDate(xpp, "simple");
                    Assert.assertEquals(2011, simpleDate.getYear());
                    Assert.assertEquals(3, simpleDate.getDayOfMonth());
                    Assert.assertEquals(2, simpleDate.getMonthValue());
                    // Full date 2011-05-06T05:00:00Z
                    Assert.assertNull(MeteoXppUtils.getZoneDateTime(xpp, "foo"));
                    ZonedDateTime dateTime = MeteoXppUtils.getZoneDateTime(xpp, "date");
                    Assert.assertEquals(5, dateTime.getHour());
                }
            }
            eventType = xpp.next();
        }
    }

}
