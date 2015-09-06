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
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import no.api.meteo.entity.core.Meta;

import java.util.List;

@Value
@ToString
@EqualsAndHashCode
public class Weather {

    @JsonProperty
    private final String title;

    @JsonProperty
    private final Meta meta;

    @JsonProperty
    private final String productDescription;

    @JsonProperty
    private final List<Time> times;

    @JsonCreator
    public Weather(@JsonProperty("title") String title,
                   @JsonProperty("meta") Meta meta,
                   @JsonProperty("productDescription") String productDescription,
                   @JsonProperty("times") List<Time> times) {
        this.title = title;
        this.meta = meta;
        this.productDescription = productDescription;
        this.times = times;
    }
}
