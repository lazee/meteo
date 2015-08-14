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

import java.time.ZonedDateTime;

@Value
public final class Noon {

    @JsonProperty
    private final ZonedDateTime time;

    @JsonProperty
    private final Double altitude;

    @JsonProperty
    private final Double direction;

    @JsonCreator
    public Noon(@JsonProperty("time") ZonedDateTime time,
                @JsonProperty("altitude") Double altitude,
                @JsonProperty("direction") Double direction) {
        this.time = time;
        this.altitude = altitude;
        this.direction = direction;
    }
}
