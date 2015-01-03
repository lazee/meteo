/*
 * Copyright (c) 2011-2013 Amedia AS.
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

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.util.Date;


@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class PointForecast extends AbstractForecast implements Forecast {

    private final Fog fog;

    private final Pressure pressure;

    private final HighClouds highClouds;

    private final MediumClouds mediumClouds;

    private final Cloudiness cloudiness;

    private final LowClouds lowClouds;

    private final WindDirection windDirection;

    private final WindSpeed windSpeed;

    private final Humidity humidity;

    private final Temperature temperature;

    private final WindProbability windProbability;

    private final TemperatureProbability temperatureProbability;

    public PointForecast(Date fromTime, Date toTime, Fog fog, Pressure pressure, HighClouds highClouds,
                         MediumClouds mediumClouds, Cloudiness cloudiness, LowClouds lowClouds,
                         WindDirection windDirection, WindSpeed windSpeed, Humidity humidity, Temperature temperature,
                         WindProbability windProbability, TemperatureProbability temperatureProbability) {
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

    public PointForecast(Date fromTime, Date toTime) {
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
