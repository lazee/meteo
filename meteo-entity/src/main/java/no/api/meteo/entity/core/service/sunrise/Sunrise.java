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
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import no.api.meteo.entity.core.Location;
import no.api.meteo.entity.core.Meta;
import no.api.meteo.entity.core.RootEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Sunrise extends RootEntity {

    @JsonProperty
    private final Location location;

    @JsonProperty
    private final List<SunriseDate> dates;

    @JsonCreator
    public Sunrise(@JsonProperty("created") Date created,
                   @JsonProperty("meta") Meta meta,
                   @JsonProperty("location") Location location,
                   @JsonProperty("dates") List<SunriseDate> dates) {
        super(created, meta);
        this.location = location;
        this.dates = dates;
    }

    public List<SunriseDate> getDates() {
        return dates == null ? new ArrayList<SunriseDate>() : Collections.unmodifiableList(dates);
    }
}
