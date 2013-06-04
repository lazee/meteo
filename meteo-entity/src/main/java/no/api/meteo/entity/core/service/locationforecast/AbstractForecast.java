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

package no.api.meteo.entity.core.service.locationforecast;

import net.sf.oval.constraint.NotNull;

import java.util.Date;

public class AbstractForecast {

    @NotNull
    private Date fromTime;

    @NotNull
    private Date toTime;

    public Date getFromTime() {
        return (fromTime == null ? null : (Date) fromTime.clone());
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = (fromTime == null ? null : (Date) fromTime.clone());
    }

    public Date getToTime() {
        return (toTime == null ? null : (Date) toTime.clone());
    }

    public void setToTime(Date toTime) {
        this.toTime = (toTime == null ? null : (Date) toTime.clone());
    }
}
