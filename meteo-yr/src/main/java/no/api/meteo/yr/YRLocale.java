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

package no.api.meteo.yr;

/**
 * Locales supported by yr.no given in ISO 639-1 (http://en.wikipedia.org/wiki/ISO_639-1)
 */
public enum YRLocale {

    NO("sted"), NN("stad"), EN("place");

    // Needed when constructing the correct url.
    private String place;

    YRLocale(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }
}
