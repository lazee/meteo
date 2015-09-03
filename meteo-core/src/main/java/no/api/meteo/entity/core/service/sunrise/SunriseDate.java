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

package no.api.meteo.entity.core.service.sunrise;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Value
public final class SunriseDate {

    @JsonProperty
    private final LocalDate date;

    @JsonProperty
    private final Sun sun;

    @JsonProperty
    private final Moon moon;

    @JsonCreator
    public SunriseDate(@JsonProperty("date") LocalDate date,
                       @JsonProperty("sun") Sun sun,
                       @JsonProperty("moon") Moon moon) {
        this.date = date;
        this.sun = sun;
        this.moon = moon;
    }

    public boolean isPolarNight() {
        return getSun().getNeverRise();
    }

    public boolean isNight(ZonedDateTime time) {
        return isPolarNight() ? true : !isSun(time);
    }

    /**
     * Check if the sun is shining for a given time.
     *
     * @param currentDate
     *         The time (date) to be checked.
     *
     * @return <code>true</code> if the sun has raised, else <code>false</code>.
     */
    public boolean isSun(ZonedDateTime currentDate) {
        boolean timeWithinSunPeriod = currentDate.equals(getSun().getRise()) || currentDate.equals(getSun().getSet()) ||
                currentDate.isAfter(getSun().getRise()) && currentDate.isBefore(getSun().getSet());

        if (getSun().getNeverRise()) {
            return false;
        } else if (getSun().getNeverSet() || timeWithinSunPeriod) {
            return true;
        }
        return false;
    }


}