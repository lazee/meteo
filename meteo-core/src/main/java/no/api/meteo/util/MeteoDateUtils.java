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

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Util class for dealing with different date issues in Meteo.
 */
public final class MeteoDateUtils {

    private static final String YYYYMMDD = "yyyy-MM-dd";

    private static final String HHMM = "HH:mm";

    private MeteoDateUtils() {
        throw new UnsupportedOperationException();
    }

    public static ZonedDateTime fullOffsetFormatToZonedDateTime(String dateStr) throws MeteoException {
        if (dateStr == null) {
            return null;
        }
        String d = dateStr;
        // Fix for invalid zone ids in the MET api
        if (d.charAt(19) == '+') {
            d = dateStr.substring(0, 22) + ":" + dateStr.substring(22);
        }
        try {
            return OffsetDateTime.parse(d).atZoneSameInstant(ZoneId.of("Z"));
        } catch (DateTimeParseException e) {
            throw new MeteoException(e);
        }
    }
    public static ZonedDateTime fullFormatToZonedDateTime(String dateStr) throws MeteoException {
        if (dateStr == null) {
            return null;
        }
        try {
            return ZonedDateTime.parse(dateStr);
        } catch (DateTimeParseException e) {
            throw new MeteoException(e);
        }
    }

    public static LocalDate yyyyMMddToLocalDate(String dateStr) {
        if (dateStr == null) {
            return null;
        }
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(YYYYMMDD));
    }

    public static String localDateToString(LocalDate localDate, String pattern) {
        if (localDate == null || pattern == null) {
            return null;
        }
        return localDate.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String zonedDateTimeToYyyyMMdd(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String zonedDateTimeToHHMM(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return null;
        }
        return zonedDateTime.format(DateTimeFormatter.ofPattern(HHMM));
    }

    public static ZonedDateTime cloneZonedDateTime(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return null;
        }
        return zonedDateTime.withZoneSameInstant(ZoneId.of("Z"));
    }

    public static ZonedDateTime toZeroMSN(ZonedDateTime dateTime) {
        return dateTime.withMinute(0).withSecond(0).withNano(0);
    }

    public static ZonedDateTime toZeroHMSN(ZonedDateTime dateTime) {
        return dateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

}
