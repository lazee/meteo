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

package no.api.meteo.service.locationforecast.builder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.api.meteo.entity.core.service.locationforecast.Cloudiness;
import no.api.meteo.entity.core.service.locationforecast.Fog;
import no.api.meteo.entity.core.service.locationforecast.HighClouds;
import no.api.meteo.entity.core.service.locationforecast.Humidity;
import no.api.meteo.entity.core.service.locationforecast.LowClouds;
import no.api.meteo.entity.core.service.locationforecast.MediumClouds;
import no.api.meteo.entity.core.service.locationforecast.PointForecast;
import no.api.meteo.entity.core.service.locationforecast.Pressure;
import no.api.meteo.entity.core.service.locationforecast.Temperature;
import no.api.meteo.entity.core.service.locationforecast.TemperatureProbability;
import no.api.meteo.entity.core.service.locationforecast.WindDirection;
import no.api.meteo.entity.core.service.locationforecast.WindProbability;
import no.api.meteo.entity.core.service.locationforecast.WindSpeed;
import no.api.meteo.util.EntityBuilder;

import java.util.Date;

@NoArgsConstructor
public class PointForecastBuilder implements EntityBuilder<PointForecast> {

    @Getter
    @Setter
    private Date fromTime;

    @Getter
    @Setter
    private Date toTime;

    @Getter
    @Setter
    private Fog fog;

    @Getter
    @Setter
    private Pressure pressure;

    @Getter
    @Setter
    private HighClouds highClouds;

    @Getter
    @Setter
    private MediumClouds mediumClouds;

    @Getter
    @Setter
    private Cloudiness cloudiness;

    @Getter
    @Setter
    private LowClouds lowClouds;

    @Getter
    @Setter
    private WindDirection windDirection;

    @Getter
    @Setter
    private WindSpeed windSpeed;

    @Getter
    @Setter
    private Humidity humidity;

    @Getter
    @Setter
    private Temperature temperature;

    @Getter
    @Setter
    private WindProbability windProbability;

    @Getter
    @Setter
    private TemperatureProbability temperatureProbability;

    public static PointForecastBuilder fromPointForecast(PointForecast pointForecast) {
        PointForecastBuilder builder = new PointForecastBuilder();
        builder.setFromTime(pointForecast.getFromTime());
        builder.setToTime(pointForecast.getToTime());
        builder.setFog(pointForecast.getFog());
        builder.setPressure(pointForecast.getPressure());
        builder.setHighClouds(pointForecast.getHighClouds());
        builder.setMediumClouds(pointForecast.getMediumClouds());
        builder.setCloudiness(pointForecast.getCloudiness());
        builder.setLowClouds(pointForecast.getLowClouds());
        builder.setWindDirection(pointForecast.getWindDirection());
        builder.setWindSpeed(pointForecast.getWindSpeed());
        builder.setHumidity(pointForecast.getHumidity());
        builder.setTemperature(pointForecast.getTemperature());
        builder.setWindProbability(pointForecast.getWindProbability());
        builder.setTemperatureProbability(pointForecast.getTemperatureProbability());
        return builder;
    }
    @Override
    public PointForecast build() {
        return new PointForecast(fromTime, toTime, fog, pressure, highClouds, mediumClouds, cloudiness, lowClouds,
                                 windDirection, windSpeed, humidity, temperature, windProbability,
                                 temperatureProbability);
    }
}
