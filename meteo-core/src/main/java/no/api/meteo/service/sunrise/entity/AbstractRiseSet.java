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

package no.api.meteo.service.sunrise.entity;

import java.util.Date;

public abstract class AbstractRiseSet {

    private Date rise;

    private Date set;

    public Date getRise() {
        return (rise == null ? null : (Date) rise.clone());
    }

    public void setRise(Date rise) {
        this.rise = (rise == null ? null : (Date) rise.clone());
    }

    public Date getSet() {
        return (set == null ? null : (Date) set.clone());
    }

    public void setSet(Date set) {
        this.set = (set == null ? null : (Date) set.clone());
    }

}
