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
public final class PointForecast extends AbstractForecast implements Forecast {

    @JsonProperty
    private final Fog fog;

    @JsonProperty
    private final Pressure pressure;

    @JsonProperty
    private final HighClouds highClouds;

    @JsonProperty
    private final MediumClouds mediumClouds;

    @JsonProperty
    private final Cloudiness cloudiness;

    @JsonProperty
    private final LowClouds lowClouds;

    @JsonProperty
    private final WindDirection windDirection;

    @JsonProperty
    private final WindSpeed windSpeed;

    @JsonProperty
    private final Humidity humidity;

    @JsonProperty
    private final Temperature temperature;

    @JsonProperty
    private final WindProbability windProbability;

    @JsonProperty
    private final TemperatureProbability temperatureProbability;

    @JsonCreator
    public PointForecast(@JsonProperty("fromTime") ZonedDateTime fromTime,
                         @JsonProperty("toTime") ZonedDateTime toTime,
                         @JsonProperty("fog") Fog fog,
                         @JsonProperty("pressure") Pressure pressure,
                         @JsonProperty("highClouds") HighClouds highClouds,
                         @JsonProperty("mediumClouds") MediumClouds mediumClouds,
                         @JsonProperty("cloudiness") Cloudiness cloudiness,
                         @JsonProperty("lowClouds") LowClouds lowClouds,
                         @JsonProperty("windDirection") WindDirection windDirection,
                         @JsonProperty("windSpeed") WindSpeed windSpeed,
                         @JsonProperty("humidity") Humidity humidity,
                         @JsonProperty("temperature") Temperature temperature,
                         @JsonProperty("windProbability") WindProbability windProbability,
                         @JsonProperty("temperatureProbability") TemperatureProbability temperatureProbability) {
        super(fromTime, toTime);
        this.fog = fog;
        this.pressure = pressure;
        this.highClouds = highClouds;
        this.mediumClouds = mediumClouds;
        this.cloudiness = cloudiness;
        this.lowClouds = lowClouds;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.temperature = temperature;
        this.windProbability = windProbability;
        this.temperatureProbability = temperatureProbability;
    }

    public PointForecast(ZonedDateTime fromTime, ZonedDateTime toTime) {
        super(fromTime, toTime);
        this.fog = null;
        this.pressure = null;
        this.highClouds = null;
        this.mediumClouds = null;
        this.cloudiness = null;
        this.lowClouds = null;
        this.windDirection = null;
        this.windSpeed = null;
        this.humidity = null;
        this.temperature = null;
        this.windProbability = null;
        this.temperatureProbability = null;
    }

}
