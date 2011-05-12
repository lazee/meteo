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

package no.api.meteo.util;

import no.api.meteo.MeteoException;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Util class for dealing with different net issues.
 */
public class MeteoNetUtils {

    private MeteoNetUtils() {
        // Intentional
    }

    /**
     * Wrap the construction of URL's to avoid throwing of checked MalformedURLException. Instead MeteoException
     * is thrown.
     *
     * @param url The url string to be used as the spec for the create URL object.
     * @return URL object created from the given url spec.
     * @throws no.api.meteo.MeteoException If a URL couldn't be created from the given url spec.
     */
    public static URL createUrl(String url) throws MeteoException {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new MeteoException(e);
        }
    }

}
