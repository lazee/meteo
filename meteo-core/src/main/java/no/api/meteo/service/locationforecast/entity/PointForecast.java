/*
 * Copyright (c) 2011 A-pressen Digitale Medier
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

package no.api.meteo.service.locationforecast.entity;

public class PointForecast extends AbstractForecast implements Forecast {

    private Fog fog;

    private Pressure pressure;

    private HighClouds highClouds;

    private MediumClouds mediumClouds;

    private Cloudiness cloudiness;

    private LowClouds lowClouds;

    private WindDirection windDirection;

    private WindSpeed windSpeed;

    private Humidity humidity;

    private Temperature temperature;

    private WindProbability windProbability;

    private TemperatureProbability temperatureProbability;

    public WindProbability getWindProbability() {
        return windProbability;
    }

    public void setWindProbability(WindProbability windProbability) {
        this.windProbability = windProbability;
    }

    public TemperatureProbability getTemperatureProbability() {
        return temperatureProbability;
    }

    public void setTemperatureProbability(TemperatureProbability temperatureProbability) {
        this.temperatureProbability = temperatureProbability;
    }

    public Fog getFog() {
        return fog;
    }

    public void setFog(Fog fog) {
        this.fog = fog;
    }

    public Pressure getPressure() {
        return pressure;
    }

    public void setPressure(Pressure pressure) {
        this.pressure = pressure;
    }

    public HighClouds getHighClouds() {
        return highClouds;
    }

    public void setHighClouds(HighClouds highClouds) {
        this.highClouds = highClouds;
    }

    public MediumClouds getMediumClouds() {
        return mediumClouds;
    }

    public void setMediumClouds(MediumClouds mediumClouds) {
        this.mediumClouds = mediumClouds;
    }

    public Cloudiness getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(Cloudiness cloudiness) {
        this.cloudiness = cloudiness;
    }

    public LowClouds getLowClouds() {
        return lowClouds;
    }

    public void setLowClouds(LowClouds lowClouds) {
        this.lowClouds = lowClouds;
    }

    public WindDirection getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(WindDirection windDirection) {
        this.windDirection = windDirection;
    }

    public WindSpeed getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(WindSpeed windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Humidity getHumidity() {
        return humidity;
    }

    public void setHumidity(Humidity humidity) {
        this.humidity = humidity;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }
}
