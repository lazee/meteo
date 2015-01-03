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

package no.api.meteo.service.sunrise.builder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import no.api.meteo.entity.core.service.sunrise.SunriseDate;
import no.api.meteo.util.EntityBuilder;

import java.util.Date;

@NoArgsConstructor
public class SunriseDateBuilder implements EntityBuilder<SunriseDate> {

    @Setter
    @Getter
    private Date date;

    @Setter
    @Getter
    private SunBuilder sunBuilder;

    @Setter
    @Getter
    private MoonBuilder moonBuilder;

    @Override
    public SunriseDate build() {
        return new SunriseDate(date, sunBuilder.build(), moonBuilder.build());
    }
}
