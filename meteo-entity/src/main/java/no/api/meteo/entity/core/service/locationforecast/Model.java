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

import java.util.Date;

public class Model {

    private Date to;

    private Date from;

    private Date runEnded;

    private Date nextRun;

    private Date termin;

    private String name;

    public Date getFrom() {
        return (from == null ? null : (Date) from.clone());
    }

    public void setFrom(Date from) {
        this.from = (from == null ? null : (Date) from.clone());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getNextRun() {
        return (nextRun == null ? null : (Date) nextRun.clone());
    }

    public void setNextRun(Date nextRun) {
        this.nextRun = (nextRun == null ? null : (Date) nextRun.clone());
    }

    public Date getRunEnded() {
        return (runEnded == null ? null : (Date) runEnded.clone());
    }

    public void setRunEnded(Date runEnded) {
        this.runEnded = (runEnded == null ? null : (Date) runEnded.clone());
    }

    public Date getTermin() {
        return (termin == null ? null : (Date) termin.clone());
    }

    public void setTermin(Date termin) {
        this.termin = (termin == null ? null : (Date) termin.clone());
    }

    public Date getTo() {
        return (to == null ? null : (Date) to.clone());
    }

    public void setTo(Date to) {
        this.to = (to == null ? null : (Date) to.clone());
    }
}
