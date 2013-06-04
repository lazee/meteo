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

package no.api.meteo.services.internal;

import org.joda.time.DateTime;

public class HourIndexKey {

    public static final int HASH_NUMBER = 31;

    private DateTime dateTime;

    private int day;

    private int month;

    private int year;

    private int hour;

    public HourIndexKey(DateTime dateTime) {
        this.dateTime = dateTime;
        this.day = dateTime.getDayOfMonth();
        this.month = dateTime.getMonthOfYear();
        this.year = dateTime.getYear();
        this.hour = dateTime.getHourOfDay();
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HourIndexKey that = (HourIndexKey) o;

        if (day != that.day) {
            return false;
        }
        if (hour != that.hour) {
            return false;
        }
        if (month != that.month) {
            return false;
        }
        if (year != that.year) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = day;
        result = HASH_NUMBER * result + month;
        result = HASH_NUMBER * result + year;
        result = HASH_NUMBER * result + hour;
        return result;
    }

    @Override
    public String toString() {
        return "PeriodIndexKey{" +
                "day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", hour=" + hour +
                '}';
    }
}