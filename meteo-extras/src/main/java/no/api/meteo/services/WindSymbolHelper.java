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

package no.api.meteo.services;

import no.api.meteo.entity.core.service.locationforecast.PointForecast;
import no.api.meteo.entity.extras.BeaufortLevel;

import java.text.DecimalFormat;

/**
 * Class that helps getting extra information about wind speed.
 */
public final class WindSymbolHelper {

    private static DecimalFormat idFormat = new DecimalFormat("00");

    private WindSymbolHelper() {
        // Intentional
    }

    /**
     * Create name for a wind symbol.
     *
     * This will be created as a mix of the Beaufort wind speed id and the wind direction. This name can be used to
     * identify what wind icon that should be used for a forecast.
     *
     * @param pointForecast The point forecast to create a wind symbol name for.
     * @return A wind symbol name. <code>null</code> if wind information was missing from the given point forecast.
     */
    public static String createWindSymbolName(PointForecast pointForecast) {
        if (pointForecast == null || pointForecast.getWindDirection() == null || pointForecast.getWindSpeed() == null) {
            return null;
        }
        return pointForecast.getWindDirection().getName().toLowerCase() +
                idFormat.format(pointForecast.getWindSpeed().getBeaufort());
    }

    /**
     * Find matching Beaufort level for a given point forecast.
     *
     * This provides you with extra information about the wind speed specified in the forecast.
     *
     * @param pointForecast Point forecast containing information about the wind speed.
     * @return Matching Beaufort level object. <code>null</code> if wind speed information is missing in the given
     *         forecast.
     */
    public static BeaufortLevel findBeaufortLevel(PointForecast pointForecast) {
        if (pointForecast == null || pointForecast.getWindSpeed() == null) {
            return null;
        }
        return BeaufortLevel.findUnitById(pointForecast.getWindSpeed().getBeaufort());
    }

}
