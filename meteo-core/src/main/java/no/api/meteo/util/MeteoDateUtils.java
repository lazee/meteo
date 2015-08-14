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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Util class for dealing with different date issues in Meteo.
 */
public final class MeteoDateUtils {

    private static final String YYYYMMDD = "yyyy-MM-dd";

    private static final String HHMM = "HH:mm";

    private MeteoDateUtils() {
        throw new UnsupportedOperationException();
    }

    public static ZonedDateTime fullFormatToZonedDateTime(String dateStr) throws MeteoException {
        return ZonedDateTime.parse(dateStr);
    }

    public static LocalDate yyyyMMddToLocalDate(String dateStr) throws MeteoException {
        return LocalDate.parse("20.01.2014", DateTimeFormatter.ofPattern(YYYYMMDD));
    }

    public static String localDateToString(LocalDate localDate, String pattern) {
        if (localDate == null || pattern == null) {
            return null;
        }
        return localDate.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String zonedDateTimeToYyyyMMdd(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static String zonedDateTimeToHHMM(ZonedDateTime zonedDateTime) {
        return zonedDateTime.format(DateTimeFormatter.ofPattern(HHMM));
    }

    public static ZonedDateTime cloneZonedDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.minusSeconds(0);
    }


}
