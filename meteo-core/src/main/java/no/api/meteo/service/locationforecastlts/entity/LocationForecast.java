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

import java.util.Date;
import java.util.List;

public class LocationForecast extends RootEntity {

    @NotNull
    private Location location;

    @NotNull
    private Date created;

    private Meta meta;

    @NotNull
    private List<PointForecast> forecasts;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public List<PointForecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<PointForecast> forecasts) {
        this.forecasts = forecasts;
    }
}
