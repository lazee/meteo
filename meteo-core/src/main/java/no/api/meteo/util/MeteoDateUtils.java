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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Util class for dealing with different date issues in Meteo.
 */
public final class MeteoDateUtils {

    private static final String YYYYMMDD = "yyyy-MM-dd";

    private static final String HHMM = "HH:mm";

    private static final String FULLFORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private MeteoDateUtils() {
        // Intentional
    }

    public static Date fullFormatToDate(String dateStr) throws MeteoException {
        return stringToDate(new SimpleDateFormat(FULLFORMAT), dateStr);
    }

    public static Date yyyyMMddToDate(String dateStr) throws MeteoException {
        return stringToDate(new SimpleDateFormat(YYYYMMDD), dateStr);
    }

    private static Date stringToDate(DateFormat format, String dateStr) throws MeteoException {
        if (dateStr == null) {
            return null;
        }
        format.setTimeZone(TimeZone.getTimeZone("Z"));
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            throw new MeteoException("Could not parse the date : " + dateStr, e);
        }
    }

    public static String dateToString(Date date, String pattern) {
        if (date == null || pattern == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static String dateToYyyyMMdd(Date date) {
        if (date == null) {
            return null;
        }
        return (new SimpleDateFormat(YYYYMMDD)).format(date);
    }

    public static String dateToHHmm(Date date) {
        if (date == null) {
            return null;
        }
        return (new SimpleDateFormat(HHMM)).format(date);
    }
}
