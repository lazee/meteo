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

import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import no.api.meteo.service.entity.IdEntity;

public class WindDirection extends IdEntity {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    private Double deg;

    public WindDirection() {
    }

    public WindDirection(String id, String name, Double deg) {
        setId(id);
        setName(name);
        setDeg(deg);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDeg() {
        return deg;
    }

    public void setDeg(Double deg) {
        this.deg = deg;
    }
}
