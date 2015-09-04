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

package no.api.meteo.entity.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNegative;
import net.sf.oval.constraint.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class Location {

    @JsonProperty
    private final String name;

    @NotNull
    @NotNegative
    @JsonProperty
    private final Double longitude;

    @NotNull
    @NotNegative
    @JsonProperty
    private final Double latitude;

    @NotNegative
    @JsonProperty
    private final Integer altitude;

    @JsonCreator
    public Location(@JsonProperty("longitude") Double longitude,
                    @JsonProperty("latitude") Double latitude,
                    @JsonProperty("altitude") Integer altitude,
                    @JsonProperty("name") String name) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.name = name;
    }
}
