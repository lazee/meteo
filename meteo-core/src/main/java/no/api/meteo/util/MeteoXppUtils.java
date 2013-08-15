/*
 * Copyright (c) 2011-2013 Amedia AS.
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
        return  v == null ? false : Boolean.parseBoolean(v);
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

    public static Date getDate(XmlPullParser xpp, String name) throws MeteoException {
        return MeteoDateUtils.fullFormatToDate(getString(xpp, name));
    }

    public static Date getSimpleDate(XmlPullParser xpp, String name) throws MeteoException {
        return MeteoDateUtils.yyyyMMddToDate(getString(xpp, name));
    }

}
