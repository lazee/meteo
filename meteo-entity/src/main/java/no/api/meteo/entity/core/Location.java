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

package no.api.meteo.entity.core;

import net.sf.oval.constraint.NotNegative;
import net.sf.oval.constraint.NotNull;

public class Location {

    public static final int HASH_CODE = 31;

    @NotNull
    @NotNegative
    private Double longitude;

    @NotNull
    @NotNegative
    private Double latitude;

    @NotNegative
    private Double altitude;

    public Location(Double longitude, Double latitude, Double altitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Location location = (Location) o;

        if (altitude != null ? !altitude.equals(location.altitude) : location.altitude != null) {
            return false;
        }
        if (latitude != null ? !latitude.equals(location.latitude) : location.latitude != null) {
            return false;
        }
        if (longitude != null ? !longitude.equals(location.longitude) : location.longitude != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = longitude != null ? longitude.hashCode() : 0;
        result = HASH_CODE * result + (latitude != null ? latitude.hashCode() : 0);
        result = HASH_CODE * result + (altitude != null ? altitude.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Location{" +
                "altitude=" + altitude +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
