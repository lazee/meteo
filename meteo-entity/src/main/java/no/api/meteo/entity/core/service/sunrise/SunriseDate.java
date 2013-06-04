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

package no.api.meteo.entity.core.service.sunrise;

import java.util.Date;

public class SunriseDate {

    private Date date;

    private Sun sun;

    private Moon moon;

    public Date getDate() {
        return (date == null ? null : (Date) date.clone());
    }

    public void setDate(Date date) {
        this.date = (date == null ? null : (Date) date.clone());
    }

    public Moon getMoon() {
        return moon;
    }

    public void setMoon(Moon moon) {
        this.moon = moon;
    }

    public Sun getSun() {
        return sun;
    }

    public void setSun(Sun sun) {
        this.sun = sun;
    }
}
