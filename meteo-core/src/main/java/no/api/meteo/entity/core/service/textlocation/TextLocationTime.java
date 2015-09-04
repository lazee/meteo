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

package no.api.meteo.entity.core.service.textlocation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import no.api.meteo.util.jackson.ZonedDateTimeDeserializer;
import no.api.meteo.util.jackson.ZonedDateTimeSerializer;

import java.time.ZonedDateTime;

@Value
@ToString
@EqualsAndHashCode
public class TextLocationTime {

    @JsonProperty
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private final ZonedDateTime from;

    @JsonProperty
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private final ZonedDateTime to;

    @JsonProperty
    private final TextLocation textLocation;

    @JsonCreator
    public TextLocationTime(@JsonProperty("from") ZonedDateTime from,
                            @JsonProperty("to") ZonedDateTime to,
                            @JsonProperty("location") TextLocation textLocation) {
        this.from = from;
        this.to = to;
        this.textLocation = textLocation;
    }
}
