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

import no.api.meteo.service.locationforecast.entity.PeriodForecast;

public class ScoreForecast {

    private PeriodForecast periodForecast;

    private int tightScore;

    private int wideScore;

    public ScoreForecast(PeriodForecast periodForecast, int tightScore, int wideScore) {
        this.periodForecast = periodForecast;
        this.tightScore = tightScore;
        this.wideScore = wideScore;
    }

    public PeriodForecast getPeriodForecast() {
        return periodForecast;
    }

    public int getTightScore() {
        return tightScore;
    }

    public int getWideScore() {
        return wideScore;
    }
}