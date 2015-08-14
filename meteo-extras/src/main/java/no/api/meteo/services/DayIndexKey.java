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

import java.time.ZonedDateTime;

@EqualsAndHashCode(of = {"day", "month", "year"}, doNotUseGetters = true)
@ToString(of = {"day", "month", "year"})
final class DayIndexKey {

    @Getter
    private final ZonedDateTime dateTime;

    private final int day;

    private final int month;

    private final int year;

    DayIndexKey(ZonedDateTime dateTime) {
        this.dateTime = dateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
        this.day = dateTime.getDayOfMonth();
        this.month = dateTime.getMonthValue();
        this.year = dateTime.getYear();
    }
}
