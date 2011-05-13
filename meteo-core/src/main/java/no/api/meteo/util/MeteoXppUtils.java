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

package no.api.meteo.util;

import no.api.meteo.MeteoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.StringReader;
import java.util.Date;

/**
 * Util class for simplifying different XPP tasks.
 */
public final class MeteoXppUtils {

    private static Logger log = LoggerFactory.getLogger(MeteoXppUtils.class);

    private MeteoXppUtils() {
        // Intentional
    }

    public static XmlPullParser createNewPullParser(String data) throws MeteoException {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(data));
            return xpp;
        } catch (XmlPullParserException e) {
            throw new MeteoException("Could not create XmlPullParser instance.", e);
        }
    }

    public static XmlPullParser createNewPullParser(InputStream inputStream) throws MeteoException {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(inputStream, "UTF-8");
            return xpp;
        } catch (XmlPullParserException e) {
            throw new MeteoException("Could not create XmlPullParser instance.", e);
        }
    }


    public static String getAttributeValue(XmlPullParser xpp, String name) {
        return xpp.getAttributeValue(null, name);
    }

    public static Double getDoubleAttributeValue(XmlPullParser xpp, String name) {
        try {
            String v = getAttributeValue(xpp, name);
            if (v == null) {
                return null;
            }
            return Double.parseDouble(v);
        } catch (NumberFormatException e) {
            log.error("Number format exception: " + e.getMessage());
            return null;
        }
    }

    public static Boolean getBooleanAttributeValue(XmlPullParser xpp, String name) {
        String v = getAttributeValue(xpp, name);
        if (v == null) {
            return false;
        }
        return Boolean.parseBoolean(v);
    }

    public static Integer getIntegerAttributeValue(XmlPullParser xpp, String name) {
        try {
            String v = getAttributeValue(xpp, name);
            if (v == null) {
                return null;
            }
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            log.error("Number format exception: " + e.getMessage());
            return null;
        }
    }

    public static Date getDateAttributeValue(XmlPullParser xpp, String name) throws MeteoException {
        return MeteoDateUtils.fullFormatToDate(getAttributeValue(xpp, name));
    }

    public static Date getSimpleDateAttributeValue(XmlPullParser xpp, String name) throws MeteoException {
        return MeteoDateUtils.yyyyMMddToDate(getAttributeValue(xpp, name));
    }

}
