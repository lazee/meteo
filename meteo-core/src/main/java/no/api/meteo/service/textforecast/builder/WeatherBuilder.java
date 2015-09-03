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

package no.api.meteo.service.textforecast.builder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.api.meteo.MetaBuilder;
import no.api.meteo.entity.core.service.textforecast.Time;
import no.api.meteo.entity.core.service.textforecast.Weather;
import no.api.meteo.util.MetaEntityBuilder;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class WeatherBuilder implements MetaEntityBuilder<Weather> {

    @Getter
    private MetaBuilder metaBuilder = new MetaBuilder();

    @Setter
    @Getter
    private String productDescription;

    @Setter
    @Getter
    private String title;

    @Setter
    @Getter
    private List<Time> times = new ArrayList<>();

    @Override
    public Weather build() {
        return new Weather(getTitle(), getMetaBuilder().build(), getProductDescription(), getTimes());
    }

    @Override
    public void setCreated(ZonedDateTime timestamp) {
        // Required by the interface, but not present for textforecasts.
    }
}
