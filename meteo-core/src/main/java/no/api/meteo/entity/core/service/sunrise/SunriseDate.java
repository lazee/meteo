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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Value;
import no.api.meteo.util.jackson.LocalDateDeserializer;
import no.api.meteo.util.jackson.LocalDateSerializer;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Value
public final class SunriseDate {

    @JsonProperty
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
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
        return isPolarNight() || !isSun(time);
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
        if (getSun().getNeverRise()) {
            return false;
        } else if (getSun().getNeverSet()) {
            return true;
        }
        return timeWithinPeriod(currentDate);
    }

    private boolean timeWithinPeriod(ZonedDateTime currentDate) {
        return currentDate.equals(getSun().getRise()) ||
                currentDate.equals(getSun().getSet()) ||
                (
                        currentDate.isAfter(getSun().getRise()) && currentDate.isBefore(getSun().getSet())
                );
    }


}
