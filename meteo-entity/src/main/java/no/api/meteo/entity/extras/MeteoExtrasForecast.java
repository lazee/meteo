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

package no.api.meteo.entity.extras;

import lombok.Value;
import no.api.meteo.entity.core.service.locationforecast.PeriodForecast;
import no.api.meteo.entity.core.service.locationforecast.PointForecast;

/**
 * This is a special customized forecast containing a mix of information for a given period of time.
 * <p/>
 * As explained in the api.met.no faq (http://api.met.no/faq.html#times) it doesn't make sense to give symbols and
 * precipitation for a given point in time. This only make sense for a given period.
 * <p/>
 * But in the Extras api we sometimes wants to return a PointForecast with a corresponding PeriodForecast, meaning a
 * PeriodForecast for the period that the PointForecast is in. We also want to add information of whether the period is
 * at night or not. More data will be added along the way.
 */
@Value
public final class MeteoExtrasForecast {

    private final PointForecast pointForecast;

    private final PeriodForecast periodForecast;

}
