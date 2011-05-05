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

package no.api.meteo.service.locationforecastlts.entity;

import net.sf.oval.constraint.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationForecast extends RootEntity {

    @NotNull
    private Location location;

    @NotNull
    private Date created;

    @NotNull
    private List<Forecast> forecasts = new ArrayList<Forecast>();

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Forecast> getForecasts() {
        return forecasts;
    }

    public List<PointForecast> getPointForecasts() {
        List<PointForecast> list = new ArrayList<PointForecast>();
        for (Forecast forecast : forecasts) {
            if (forecast instanceof PointForecast) {
                list.add((PointForecast) forecast);
            }
        }
        return list;
    }

    public List<PeriodForecast> getPeriodForecasts() {
        List<PeriodForecast> list = new ArrayList<PeriodForecast>();
        for (Forecast forecast : forecasts) {
            if (forecast instanceof PeriodForecast) {
                list.add((PeriodForecast) forecast);
            }
        }
        return list;
    }

    public void setForecasts(List<Forecast> forecasts) {
        if (forecasts != null) {
            this.forecasts = forecasts;
        }
    }
}
