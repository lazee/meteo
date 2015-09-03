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

package no.api.meteo.entity.core.service.textforecast.query;

import java.util.Optional;

import static no.api.meteo.entity.core.service.textforecast.query.Language.NB;

/**
 * As the different text forecasts are in a different format, and no good XSDs are available, we only support the
 * land forecast now, as that is what we needed at the time.
 *
 * It will be a real mess to support the other formats. Still thinking about how to solve this.
 */
public enum ForecastQuery {

    /*LANDDAY0_NB("landday0", NB),

    LANDDAY1_NB("landday1", NB),

    LANDDAY2_NB("landday2", NB),

    LANDLONG_NB("landlong", NB), */

    LAND_NB("land", NB);

    /*COAST_NB("coast", NB),

    COAST_EN("coast", EN),

    SEABANKDAY1_NB("seabankday1", NB),

    SEABANKDAY1_EN("seabankday1", EN),

    SEABANKDAY2_NB("seabankday2", NB),

    SEABANKLONG_NB("seabanklong", NB),

    SEABANK_NB("seabank", NB),

    SEAOSLOFJORD_NB("seaoslofjord", NB),

    SEAHIGH_NB("seahigh", NB),

    SEAHIGH_EN("seahigh", EN),

    OBS_NB("obs", NB),

    EASTER_NB("easter", NB),

    GALE_NB("gale", NB),

    GALE_EN("gale", EN),

    GALE_NN("gale", NN),

    AW_ENGM_EN("aw_engm", EN),

    AW_ENRY_EN("aw_enry", EN),

    AW_ENZV_EN("aw_enzv", EN),

    IGA_FBNO41_EN("iga_fbno41", EN),

    IGA_FBNO42_EN("iga_fbno42", EN),

    IGA_FBNO43_EN("iga_fbno43", EN),

    IGA_FBNO44_EN("iga_fbno44", EN),

    IGA_FBNO45_EN("iga_fbno45", EN),

    ROUTE_FBNO69_EN("route_fbno69", EN),

    ROUTE_FBNO70_EN("route_fbno70", EN),

    ROUTE_FBNO72_EN("route_fbno72", EN),

    ROUTE_FBNO74_EN("route_fbno74", EN),

    ROUTE_FBNO75_EN("route_fbno75", EN),

    ROUTE_FBNO76_EN("route_fbno76", EN),

    ROUTE_FBNO77_EN("route_fbno77", EN),

    ROUTE_FBNO78_EN("route_fbno78", EN),

    ROUTE_FBNO79_EN("route_fbno79", EN),

    ROUTE_FBNO80_EN("route_fbno80", EN);*/

    private final String name;

    private final Language language;

    ForecastQuery(String name, Language language) {
        this.name = name;
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public Language getLanguage() {
        return language;
    }

    public static Optional<ForecastQuery> findByValue(String name, Language language) {
        if (name == null || language == null) {
            return Optional.empty();
        }
        for (ForecastQuery type : ForecastQuery.values()) {
            if (type.getName().equals(name) && type.getLanguage() == language) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }
}
