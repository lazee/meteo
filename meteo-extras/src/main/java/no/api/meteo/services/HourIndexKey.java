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

package no.api.meteo.services;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.joda.time.DateTime;

@EqualsAndHashCode(of = {"day", "month", "year", "hour"}, doNotUseGetters = true)
@ToString(of = {"day", "month", "year", "hour"})
class HourIndexKey {

    @Getter
    private DateTime dateTime;

    private int day;

    private int month;

    private int year;

    private int hour;

    HourIndexKey(DateTime dateTime) {
        this.dateTime = dateTime;
        this.day = dateTime.getDayOfMonth();
        this.month = dateTime.getMonthOfYear();
        this.year = dateTime.getYear();
        this.hour = dateTime.getHourOfDay();
    }

}