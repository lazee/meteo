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

package no.api.meteo.services.internal;

import no.api.meteo.entity.core.service.locationforecast.PeriodForecast;

public class ScoreForecast {

    private PeriodForecast periodForecast;

    private int pointTightScore;

    private int pointWideScore;

    public ScoreForecast(PeriodForecast periodForecast, int pointTightScore, int pointWideScore) {
        this.periodForecast = periodForecast;
        this.pointTightScore = pointTightScore;
        this.pointWideScore = pointWideScore;
    }

    public PeriodForecast getPeriodForecast() {
        return periodForecast;
    }

    public int getPointTightScore() {
        return pointTightScore;
    }

    public int getPointWideScore() {
        return pointWideScore;
    }
}