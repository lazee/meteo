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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
@EqualsAndHashCode
public class TextLocation {

    @JsonProperty
    private final String header;

    @JsonProperty
    private final String forecast;

    @JsonProperty
    private final String name;

    @JsonPropertyOrder
    private final String id;

    @JsonProperty
    private final String type;

    @JsonProperty
    private final String issuer;

    // From XSD : https://api.met.no/weatherapi/textlocation/1.0/schema
    // But isn't in use in the actual feed.
    //@JsonProperty
    //private String exname;

    // From XSD : https://api.met.no/weatherapi/textlocation/1.0/schema
    // But isn't in use in the actual feed.
    //@JsonProperty
    //private URI url;

    @JsonCreator
    public TextLocation(@JsonProperty("type") String type,
                        @JsonProperty("header") String header,
                        @JsonProperty("name") String name,
                        @JsonProperty("id") String id,
                        @JsonProperty("issuer") String issuer,
                        @JsonProperty("forecast") String forecast) {
        this.type = type;
        this.header = header;
        this.name = name;
        this.id = id;
        this.issuer = issuer;
        this.forecast = forecast;
    }
}

