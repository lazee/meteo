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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MeteoParserUtils {

    private static Logger log = LoggerFactory.getLogger(MeteoParserUtils.class);

    private MeteoParserUtils() {
        // Intentional
    }

    public static Date metDatestringToDate(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            throw new MeteoException("Could not parse the date : " + dateStr);
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

    public static Date getDateAttributeValue(XmlPullParser xpp, String name) {
        return metDatestringToDate(getAttributeValue(xpp, name));
    }
}
