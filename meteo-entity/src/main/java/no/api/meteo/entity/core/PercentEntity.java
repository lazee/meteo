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

package no.api.meteo.entity.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.sf.oval.constraint.NotNull;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class PercentEntity extends IdEntity {

    @NotNull
    @Getter
    private final Double percent;

    public PercentEntity(String id, Double percent) {
        super(id);
        this.percent = percent;
    }
}
