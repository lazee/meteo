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

package no.api.meteo.entity.core.service.textforecast;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
@EqualsAndHashCode
public class Location {

    @JsonProperty
    private String type;

    @JsonProperty
    private String name;

    @JsonPropertyOrder
    private String id;

    @JsonProperty
    private String header;

    @JsonProperty
    private String in;

    @JsonCreator
    public Location(@JsonProperty("type") String type,
                    @JsonProperty("name") String name,
                    @JsonProperty("id") String id,
                    @JsonProperty("header") String header,
                    @JsonProperty("in") String in) {
        this.type = type;
        this.name = name;
        this.id = id;
        this.header = header;
        this.in = in;
    }
}
