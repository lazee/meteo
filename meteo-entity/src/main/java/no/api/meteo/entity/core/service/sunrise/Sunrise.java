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

package no.api.meteo.entity.core.service.sunrise;

import no.api.meteo.entity.core.Location;
import no.api.meteo.entity.core.RootEntity;

import java.util.ArrayList;
import java.util.List;

public class Sunrise extends RootEntity {

   private Location location;

   private List<SunriseDate> dates = new ArrayList<SunriseDate>();

    public List<SunriseDate> getDates() {
        return dates;
    }

    public void setDates(List<SunriseDate> dates) {
        if (dates != null) {
            this.dates = dates;
        }
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
