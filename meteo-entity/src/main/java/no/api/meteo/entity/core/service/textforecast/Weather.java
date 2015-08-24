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
    private String title;

    @JsonProperty
    private Meta meta;

    @JsonProperty
    private String productionDescripton;

    @JsonProperty
    private List<Time> times;

    @JsonCreator
    public Weather(@JsonProperty("title") String title,
                   @JsonProperty("meta") Meta meta,
                   @JsonProperty("productionDescription") String productionDescripton,
                   @JsonProperty("times") List<Time> times) {
        this.title = title;
        this.meta = meta;
        this.productionDescripton = productionDescripton;
        this.times = times;
    }
}
