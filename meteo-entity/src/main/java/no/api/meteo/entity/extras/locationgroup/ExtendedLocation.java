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

package no.api.meteo.entity.extras.locationgroup;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import no.api.meteo.entity.core.Location;

@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ExtendedLocation extends Location {

    private final String name;

    public ExtendedLocation(Double longitude, Double latitude, Double altitude, String name) {
        super(longitude, latitude, altitude);
        this.name = name;
    }
}
