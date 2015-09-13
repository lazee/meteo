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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import net.sf.oval.constraint.NotNegative;
import net.sf.oval.constraint.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
public class Location {

    private static Pattern P = Pattern.compile("(\\d{1,3}\\.\\d{1,3}),(\\d{1,3}\\.\\d{1,3})(,\\d{1,4})?");

    @JsonProperty
    private final String name;

    @NotNull
    @NotNegative
    @JsonProperty
    private final Double longitude;

    @NotNull
    @NotNegative
    @JsonProperty
    private final Double latitude;

    @NotNegative
    @JsonProperty
    private final Integer altitude;

    @JsonCreator
    public Location(@JsonProperty("longitude") Double longitude,
                    @JsonProperty("latitude") Double latitude,
                    @JsonProperty("altitude") Integer altitude,
                    @JsonProperty("name") String name) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.name = name;
    }

    /**
     * Create an untitled Location from coordinate string.
     *
     * @param coordinates Coordinate string on the format longitude,latitude,altitude. Eg: 20.3,123.3,123 or 20.3,123.3
     * @return Instance of the location with the coordinates from the input string.
     */
    public static Location fromCoordinates(String coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Cannot create Location from null input.");
        }
        Matcher m = P.matcher(coordinates);
        if (!m.matches()) {
            throw new IllegalArgumentException(
                    coordinates + " must be on the pattern (longitude,latitude,altitude) : " + P.pattern());
        }

        try {
            Double longitude = Double.valueOf(m.group(1));
            Double latitude = Double.valueOf(m.group(2));
            Integer altitude = 0;
            if (m.group(3) != null) {
                altitude = Integer.valueOf(m.group(3).substring(1));
            }
            return new Location(longitude, latitude, altitude, "");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    coordinates + " must be on the pattern (longitude,latitude,altitude) : " + P.pattern());
        }

    }
}
