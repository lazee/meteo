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
import no.api.meteo.entity.core.Location;
import no.api.meteo.entity.core.service.locationforecast.Forecast;
import no.api.meteo.entity.core.service.locationforecast.LocationForecast;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class LocationForecastBuilder {

    @Setter
    @Getter
    private ZonedDateTime created;

    @Getter
    private MetaBuilder metaBuilder = new MetaBuilder();

    @Setter
    @Getter
    private Location location;

    @Setter
    @Getter
    private List<Forecast> forecasts = new ArrayList<>();

    public LocationForecast build() {
        return new LocationForecast(getCreated(), getMetaBuilder().build(), getLocation(), getForecasts());
    }
}