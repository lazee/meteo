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

package no.api.meteo.locationgroups.config;

import no.api.meteo.MeteoException;
import no.api.meteo.entity.extras.locationgroup.LocationGroup;

import java.io.InputStream;
import java.util.Map;

public final class ConfigLoader {

    private ConfigLoader() {
        // Intentional
    }

    public static Map<String, LocationGroup> load(InputStream inputStream) throws MeteoException {
        ConfigParser parser = new ConfigParser();
        return parser.parse(inputStream);
    }

    public static Map<String, LocationGroup> load(String data) throws MeteoException {
        ConfigParser parser = new ConfigParser();
        return parser.parse(data);
    }

}
