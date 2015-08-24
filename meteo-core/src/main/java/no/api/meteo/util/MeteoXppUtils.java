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

import lombok.extern.slf4j.Slf4j;
import no.api.meteo.MeteoException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Util class for simplifying different XPP tasks.
 */
@Slf4j
public final class MeteoXppUtils {

    private MeteoXppUtils() {
        throw new UnsupportedOperationException();
    }

    public static XmlPullParser createPullParser(String data) throws MeteoException {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(data));
            return xpp;
        } catch (XmlPullParserException e) {
            throw new MeteoException("Could not create XmlPullParser instance.", e);
        }
    }

    public static XmlPullParser createPullParser(InputStream inputStream) throws MeteoException {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(inputStream, "UTF-8");
            return xpp;
        } catch (XmlPullParserException e) {
            throw new MeteoException("Could not create XmlPullParser instance.", e);
        }
    }

    public static String getString(XmlPullParser xpp, String name) {
        return xpp.getAttributeValue(null, name);
    }

    public static Double getDouble(XmlPullParser xpp, String name) {
        try {
            String v = getString(xpp, name);
            return v == null ? null : Double.parseDouble(v);
        } catch (NumberFormatException e) {
            log.error("Number format exception: " + e.getMessage());
            return null;
        }
    }

    public static Boolean getBoolean(XmlPullParser xpp, String name) {
        String v = getString(xpp, name);
        return v != null && Boolean.parseBoolean(v);
    }

    public static Integer getInteger(XmlPullParser xpp, String name) {
        try {
            String v = getString(xpp, name);
            return v == null ? null : Integer.parseInt(v);
        } catch (NumberFormatException e) {
            log.error("Number format exception: " + e.getMessage());
            return null;
        }
    }

    public static ZonedDateTime getZoneDateTime(XmlPullParser xpp, String name) throws MeteoException {
        return MeteoDateUtils.fullFormatToZonedDateTime(getString(xpp, name));
    }

    public static ZonedDateTime getZoneDateTimeNorway(XmlPullParser xpp, String name) throws MeteoException {
        LocalDateTime parse =
                LocalDateTime.parse(getString(xpp, name), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return parse.atZone(ZoneId.of("Europe/Oslo"));
    }

    public static LocalDate getLocalDate(XmlPullParser xpp, String name) throws MeteoException {
        return MeteoDateUtils.yyyyMMddToLocalDate(getString(xpp, name));
    }

    public static String readText(XmlPullParser xpp) {
        String result = null;
        try {
            if (xpp.next() == XmlPullParser.TEXT) {
                result = xpp.getText();
                xpp.nextTag();
            }
        } catch (XmlPullParserException | IOException e) {
            log.error("Problem while reading tag body", e);
        }
        return result;

    }
}
