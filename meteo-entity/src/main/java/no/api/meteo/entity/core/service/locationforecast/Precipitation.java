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

import net.sf.oval.constraint.NotNull;

public final class Precipitation extends UnitEntity {

    @NotNull
    private Double minValue;

    @NotNull
    private Double value;

    @NotNull
    private Double probability;

    @NotNull
    private Double maxValue;

    public Precipitation() {
    }

    public Precipitation(String id, String unit, Double minValue, Double value,
                         Double probability, Double maxValue) {
        setId(id);
        setUnit(unit);
        setMinValue(minValue);
        setValue(value);
        setMaxValue(maxValue);
        setProbability(probability);
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }
}
