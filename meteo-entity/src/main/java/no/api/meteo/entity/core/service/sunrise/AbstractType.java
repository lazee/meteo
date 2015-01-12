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

package no.api.meteo.entity.core.service.sunrise;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractType extends AbstractRiseSet {

    @Getter
    @JsonProperty
    private final Boolean neverRise;

    @Getter
    @JsonProperty
    private final Boolean neverSet;

    @JsonProperty
    private final List<ErrorType> error;

    public AbstractType(Date rise, Date set, Boolean neverRise, Boolean neverSet, List<ErrorType> error) {
        super(rise, set);
        this.neverRise = neverRise;
        this.neverSet = neverSet;
        this.error = error;
    }

    public List<ErrorType> getError() {
        return error == null ? new ArrayList<ErrorType>() : Collections.unmodifiableList(error);
    }
}
