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
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.time.ZonedDateTime;

@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class PeriodForecast extends AbstractForecast implements Forecast {

    @JsonProperty
    private final Precipitation precipitation;

    @JsonProperty
    private final Symbol symbol;

    @JsonProperty
    private final SymbolProbability symbolProbability;

    @JsonCreator
    public PeriodForecast(
            @JsonProperty("fromTime") ZonedDateTime fromTime,
            @JsonProperty("toTime") ZonedDateTime toTime,
            @JsonProperty("precipitation") Precipitation precipitation,
            @JsonProperty("symbol") Symbol symbol,
            @JsonProperty("symbolProbability") SymbolProbability symbolProbability) {
        super(fromTime, toTime);
        this.precipitation = precipitation;
        this.symbol = symbol;
        this.symbolProbability = symbolProbability;
    }
}
