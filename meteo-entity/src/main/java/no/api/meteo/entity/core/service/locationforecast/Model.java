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

package no.api.meteo.entity.core.service.locationforecast;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.Date;

@Value
public final class Model {

    @JsonProperty
    private final Date to;

    @JsonProperty
    private final Date from;

    @JsonProperty
    private final Date runEnded;

    @JsonProperty
    private final Date nextRun;

    @JsonProperty
    private final Date termin;

    @JsonProperty
    private final String name;

    @JsonCreator
    public Model(@JsonProperty("to") Date to,
                 @JsonProperty("from") Date from,
                 @JsonProperty("runEnded") Date runEnded,
                 @JsonProperty("nextRun") Date nextRun,
                 @JsonProperty("termin") Date termin,
                 @JsonProperty("name") String name) {
        this.to = to;
        this.from = from;
        this.runEnded = runEnded;
        this.nextRun = nextRun;
        this.termin = termin;
        this.name = name;
    }
}
